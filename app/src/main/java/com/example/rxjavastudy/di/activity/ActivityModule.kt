package com.example.rxjavastudy.di.activity

import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule (private val activity: ComponentActivity) {
    @Provides
    fun activity() = activity
}
