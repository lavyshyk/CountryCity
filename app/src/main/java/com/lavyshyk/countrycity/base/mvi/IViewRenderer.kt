package com.lavyshyk.countrycity.base.mvi

interface IViewRenderer<STATE> {
    fun render(state: STATE)
}