package com.lavyshyk.countrycity.base.mvp

interface IBaseMvpView {

    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()
}