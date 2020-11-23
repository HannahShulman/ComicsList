package com.hanna.zava.comicslist.di.components;

import com.hanna.zava.comicslist.repository.ComicsRepository
import com.hanna.zava.comicslist.datasource.db.ComicDao
import com.hanna.zava.comicslist.di.modules.*
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetModule::class, DaoModule::class, DbModule::class,
    RepositoryModule::class, SharedPreferenceModule::class])
interface NetComponent {

    val comicsRepository: ComicsRepository

    val comicDao: ComicDao
}
