package com.lavyshyk.countrycity.base.mvi

import android.view.View
import androidx.annotation.LayoutRes
import org.koin.androidx.scope.ScopeFragment

abstract class BaseMVIFragment<INTENT : ViewIntent, ACTION : ViewAction, STATE : ViewState>(@LayoutRes val layoutId: Int) : ScopeFragment(layoutId),
     IViewRenderer<STATE> {

   protected lateinit  var viewState: STATE
    val mState get() = viewState

    abstract fun initUI(view: View)
    abstract fun initData()
    abstract fun initEvent()
    abstract fun showError(error: String, throwable: Throwable)
}