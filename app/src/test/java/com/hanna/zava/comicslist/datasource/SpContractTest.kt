package com.hanna.zava.comicslist.datasource

import android.os.Build
import androidx.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SpContractTest {
    private lateinit var spContract: SpContract

    @Before
    fun reset() {
        spContract = SpContract(//re-init sp for each test to start with clear data
            PreferenceManager.getDefaultSharedPreferences(
                InstrumentationRegistry.getInstrumentation().context
            )
        )
    }

    @Test
    fun `get last queried time, for first time returns 0`() {
        val lastFetched = spContract.lastTimeComicsFetched
        assertThat(lastFetched).isEqualTo(0)
    }

    @Test
    fun `get last queried time, after been set, returns set time ms`() {
        val ms: Long by lazy {
            Date().time
        }
        spContract.lastTimeComicsFetched = ms
        val lastFetched = spContract.lastTimeComicsFetched
        assertThat(lastFetched).isEqualTo(ms)
    }
}