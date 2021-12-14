package com.example.rxjavastudy.di.app

import android.app.Application
import com.example.rxjavastudy.Constants
import com.example.rxjavastudy.network.GiphyApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule(private val application: Application) {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun application() = application

    @Provides
    fun retrofit() = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.baseUrl)
        .build()

    @Provides
    fun giphyApi(retrofit: Retrofit) =
        retrofit.create(GiphyApi::class.java)
}
