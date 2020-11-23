package com.hanna.zava.comicslist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hanna.zava.comicslist.datasource.db.ThumbnailConverter

data class ComicsResponse(val code: Int, val status: String, val data: ComicsData)

data class ComicsData(val offset: Int, val limit: Int, val total: Int, val count: Int, val results: List<Comic>)

@Entity
@TypeConverters(ThumbnailConverter::class)
data class Comic(
    @PrimaryKey val id: Int = 0,
    val title: String = "",
    val thumbnail: Thumbnail,
    val issueNumber: Int = 0,
    val variantDescription: String = "",
    val description: String?,
    val isbn: String = "",
    val upc: String = "",
    val pageCount: Int
)

data class Thumbnail(val path: String, val extension: String){
    val fullUrl: String
    get() = path.plus(".").plus(extension)
}