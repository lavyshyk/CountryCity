package com.lavyshyk.domain.useCase


abstract class UseCaseCoroutine<Params : Any, Result>  {

    private var mParams: Params? = null

    protected abstract suspend fun buildCoroutine(params: Params?): Result

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): UseCaseCoroutine<Params, Result> {
        mParams = params
        return this

    }

    suspend fun execute(): Result {
        require(!(mIsParamsRequired && mParams == null)) { "Params are required" }
        return buildCoroutine(mParams)
    }

}