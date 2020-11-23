package com.hanna.zava.comicslist.repository

import android.text.format.DateUtils
import com.hanna.zava.comicslist.datasource.SpContract
import com.hanna.zava.comicslist.datasource.db.ComicDao
import com.hanna.zava.comicslist.datasource.network.Api
import com.hanna.zava.comicslist.datasource.network.FlowNetworkBoundResource
import com.hanna.zava.comicslist.datasource.network.Resource
import com.hanna.zava.comicslist.model.Comic
import com.hanna.zava.comicslist.model.ComicsResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*
import javax.inject.Inject

interface ComicsRepository {
    suspend fun getAllComics(): Flow<Resource<List<Comic>>>
    suspend fun getComicById(id: Int): Comic
}

class ComicsRepositoryImpl @Inject constructor(
    private val api: Api,
    private val comicDao: ComicDao,
    private val spContract: SpContract
) : ComicsRepository {

    override suspend fun getAllComics(): Flow<Resource<List<Comic>>> {
        return object : FlowNetworkBoundResource<List<Comic>, ComicsResponse>() {
            override suspend fun saveNetworkResult(item: ComicsResponse) {
                spContract.lastTimeComicsFetched = Date().time
                withContext(Dispatchers.Default) { comicDao.saveAllComics(item.data.results) }
            }

            override suspend fun loadFromDb(): List<Comic> {
                return comicDao.getAllComics()
            }

            override suspend fun fetchFromNetwork(): Response<ComicsResponse> {
                return api.getAllComics()
            }

            override fun shouldFetch(): Boolean {
                return true//Date().time.minus(DateUtils.DAY_IN_MILLIS) >= spContract.lastTimeComicsFetched
            }
        }.asFlow()
    }

    override suspend fun getComicById(id: Int): Comic {
        return comicDao.getComicById(id)
    }
}