package com.hanna.zava.comicslist.ui

import android.os.Build
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.extensions.replace
import com.hanna.zava.comicslist.model.Comic
import com.hanna.zava.comicslist.model.Thumbnail
import com.hanna.zava.comicslist.viewmodel.ComicsViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ComicDetailsDialogTest {

    lateinit var scenario: FragmentScenario<ComicDetailsDialog>
    lateinit var factory: ComicDetailsDialogTestFactory
    private val viewModelMock: ComicsViewModel = mock {
        onBlocking { getComicById(4) } doReturn Comic(
            title = "This is the comic title",
            thumbnail = Thumbnail("", ""), pageCount = 12, description = "this is a description"
        )
    }

    @Before
    fun setupScenario() {
        factory = ComicDetailsDialogTestFactory(viewModelMock)
        scenario = FragmentScenario.launchInContainer(
            ComicDetailsDialog::class.java,
            bundleOf(ComicDetailsDialog.COMIC_ID to 4), R.style.Theme_AppCompat_NoActionBar, factory
        )
    }

    @Test
    fun `creating fragment by newInstance, passes value to argument with comic_id key`() {
        val fragment = ComicDetailsDialog.newInstance(4)
        val arguments = fragment.arguments
        assertThat(arguments?.get("comic_id")).isEqualTo(4)
    }

    @Test
    fun `comic dialog displays the comic description`() {
        runBlockingTest {
            scenario.onFragment {
                assertDisplayed(R.id.description, "Description: this is a description")
            }
        }
    }

    @Test
    fun `comic dialog displays the comic title`() {
        runBlockingTest {
            scenario.onFragment {
                assertDisplayed(R.id.title, "This is the comic title")
            }
        }
    }

    @Test
    fun `comic details dialog button clicked, dismisses dialog`() {
        runBlockingTest {
            scenario.onFragment {
                clickOn(R.id.primary_button)
                assertThat(it.isVisible).isFalse()
            }
        }
    }
}

class ComicDetailsDialogTestFactory constructor(
    var viewModelMock: ComicsViewModel
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        ComicDetailsDialog().apply {
            replace(ComicDetailsDialog::viewModel, viewModelMock)
        }
}