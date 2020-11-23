package com.hanna.zava.comicslist.di.modules

import com.hanna.zava.comicslist.datasource.db.AppDb
import com.hanna.zava.comicslist.datasource.db.ComicDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DaoModule {

    @Provides
    @Singleton
    fun provideComicDao(db: AppDb): ComicDao = db.comicDao()
}