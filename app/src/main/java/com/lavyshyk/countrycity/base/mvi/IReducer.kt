package com.lavyshyk.countrycity.base.mvi

interface IReducer<STATE, T :Any> {
    fun reduce(result: Result<T>, state: STATE,): STATE
}