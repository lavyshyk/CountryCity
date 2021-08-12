package com.lavyshyk.countrycity.base.mpv

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseMvpPresenter<View : IBaseMvpView> {

    private lateinit var mView: View
    private val mCompositeDisposable = CompositeDisposable()

    fun attachView(view: View) {
        mView = view
    }

    protected open fun getView(): View? = mView

    fun onDestroyView() {
        mCompositeDisposable.clear()
    }

    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun <Data> inBackground(flowable: Flowable<Data>): Flowable<Data> {
        return flowable.observeOn((AndroidSchedulers.mainThread()))
            .subscribeOn(Schedulers.io())
    }

    fun <Data> handleProgress(flowable: Flowable<Data>, isRefresh: Boolean): Flowable<Data> {
        return flowable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!isRefresh) {
                    getView()?.showProgress()
                }
            }.doOnNext {
                getView()?.hideProgress()
            }
            .observeOn(Schedulers.io())
    }

    fun <Data> handleProgress(flowable: Flowable<Data>): Flowable<Data> {
        return flowable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                    getView()?.showProgress()
                }
            .doOnNext {
                getView()?.hideProgress()
            }
            .observeOn(Schedulers.io())
    }



    //    inline fun <reified ReifiedType : ViewType> instantiateDummyView(): ReifiedType {
//        return ReifiedType::class.java.getConstructor().newInstance()
//    }

}