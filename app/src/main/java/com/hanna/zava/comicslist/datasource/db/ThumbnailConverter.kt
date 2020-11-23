package com.hanna.zava.comicslist.datasource.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hanna.zava.comicslist.model.Thumbnail

//Prototypes - V
//Tests - V
class ThumbnailConverter {

    @TypeConverter
    fun stringToThumbnail(thumbnailValue: String): Thumbnail {
        return Gson().fromJson(thumbnailValue, Thumbnail::class.java)
    }

    @TypeConverter
    fun thumbnailToString(value: Thumbnail): String {
        return Gson().toJson(value)
    }
}