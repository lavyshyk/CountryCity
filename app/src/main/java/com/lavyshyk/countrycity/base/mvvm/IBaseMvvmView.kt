package com.lavyshyk.countrycity.base.mvvm

interface IBaseMvvmView {
    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()
}