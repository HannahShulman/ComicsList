package com.hanna.zava.comicslist.di.modules

import android.app.Application
import com.google.gson.GsonBuilder
import com.hanna.zava.comicslist.BuildConfig
import com.hanna.zava.comicslist.datasource.network.Api
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class NetModule {

    @Provides
    @Singleton
    open fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val date = Date().time
                val url =
                    request.url.newBuilder()
                        .addQueryParameter("apikey", "cf569476c509fffef0e02c4a58f76b62")
                        .addQueryParameter("ts", date.toString())//120L.toString())
                        .addQueryParameter(
                            "hash",
                            date.toString().plus("3364337dde0d8f28e0c9f524b9ae36c5e2998f62")
                                .plus("cf569476c509fffef0e02c4a58f76b62").toMD5()
                        )
                        .build()
                val newRequest = request.newBuilder().url(url).build()
                chain.proceed(newRequest)
            }
            .addInterceptor { chain: Interceptor.Chain ->
                val builder = chain.request().newBuilder()
                chain.proceed(builder.build())
            }
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(cache).build()
    }

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpCache(application: Application): Cache {
            val cacheSize = 10 * 1024 * 1024 // 10 MiB
            return Cache(application.cacheDir, cacheSize.toLong())
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            client: OkHttpClient
        ): Retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl(BuildConfig.SERVER_URL)
                .client(client)
                .build()

        @Provides
        @Singleton
        fun provideApi(retrofit: Retrofit): Api =
            retrofit.create(Api::class.java)
    }
}

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}