package com.example.rxjavastudy

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.rxjavastudy.ui.GiphyListFragment
import org.junit.Test

class GiphyListFragmentTest: MockUiBase() {
    @Test
    fun giphy_list_fragment() {
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GiphyListFragment>(fragmentArgs)
        uiWait(3000)
        scenario.close()
    }
}
