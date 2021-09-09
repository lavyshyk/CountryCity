package com.lavyshyk.countrycity.base.mvvm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Job

open class BaseViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {

    protected val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    protected var mJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
        mJob?.cancel()
    }
}