package com.hanna.zava.comicslist.extensions

import android.os.Build
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentExtensionTests {

    @get:Rule
    var exception = ExpectedException.none()

    lateinit var scenario: FragmentScenario<Fragment>
    val key = "Bundle key"

    @Before
    fun setupScenario() {
        scenario = FragmentScenario.launch(
            Fragment::class.java,
            bundleOf(key to 4)
        )
    }

    @Test
    fun `extraNotNull returns right value`() {
        scenario.onFragment {
            val value = it.extraNotNull<Int>(key).value
            assertThat(value).isEqualTo(4)
        }
    }

    @Test
    fun `extraNotNull with non existing key, throws correct error`() {
        scenario.onFragment {
            exception.expectMessage("expected argument with key key was not in the argument bundle")
            it.extraNotNull<Int>("key").value
        }
    }

    @Test
    fun `extraNotNull with defined with wrong key, throws correct error`() {
        scenario.onFragment {
            exception.expectMessage("expected argument with key $key is not of the expected type")
            it.extraNotNull<String>(key).value
        }
    }
}