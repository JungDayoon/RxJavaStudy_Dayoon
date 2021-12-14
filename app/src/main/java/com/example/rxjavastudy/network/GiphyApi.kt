package com.example.rxjavastudy.network

import com.example.rxjavastudy.data.Giphy
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("/v1/gifs/random")
    fun getRandomGif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String?,
        @Query("rating") rating: String?,
        @Query("random_id") randomId: String?
    ): Single<Response<GiphyRandomResponse>>
}
