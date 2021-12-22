package com.example.rxjavastudy.ui.base

import androidx.activity.ComponentActivity
import com.example.rxjavastudy.MainApplication
import com.example.rxjavastudy.di.activity.ActivityModule

open class BaseActivity: ComponentActivity() {
    private val appComponent get() = (application as MainApplication).appComponent

    private val activityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this))
    }

    protected val injector get() = activityComponent
}
