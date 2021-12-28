package com.example.rxjavastudy.network

import com.example.rxjavastudy.data.Gif
import com.example.rxjavastudy.data.Meta
import com.example.rxjavastudy.data.Pagination

data class GiphySearchResponse(
    val data: List<Gif>,
    val pagination: Pagination,
    val meta: Meta
)
