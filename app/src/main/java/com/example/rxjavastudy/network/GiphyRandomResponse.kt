package com.example.rxjavastudy.network

import com.example.rxjavastudy.data.Giphy

data class GiphyRandomResponse (
    val data: Giphy,
    val meta: Meta
)

data class Meta(
    val msg: String,
    val status: String,
    val resposne_id: String
)
