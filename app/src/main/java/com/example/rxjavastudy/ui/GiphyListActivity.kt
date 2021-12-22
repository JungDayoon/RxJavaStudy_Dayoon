package com.example.rxjavastudy.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import com.example.rxjavastudy.ui.base.BaseActivity
import javax.inject.Inject

class GiphyListActivity : BaseActivity() {
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var imageLoader: ImageLoader

    private val viewModel: GiphyListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GiphyListViewModel::class.java)
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState)

        initViewModel()

        setContent {
            GridView(viewModel)
        }
    }

    private fun initViewModel() {
        viewModel.getRandomGiphy(GIPHY_COUNT)
    }

    @ExperimentalFoundationApi
    @Composable
    fun GridView(viewModel: GiphyListViewModel) {
        ShowItems(viewModel.randomGiphyList)
    }

    @ExperimentalFoundationApi
    @Composable
    fun ShowItems(giphyList: List<Giphy?>) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(COLUMN_NUM)
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

    companion object {
        const val COLUMN_NUM = 3
        const val GIPHY_COUNT = 20L
    }
}
