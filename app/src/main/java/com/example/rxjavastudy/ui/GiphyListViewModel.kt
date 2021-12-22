package com.example.rxjavastudy.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.network.GiphyApiClient
import com.example.rxjavastudy.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GiphyListViewModel @Inject constructor(
    private val giphyApiClient: GiphyApiClient
): BaseViewModel() {
    val randomGiphyList = mutableStateListOf<Giphy?>()

    fun getRandomGiphy(count: Long) {
        disposeBag.add(giphyApiClient.getRandomGiphy()
            .repeat(count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    randomGiphyList.add(response.data)
                    Log.d(TAG, "getRandomGiphy randomGiphyList size: ${randomGiphyList.size}")
                },
                {
                    Log.e(TAG, "getRandomGiphy error: response is not successful or response.body is null")
                }
            ))

    }

    companion object {
        val TAG = GiphyListViewModel.toString()
    }
}
