package com.example.rxjavastudy.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.rxjavastudy.Constants
import com.example.rxjavastudy.data.Gif
import com.example.rxjavastudy.network.GiphyApiClient
import com.example.rxjavastudy.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CheckResult")
class GiphyListViewModel @Inject constructor(
    private val giphyApiClient: GiphyApiClient
): BaseViewModel() {
    var randomGiphyList = mutableStateListOf<Gif?>()

    val radioOptions = listOf(Constants.SEARCH_MODE_THROTTLING, Constants.SEARCH_MODE_DEBOUNCING)
    val searchMode = mutableStateOf(radioOptions[0])
    val searchQuery: MutableLiveData<String> = MutableLiveData("")

    val throttlingSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val debouncingSubject: BehaviorSubject<String> = BehaviorSubject.create()

    fun getRandomGiphy(count: Long) {
        randomGiphyList.clear()
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

    fun getSearchGiphyList(searchQuery: String, count: Int) {
        randomGiphyList.clear()
        disposeBag.add(giphyApiClient.getSearchGiphyList(searchQuery, count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    randomGiphyList.addAll(response.data)
                    Log.d(TAG, "getRandomGiphy randomGiphyList size: ${randomGiphyList.size}")
                },
                {
                    Log.e(TAG, "getRandomGiphy error: response is not successful or response.body is null")
                }
            ))
    }

    init {
        throttlingSubject
            .throttleLast(1000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG, "throttling text: $it")
            }
            .subscribe {
                if (it.isBlank()) {
                    getRandomGiphy(20)
                } else {
                    getSearchGiphyList(it, 20)
                }
            }

        debouncingSubject
            .debounce(1000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG, "debouncing text: $it")
            }
            .subscribe {
                if (it.isBlank()) {
                    getRandomGiphy(20)
                } else {
                    getSearchGiphyList(it, 20)
                }
            }
    }

    companion object {
        val TAG = GiphyListViewModel.javaClass.name
    }
}
