package com.example.rxjavastudy.ui.base

import androidx.lifecycle.ViewModel
import com.example.rxjavastudy.rxjava.DisposeBag

open class BaseViewModel: ViewModel() {
    val disposeBag = DisposeBag()

    override fun onCleared() {
        super.onCleared()

        disposeBag.dispose()
    }
}
