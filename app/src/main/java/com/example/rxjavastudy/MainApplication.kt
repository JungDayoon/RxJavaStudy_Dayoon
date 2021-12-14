package com.example.rxjavastudy

import android.app.Application
import com.example.rxjavastudy.di.app.AppComponent
import com.example.rxjavastudy.di.app.AppModule
import com.example.rxjavastudy.di.app.DaggerAppComponent

class MainApplication: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
