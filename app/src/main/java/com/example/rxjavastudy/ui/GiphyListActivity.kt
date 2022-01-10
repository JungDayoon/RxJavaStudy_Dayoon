package com.example.rxjavastudy.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.example.rxjavastudy.Constants
import com.example.rxjavastudy.R
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import com.example.rxjavastudy.ui.base.BaseActivity
import javax.inject.Inject

class GiphyListActivity : BaseActivity() {
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var imageLoader: ImageLoader

    private val viewModel: GiphyListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GiphyListViewModel::class.java)
    }

    @InternalTextApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState)

        initViewModel()

        setContent {
            Column {
                RadioButtonGroupComponent()
                SearchQueryInputComponent()
                GiphyListGridComponent()
            }
        }
    }

    private fun initViewModel() {
        viewModel.getRandomGiphy(GIPHY_COUNT)
        viewModel.searchQuery.observe(this) {
            when (viewModel.searchMode.value) {
                Constants.SEARCH_MODE_THROTTLING -> {
                    viewModel.throttlingSubject.onNext(it)
                }
                Constants.SEARCH_MODE_DEBOUNCING -> {
                    viewModel.debouncingSubject.onNext(it)
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun GiphyListGridComponent() {
        val giphyList = viewModel.randomGiphyList
        LazyVerticalGrid(
            cells = GridCells.Fixed(COLUMN_NUM)
        ) {
            items(giphyList.size) {
                Image(
                    painter = rememberImagePainter(
                        data = giphyList[it]?.images?.fixed_height?.url,
                        imageLoader = imageLoader,
                        builder = {
                            placeholder(R.drawable.loading_icon)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )
            }
        }
    }

    @InternalTextApi
    @Composable
    fun SearchQueryInputComponent() {
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            value = viewModel.searchQuery.observeAsState().value.orEmpty(),
            onValueChange = {
                viewModel.searchQuery.value = it
            },
            label = {
                Text("Search Query")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                unfocusedIndicatorColor = Color.Gray
            )
        )
    }

    @Composable
    fun RadioButtonGroupComponent() {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column {
                viewModel.radioOptions.forEach { text ->
                    Row(Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == viewModel.searchMode.value),
                            onClick = { viewModel.searchMode.value = text }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
    @InternalTextApi
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
