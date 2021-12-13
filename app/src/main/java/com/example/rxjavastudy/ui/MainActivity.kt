package com.example.rxjavastudy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rxjavastudy.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = GiphyListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.giphy_list_fragment, fragment)
            .commit()
    }
}
