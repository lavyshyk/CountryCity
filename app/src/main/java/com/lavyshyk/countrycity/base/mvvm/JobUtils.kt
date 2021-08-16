package com.lavyshyk.countrycity.base.mvvm

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Extension function tu push the loading status to the observing outcome.
 */

fun <T> MutableLiveData<Outcome<T>>.loading(isLoading: Boolean) {
    this.value = Outcome.loading(isLoading)
}

/**
 * Extension function tu push a success event with data to the observing outcome.
 */

fun <T> MutableLiveData<Outcome<T>>.success(data: T) {
    with(this) {
        loading(false)
        value = Outcome.success(data)
    }
}

/**
 * Extension function to push a next event with data to the observing outcome.
 */

fun <T> MutableLiveData<Outcome<T>>.next(data: T) {
    with(this) {
        loading(false)
        value = Outcome.next(data)
    }
}

/**
 * Extension function to push failed event with an exception to the observing outcome.
 */

fun <T> MutableLiveData<Outcome<T>>.failed(t: Throwable) {
    with(this) {
        loading(false)
        value = Outcome.failure(t)
    }
}

/**
 * Executes a job [Flowable] in IO thread and manages state of executing.
 */


fun <T> executeJob(job: Flowable<T>, outcome: MutableLiveData<Outcome<T>>): Disposable {
    outcome.loading(true)
    return job.executeOnIOThread()
        .subscribe({
            outcome.next(it)
        }, {
            outcome.failed(it)
        }, {
            if (outcome.value is Outcome.Next) {
                outcome.success((outcome.value as Outcome.Next).data)
            }
        })
}

fun <T> executeJobInInitialThread(
    job: Flowable<T>,
    outcome: MutableLiveData<Outcome<T>>
): Disposable {
    outcome.loading(true)
    return job.subscribe({
        outcome.next(it)
    }, {
        outcome.failed(it)
    }, {
        if (outcome.value is Outcome.Next) {
            outcome.success((outcome.value as Outcome.Next).data)
        }
    })
}

/**
 *Execute job [Single] in IO tread and manages state of executing.
 */

//fun <T> executeJob(job: Single<T>, outcome: MutableLiveData<Outcome<T>>): Disposable {
//    outcome.loading(true)
//    return job.executeOnIOThread()
//        .subscribe({
//            outcome.success(it)
//        }, {
//            outcome.failed(it)
//        })
//}

/**
 * Execute job [Completable] in IO thread and manages state of executing.
 */

//fun executeJob(job:  Completable, outcome: MutableLiveData<Outcome<Any>>): Disposable {
//    outcome.loading(true)
//    return job.executeOnIOThread()
//        .subscribe({
//            outcome.success(Any())
//        }, {
//            outcome.failed(it)
//        })
//}

fun <T> executeJobWithoutProgress(
    job: Flowable<T>,
    outcome: MutableLiveData<Outcome<T>>
): Disposable {
    return job.executeOnIOThread()
        .subscribe({
            outcome.next(it)
        }, {
            outcome.failed(it)
        }, {
            if(outcome.value is Outcome.Next) {
                outcome.success((outcome.value as Outcome.Next).data)
            }
        })
}