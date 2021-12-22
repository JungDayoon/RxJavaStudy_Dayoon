package com.example.rxjavastudy.di.app

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.rxjavastudy.Constants
import com.example.rxjavastudy.network.GiphyApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule(private val application: Application) {
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

    @Provides
    fun provideImageLoader() = ImageLoader.invoke(application.applicationContext).newBuilder()
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(application.applicationContext))
                } else {
                    add(GifDecoder())
                }
            }.build()
}
