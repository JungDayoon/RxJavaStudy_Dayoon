package com.example.rxjavastudy.di.activity

import com.example.rxjavastudy.di.viewmodel.ViewModelFactoryModule
import com.example.rxjavastudy.di.viewmodel.ViewModelModule
import com.example.rxjavastudy.ui.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class, ViewModelModule::class, ViewModelFactoryModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
}
