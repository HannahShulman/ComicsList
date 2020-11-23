package com.hanna.zava.comicslist.datasource.network

import com.hanna.zava.comicslist.model.ComicsResponse
import retrofit2.Response
import retrofit2.http.GET

//Prototypes - N/A
//Tests - N/A
interface Api {

    @GET("v1/public/comics")
    suspend fun getAllComics(): Response<ComicsResponse>
}