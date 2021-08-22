package com.lavyshyk.countrycity.base.mvvm

import java.util.concurrent.CancellationException

sealed class OutcomeCoroutine<T> {

    data class Running<T>(var loading: Boolean): OutcomeCoroutine<T>()
    data class Cancel<T>(var c: CancellationException): OutcomeCoroutine<T>()
    data class Result<T>(var data: T): OutcomeCoroutine<T>()
    data class FailureCor<T>(var t: Throwable): OutcomeCoroutine<T>()

    companion object {
        fun <T> running(isLoading: Boolean): OutcomeCoroutine<T> = Running(isLoading)
        fun <T> result(data: T): OutcomeCoroutine<T> = Result(data)
        fun <T> cancellation(c: CancellationException): OutcomeCoroutine<T> = Cancel(c)
        fun <T> failure(t: Throwable): OutcomeCoroutine<T> = FailureCor(t)

    }
}
