package com.example.rxjavastudy.di.presentation

import com.example.rxjavastudy.ui.GiphyListFragment
import dagger.Subcomponent

@Subcomponent
interface PresentationComponent {
    fun inject(giphyListFragment: GiphyListFragment)
}
