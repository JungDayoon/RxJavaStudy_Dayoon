package com.example.rxjavastudy.ui

enum class SearchModeType (val index: Int, val text: String) {
    THROTTLE(0, "THROTTLE"),
    DEBOUNCE(1, "DEBOUNCE");

    fun toggle(): SearchModeType {
        return if (this == THROTTLE) {
            DEBOUNCE
        } else {
            THROTTLE
        }
    }
}
