package com.hanna.zava.comicslist.di.modules


import com.hanna.zava.comicslist.repository.ComicsRepository
import com.hanna.zava.comicslist.repository.ComicsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun comicsRepository(repository: ComicsRepositoryImpl): ComicsRepository
}