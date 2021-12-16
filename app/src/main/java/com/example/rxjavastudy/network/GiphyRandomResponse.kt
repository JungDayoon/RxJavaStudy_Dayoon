package com.example.rxjavastudy.network

import com.example.rxjavastudy.data.Giphy
import com.example.rxjavastudy.data.Meta

data class GiphyRandomResponse (
    val data: Giphy,
    val meta: Meta
)
