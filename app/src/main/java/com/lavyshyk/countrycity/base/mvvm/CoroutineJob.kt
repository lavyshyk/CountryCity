package com.lavyshyk.countrycity.base.mvvm

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException


fun <T> MutableLiveData<OutcomeCoroutine<T>>.running(isLoading: Boolean) {
    this.value = OutcomeCoroutine.running(isLoading)
}

fun <T> MutableLiveData<OutcomeCoroutine<T>>.result(data: T) {
    this.apply {
        running(false)
        value = OutcomeCoroutine.result(data)
    }
}

fun <T> MutableLiveData<OutcomeCoroutine<T>>.cancellation(c: CancellationException) {
    this.apply {
        running(false)
        value = OutcomeCoroutine.cancellation(c)
    }
}

fun <T> MutableLiveData<OutcomeCoroutine<T>>.failed(t: Throwable) {
    this.apply {
        running(false)
        value = OutcomeCoroutine.failure(t)
    }
}

fun <T> coroutineJobIO(
    coroutineScope: CoroutineScope, block: suspend () -> T,
    outcomeCoroutine: MutableLiveData<OutcomeCoroutine<T>>
) : Job {
    outcomeCoroutine.running(true)
    return  CoroutineScope(coroutineScope.coroutineContext).launch {
        try {
            val response = withContext(coroutineScope.coroutineContext + Dispatchers.IO) {
                block.invoke()
            }
            outcomeCoroutine.result(response)
        } catch (c: CancellationException) {
            outcomeCoroutine.cancellation(c)
        } catch (t: Throwable) {
            outcomeCoroutine.failed(t)
        }
    }

}