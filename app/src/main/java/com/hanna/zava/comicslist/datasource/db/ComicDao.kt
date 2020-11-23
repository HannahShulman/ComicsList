package com.hanna.zava.comicslist.datasource.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.hanna.zava.comicslist.model.Comic
import kotlinx.coroutines.flow.Flow

//Prototypes - N/A
//Tests - N/A
@Dao
interface ComicDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertComic(list: List<Comic>)

    @Query("SELECT * FROM Comic")
    suspend fun getAllComics(): List<Comic>

    @Query("SELECT * FROM Comic WHERE id =:id")
    suspend fun getComicById(id: Int): Comic

    @Query("DELETE FROM Comic")
    suspend fun deleteAllComics()

    @Transaction
    open suspend fun saveAllComics(list: List<Comic>) {
        deleteAllComics()
        insertComic(list)
    }
}