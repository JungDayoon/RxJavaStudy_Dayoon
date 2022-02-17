package com.example.rxjavastudy.network

import com.example.rxjavastudy.data.Meta
import com.example.rxjavastudy.data.Term

data class GiphyAutoCompleteResponse (
    val data: List<Term>,
    val meta: Meta
)
