package com.lavyshyk.countrycity.base.mvvm

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Extension function to subscribe [Single] on the IO thread and observe on the UI thread.
 * */
fun <T> Single<T>.executeOnIOThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Extension function to subscribe [Completable] on IO thread and observe in UI thread.
 */
fun  Completable.executeOnIOThread(): Completable{
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Extension function to subscribe [Flowable] on IO thread and observe in UI thread.
 */
fun <T> Flowable<T>.executeOnIOThread(): Flowable<T> {
    return this.
            observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

/**
 * Extension function in subscribe [Observable] on IO tread and observe in UI thread.
 */

fun <T> Observable<T>.executeOnIOThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Extension function to add a Disposable to a CompositeDisposable
 */

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}