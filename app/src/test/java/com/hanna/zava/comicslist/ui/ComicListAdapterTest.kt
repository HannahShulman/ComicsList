package com.hanna.zava.comicslist.ui

import android.os.Build
import android.view.LayoutInflater
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.model.Comic
import com.hanna.zava.comicslist.model.Thumbnail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ComicListAdapterTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val clickListener = { _: Int -> }
    val adapter = ComicListAdapter(clickListener)

    val comic = Comic(
        title = "This is the comic title",
        thumbnail = Thumbnail("", ""), pageCount = 12, description = "this is a description"
    )

    @Test
    fun `diffUtil when ids are the same, items return as same items`() {
        val isSame = ComicListAdapter.diffUtil.areItemsTheSame(comic, comic)
        assertThat(isSame).isTrue()
    }

    @Test
    fun `diffUtil when ids are not the same, items return not as same items`() {
        val isSame = ComicListAdapter.diffUtil.areItemsTheSame(comic, comic.copy(id = 3))
        assertThat(isSame).isFalse()
    }

    @Test
    fun `diffUtil when comic content is the same, areContentsTheSame return true`() {
        val isSame = ComicListAdapter.diffUtil.areContentsTheSame(comic, comic)
        assertThat(isSame).isTrue()
    }

    @Test
    fun `diffUtil when comic content are not the same, areContentsTheSame return false`() {
        val isSame =
            ComicListAdapter.diffUtil.areContentsTheSame(comic, comic.copy(thumbnail = Thumbnail("path", "extension")))
            assertThat(isSame).isFalse()
    }

    @Test
    fun `view holder displays correct content`() {
        adapter.submitList(listOf(comic))
        val view = LayoutInflater.from(context).inflate(R.layout.single_comic_layout, null)
        val holder = ComicViewHolder(view, clickListener)
        adapter.onBindViewHolder(holder, 0)
        assertThat(holder.comicTextView.text).isEqualTo("This is the comic title")
    }

}