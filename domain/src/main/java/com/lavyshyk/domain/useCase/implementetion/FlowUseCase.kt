package com.lavyshyk.domain.useCase.implementetion


abstract class FlowUseCase<Params : Any, Result> {

    private var mParams: Params? = null

    protected abstract fun buildFlow(params: Params?): Result

    abstract val mIsParamsRequired: Boolean

    fun setParams(params: Params): FlowUseCase<Params, Result> {
        mParams = params
        return this
    }

    fun  execute(): Result {
        require(!(mIsParamsRequired && mParams == null)) { "Params are required" }
        return buildFlow(mParams)
    }

}
