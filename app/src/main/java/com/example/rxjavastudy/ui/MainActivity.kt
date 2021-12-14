package com.example.rxjavastudy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.rxjavastudy.MainApplication
import com.example.rxjavastudy.R
import com.example.rxjavastudy.di.activity.ActivityModule
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import java.util.EnumSet.of
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val appComponent get() = (application as MainApplication).appComponent

    val activityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = GiphyListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
