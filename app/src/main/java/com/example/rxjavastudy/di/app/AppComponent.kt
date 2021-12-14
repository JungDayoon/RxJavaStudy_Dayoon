package com.example.rxjavastudy.di.app

import com.example.rxjavastudy.di.activity.ActivityComponent
import com.example.rxjavastudy.di.activity.ActivityModule
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent
}
