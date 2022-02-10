package com.example.rxjavastudy.rxjava

import androidx.lifecycle.LifecycleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.HashMap

class DisposeBag : LifecycleObserver {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var exclusiveDisposableMap: MutableMap<String, Disposable>
    var isValid = true
        protected set

    private fun reset() {
        compositeDisposable = CompositeDisposable()
        exclusiveDisposableMap = HashMap()
    }

    fun add(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        if (!isValid) {
            disposable.dispose()
            return
        }
        compositeDisposable.add(disposable)
    }

    fun add(vararg disposables: Disposable?) {
        for (disposable in disposables) {
            add(disposable)
        }
    }

    @JvmOverloads
    fun addExclusive(disposable: Disposable, tag: String = DEFAULT_EXCLUSIVE_TAG) {
        if (!isValid) {
            disposable.dispose()
            return
        }
        registerExclusiveDisposable(disposable, tag)
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        reset()
        isValid = false
    }

    fun clear() {
        dispose()
        isValid = true
    }

    private fun registerExclusiveDisposable(disposable: Disposable, tag: String) {
        disposeExclusiveDisposable(tag)
        exclusiveDisposableMap[tag] = disposable
        compositeDisposable.add(disposable)
    }

    fun disposeExclusiveDisposable(tag: String) {
        if (exclusiveDisposableMap.containsKey(tag)) {
            val prevDisposable = exclusiveDisposableMap.remove(tag)
            compositeDisposable.remove(prevDisposable!!)
            if (!prevDisposable.isDisposed) {
                prevDisposable.dispose()
            }
        }
    }

    companion object {
        private const val DEFAULT_EXCLUSIVE_TAG = "defaultExclusive"
    }

    init {
        reset()
    }
}
