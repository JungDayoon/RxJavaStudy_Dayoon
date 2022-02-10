package com.example.rxjavastudy.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rxjavastudy.data.Gif
import com.example.rxjavastudy.network.GiphyApiClient
import com.example.rxjavastudy.ui.GiphyListFragment.Companion.LOAD_COUNT
import com.example.rxjavastudy.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CheckResult")
class GiphyListViewModel @Inject constructor(
    private val giphyApiClient: GiphyApiClient
): BaseViewModel() {
    private val giphyList: ArrayList<Gif?> = arrayListOf()
    val giphyListLiveData: MutableLiveData<ArrayList<Gif?>> = MutableLiveData(arrayListOf())
    val blinkListLiveData: MutableLiveData<List<Int>> = MutableLiveData(listOf())

    val searchMode = MutableLiveData(SearchModeType.THROTTLE)

    val throttlingSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val debouncingSubject: BehaviorSubject<String> = BehaviorSubject.create()

    var searchQuery: String? = null

    fun getRandomGiphy(count: Long) {
        blinkListLiveData.value = listOf()
        disposeBag.disposeExclusiveDisposable(RANDOM_BLINK_TAG)

        disposeBag.addExclusive(giphyApiClient.getRandomGiphy()
            .repeat(count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    giphyList.add(response.data)
                    giphyListLiveData.value = giphyList
                },
                {
                    Log.e(TAG, "getRandomGiphy error: response is not successful or response.body is null")
                }
            ))
    }

    private fun randomBlink(data: List<Gif>) {
        disposeBag.addExclusive(Observable
            .interval(5, TimeUnit.SECONDS)
            .flatMap {
                Observable.just(shuffleGiphy(data.size))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    blinkListLiveData.postValue(response)
                },
                {
                    Log.e(TAG, "randomBlink error")
                }
            ), RANDOM_BLINK_TAG)
    }

    fun getSearchGiphyList(searchQuery: String, count: Int) {
        disposeBag.addExclusive(giphyApiClient.getSearchGiphyList(searchQuery, count, giphyList.size)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    giphyList.addAll(response.data)
                    giphyListLiveData.value = giphyList

                    randomBlink(response.data)
                },
                {
                    Log.e(TAG, "getSearchGiphy error: response is not successful or response.body is null")
                }
            ))
    }

    fun clearGiphyList() {
        giphyList.clear()
        giphyListLiveData.value = giphyList
    }

    private fun shuffleGiphy(size: Int): List<Int> {
        val indexList = (0..size).toMutableList()
        val randomKey = indexList.random()
        indexList.shuffle()

        Log.d("test", "after shuffle: $indexList")
        return indexList.subList(0, randomKey)
    }

    fun initSubject() {
        disposeBag.add(
            throttlingSubject
                .throttleLast(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.d(TAG, "throttling text: $it")
                }
                .subscribe(
                    {
                        searchQuery = it
                        clearGiphyList()

                        if (it.isBlank()) {
                            getRandomGiphy(LOAD_COUNT.toLong())
                        } else {
                            getSearchGiphyList(it, LOAD_COUNT)
                        }
                    },
                    {
                        Log.e(TAG, "throttling error")
                    }
                )
        )

        disposeBag.add(
            debouncingSubject
                .debounce(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.d(TAG, "debouncing text: $it")
                }
                .subscribe(
                    {
                        searchQuery = it
                        clearGiphyList()

                        if (it.isBlank()) {
                            getRandomGiphy(LOAD_COUNT.toLong())
                        } else {
                            getSearchGiphyList(it, LOAD_COUNT)
                        }
                    },
                    {
                        Log.e(TAG, "debouncing error")
                    })
        )
    }

    companion object {
        val TAG = GiphyListViewModel::class.java.name
        val RANDOM_BLINK_TAG = "RANDOM_BLINK"
    }
}
