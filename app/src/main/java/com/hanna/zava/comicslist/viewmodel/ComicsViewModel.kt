package com.hanna.zava.comicslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hanna.zava.comicslist.OpenForTesting
import com.hanna.zava.comicslist.model.Comic
import com.hanna.zava.comicslist.repository.ComicsRepository
import javax.inject.Inject

@OpenForTesting
class ComicsViewModel @Inject constructor(private val repository: ComicsRepository) :
    ViewModel() {

    suspend fun comicsList() = repository.getAllComics()
    suspend fun getComicById(id: Int): Comic = repository.getComicById(id)
}

class ComicsViewModelFactory @Inject constructor(private val repository: ComicsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ComicsViewModel(repository) as T
    }
}