package com.example.rxjavastudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.rxjavastudy.network.GiphyApiClient
import com.example.rxjavastudy.network.GiphyRandomResponse
import com.example.rxjavastudy.network.GiphySearchResponse
import com.example.rxjavastudy.ui.GiphyListViewModel
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class GiphyListViewModelTest {
    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule()

    @JvmField
    @Rule
    var testScheduleRule = RxSchedulerRule()

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: GiphyListViewModel

    @Mock lateinit var mockApiClient: GiphyApiClient

    @Mock lateinit var mockGiphyRandomResponse: GiphyRandomResponse

    @Mock lateinit var mockGiphySearchResponse: GiphySearchResponse

    @Before
    fun setup() {
        viewModel = spy(GiphyListViewModel(mockApiClient))
        `when`(mockApiClient.getRandomGiphy()).thenReturn(Single.just(mockGiphyRandomResponse))
        `when`(mockApiClient.getSearchGiphyList(anyString(), anyInt(), anyInt())).thenReturn(Single.just(mockGiphySearchResponse))
    }

    @Test
    fun getRandomGiphyTest() {
        viewModel.getRandomGiphy(30)
        Assert.assertTrue(viewModel.giphyListLiveData.value != null)
        Assert.assertTrue(viewModel.giphyListLiveData.value!!.contains(mockGiphyRandomResponse.data))
        Assert.assertEquals(viewModel.giphyListLiveData.value!!.size, 30)
    }

    @Test
    fun getSearchGiphyTest() {
        viewModel.getSearchGiphyList("test", 30)
        Assert.assertEquals(viewModel.giphyListLiveData.value, mockGiphySearchResponse.data)
    }

    @Test
    fun clearGiphyListTest() {
        viewModel.giphyListLiveData.value?.addAll(mockGiphySearchResponse.data)
        viewModel.clearGiphyList()
        Assert.assertTrue(viewModel.giphyListLiveData.value.isNullOrEmpty())
    }
}
