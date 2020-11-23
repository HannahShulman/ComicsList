package com.hanna.zava.comicslist.converters

import com.google.common.truth.Truth.assertThat
import com.hanna.zava.comicslist.datasource.db.ThumbnailConverter
import com.hanna.zava.comicslist.model.Thumbnail
import org.junit.Test


class ThumbnailConverterTest {

    @Test
    fun `convert string to Thumbnail`(){
        val string = ThumbnailConverter().stringToThumbnail("{\"extension\":\"jpg\",\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\"}")
        assertThat(string).isEqualTo(
            Thumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
                "jpg"
            )
        )
    }

    @Test
    fun `convert array to string`(){
        val string = ThumbnailConverter().thumbnailToString(
            Thumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
                "jpg"
            )
        )
        assertThat(string).isEqualTo("{\"extension\":\"jpg\",\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\"}")
    }
}