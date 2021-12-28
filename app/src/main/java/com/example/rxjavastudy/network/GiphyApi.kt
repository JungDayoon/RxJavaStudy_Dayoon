package com.example.rxjavastudy.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("/v1/gifs/random")
    fun getRandomGif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String?,
        @Query("rating") rating: String?,
        @Query("random_id") randomId: String?
    ): Single<GiphyRandomResponse>

    @GET("/v1/gifs/search")
    fun getSearchGifList(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
    ): Single<GiphySearchResponse>
}
