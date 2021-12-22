package com.example.rxjavastudy.network

import com.example.rxjavastudy.Constants
import com.example.rxjavastudy.data.Giphy
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class GiphyApiClient @Inject constructor (
    private val giphyApi: GiphyApi
) {
    fun getRandomGiphy(): Single<GiphyRandomResponse> {
        return giphyApi.getRandomGif(Constants.apiKey, null, null, null)
    }
}


