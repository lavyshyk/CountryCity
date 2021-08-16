package com.lavyshyk.countrycity.base.mvp

import android.os.Bundle
import org.koin.androidx.scope.ScopeFragment

abstract class BaseMvpKoinFragment <View: IBaseMvpView, PresenterType: BaseMvpPresenter<View>> :  ScopeFragment(){


    protected lateinit var mPresenter: PresenterType

//    abstract fun createPresenter()
//
//    abstract fun getPresenter(): PresenterType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //createPresenter()
    }
}