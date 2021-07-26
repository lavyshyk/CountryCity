package com.lavyshyk.countrycity.base.mpv

interface IBaseMvpView {

    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()
}