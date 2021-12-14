package com.example.rxjavastudy.di.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rxjavastudy.network.GiphyApiClient
import com.example.rxjavastudy.ui.GiphyListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(GiphyListViewModel::class)
    fun provideMainViewModel(
        giphyApiClient: GiphyApiClient
    ): ViewModel {
        return GiphyListViewModel(giphyApiClient)
    }
}
