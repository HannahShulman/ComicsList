package com.hanna.zava.comicslist.di.modules

import android.app.Application
import androidx.room.Room
import com.hanna.zava.comicslist.datasource.db.AppDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DbModule {

    @Provides
    @Singleton
    @Suppress("SpreadOperator")
    open fun provideDB(application: Application): AppDb =
        Room.databaseBuilder(application, AppDb::class.java, "app-db").build()
}