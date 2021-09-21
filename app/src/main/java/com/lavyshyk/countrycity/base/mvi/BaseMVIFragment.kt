package com.lavyshyk.countrycity.base.mvi

import android.os.Bundle
import android.view.View

abstract class BaseMVIFragment<INTENT : ViewIntent, ACTION : ViewAction, STATE : ViewState, VMODEL : BaseViewModel<INTENT, ACTION, STATE>>(
    private val modelClass: Class<VMODEL>
) :
    RootFragment(), IViewRenderer<STATE> {

    protected lateinit var viewState: STATE
    val mState get() = viewState

    private val mViewModel: VMODEL by lazy {
        viewModelProvider(this.viewModelFactory, modelClass.kotlin)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        initData()
        initEvent()
        super.onViewCreated(view, savedInstanceState)
        mViewModel.state.observe(viewLifecycleOwner, {
            viewState = it
            render(it)
        })

    }

    abstract fun initUI(view: View)
    abstract fun initData()
    abstract fun initEvent()
    abstract fun showError(error: String, throwable: Throwable)
    fun dispatchIntent(intent: INTENT) {
        mViewModel.dispatchIntent(intent)
    }
}