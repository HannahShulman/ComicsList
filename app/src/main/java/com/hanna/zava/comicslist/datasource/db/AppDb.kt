package com.hanna.zava.comicslist.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanna.zava.comicslist.model.Comic

//Prototypes - N/A
//Tests - N/A
@Database(entities = [Comic::class], version = 1)
abstract class AppDb: RoomDatabase(){
    abstract fun comicDao(): ComicDao
}