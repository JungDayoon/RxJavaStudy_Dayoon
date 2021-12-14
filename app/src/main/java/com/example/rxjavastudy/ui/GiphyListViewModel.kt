package com.example.rxjavastudy.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.network.GiphyApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GiphyListViewModel @Inject constructor(
    private val giphyApiClient: GiphyApiClient
): ViewModel() {
    private val _randomGiphy: MutableLiveData<Giphy?> = MutableLiveData()
    val randomGiphy get() = _randomGiphy

    fun fetchRandomGiphyList() {
        for (index in 0 until 10) {
            getRandomGiphy()
        }
    }

    private fun getRandomGiphy() {
        Log.d("giphyTest", "getRandomGiphy called")
        giphyApiClient.getRandomGiphy()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { response ->
                if (!response.isSuccessful || response.body() == null) {
                    Log.e(TAG, "getRandomGiphy error: response is not successful or response.body is null")
                    return@doOnSuccess
                }
                _randomGiphy.postValue(response.body()?.data)
            }
            .doOnError {
                Log.e(TAG, "getRandomGiphy error: response is not successful or response.body is null")
            }
            .subscribe()
    }

    companion object {
        val TAG = GiphyListViewModel.javaClass.name
    }
}
