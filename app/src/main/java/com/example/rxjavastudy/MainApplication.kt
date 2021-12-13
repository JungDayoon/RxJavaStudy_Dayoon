package com.example.rxjavastudy

import android.app.Application
import com.example.rxjavastudy.di.AppModule
import com.example.rxjavastudy.di.DaggerAppComponent

class MainApplication: Application() {
    val appComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
