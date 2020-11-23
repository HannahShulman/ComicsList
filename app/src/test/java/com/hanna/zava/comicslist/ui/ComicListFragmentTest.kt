package com.hanna.zava.comicslist.ui

import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.datasource.network.Resource
import com.hanna.zava.comicslist.extensions.replace
import com.hanna.zava.comicslist.model.Comic
import com.hanna.zava.comicslist.model.Thumbnail
import com.hanna.zava.comicslist.viewmodel.ComicsViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ComicListFragmentTest {

    lateinit var scenario: FragmentScenario<ComicsListFragment>
    lateinit var factory: ComicListFragmentTestFactory
    val initListResource = Resource.success(
        listOf(
            Comic(
                title = "This is the comic title",
                thumbnail = Thumbnail("", ""), pageCount = 12, description = "this is a description"
            )
        )
    )
    val listValue = MutableStateFlow<Resource<List<Comic>>>(initListResource)
    private val viewModelMock: ComicsViewModel = mock {
        onBlocking { comicsList() } doReturn listValue
    }

    private var clickCounter = 0

    @Before
    fun setupScenario() {
        clickCounter = 0
        factory = ComicListFragmentTestFactory(viewModelMock)
        scenario = FragmentScenario.launchInContainer(
            ComicsListFragment::class.java,
            null, R.style.Theme_AppCompat_NoActionBar, factory
        )
    }

    @Test
    fun `when list successfully received with data, list length is same as data length`() {
        listValue.value = Resource.success(
            listOf(
                Comic(
                    title = "This is the comic title",
                    thumbnail = Thumbnail("", ""),
                    pageCount = 12,
                    description = "this is a description"
                )
            )
        )
        scenario.onFragment {
            val rv = it.view!!.findViewById<RecyclerView>(R.id.comics_list)
            assertThat(rv!!.adapter?.itemCount).isEqualTo(1)
        }
    }

    @Test
    fun `when successful resource received, main_view is visible and loading view is gone`() {
        listValue.value = Resource.success(
            listOf(
                Comic(
                    title = "This is the comic title",
                    thumbnail = Thumbnail("", ""),
                    pageCount = 12,
                    description = "this is a description"
                )
            )
        )
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.main_view)
            val loadingView = it.view!!.findViewById<View>(R.id.loading_view)
            assertThat(mainView.isVisible).isTrue()
            assertThat(loadingView.isVisible).isFalse()
        }
    }

    @Test
    fun `when resource loading with no data, main_view is invisible and loading view is visible`() {
        listValue.value = Resource.loading(null)
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.main_view)
            val loadingView = it.view!!.findViewById<View>(R.id.loading_view)
            assertThat(mainView.isVisible).isFalse()//.isTrue()
            assertThat(loadingView.isVisible).isTrue()
        }
    }

    @Test
    fun `when resource loading with data, main_view is visible and loading view is gone`() {
        listValue.value = Resource.loading(
            listOf(
                Comic(
                    title = "This is the comic title",
                    thumbnail = Thumbnail("", ""),
                    pageCount = 12,
                    description = "this is a description"
                )
            )
        )
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.main_view)
            val loadingView = it.view!!.findViewById<View>(R.id.loading_view)
            assertThat(mainView.isVisible).isTrue()
            assertThat(loadingView.isVisible).isFalse()
        }
    }
}

class ComicListFragmentTestFactory constructor(
    var viewModelMock: ComicsViewModel
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        ComicsListFragment().apply {
            replace(ComicsListFragment::viewModel, viewModelMock)
        }
}