package com.lavyshyk.countrycity.base.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseMvpFragment<View: IBaseMvpView, PresenterType: BaseMvpPresenter<View>> : Fragment() {

    protected lateinit var mPresenter: PresenterType

    abstract fun createPresenter()

    abstract fun getPresenter(): PresenterType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //createPresenter()
    }
}