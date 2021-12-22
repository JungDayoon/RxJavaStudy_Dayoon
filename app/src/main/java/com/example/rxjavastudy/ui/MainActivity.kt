package com.example.rxjavastudy.ui

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.rxjavastudy.MainApplication
import com.example.rxjavastudy.R
import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.di.activity.ActivityModule
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private val appComponent get() = (application as MainApplication).appComponent

    private val activityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this))
    }

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: GiphyListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GiphyListViewModel::class.java)
    }

    private val giphyList: ArrayList<Giphy?> = arrayListOf()

    private val imageLoader = ImageLoader.invoke(this).newBuilder()
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(applicationContext))
            } else {
                add(GifDecoder())
            }
        }.build()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)

        initViewModel()

        setContent {
            GridView(viewModel)
        }
    }

    private fun initViewModel() {
        viewModel.getRandomGiphy()

        viewModel.randomGiphy.observe(this, {
            giphyList.add(it)
        })
    }

    @ExperimentalFoundationApi
    @Composable
    fun GridView(viewModel: GiphyListViewModel) {
        val complete = viewModel.complete.observeAsState(false)

        if (complete.value)
            ShowItems(giphyList)
    }

    @ExperimentalFoundationApi
    @Composable
    fun ShowItems(giphyList: ArrayList<Giphy?>) {
        Log.d("giphyTest", "showItems giphyList: $giphyList")

        LazyVerticalGrid(
            cells = GridCells.Fixed(3)
        ) {
            items(giphyList.size) {
                Image(
                    painter = rememberImagePainter(data = giphyList[it]?.images?.fixed_height?.url, imageLoader = imageLoader),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
            }
        }

    }

    @ExperimentalFoundationApi
    @Preview
    @Composable
    fun PreviewGridView() {
    }

}
