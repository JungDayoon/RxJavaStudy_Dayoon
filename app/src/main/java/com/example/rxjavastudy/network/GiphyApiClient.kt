package com.example.rxjavastudy.network

import com.example.rxjavastudy.Constants
import io.reactivex.Single
import javax.inject.Inject

class GiphyApiClient @Inject constructor (
    private val giphyApi: GiphyApi
) {
    fun getRandomGiphy(): Single<GiphyRandomResponse> {
        return giphyApi.getRandomGif(Constants.apiKey, null, null, null)
    }

    fun getSearchGiphyList(searchQuery: String, limit: Int, offset: Int): Single<GiphySearchResponse> {
        return giphyApi.getSearchGifList(Constants.apiKey, searchQuery, limit, offset)
    }
}


