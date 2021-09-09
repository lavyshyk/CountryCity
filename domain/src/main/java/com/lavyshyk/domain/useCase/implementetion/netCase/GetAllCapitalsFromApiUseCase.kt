package com.lavyshyk.domain.useCase.implementetion.netCase

import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.repository.NetworkRepositoryOnCoroutine
import com.lavyshyk.domain.useCase.CoroutineUseCase

class GetAllCapitalsFromApiUseCase(private val mNetworkRepositoryOnCoroutine: NetworkRepositoryOnCoroutine) :
    CoroutineUseCase<Unit, MutableList<CapitalDto>>() {

    override suspend fun buildCoroutine(params: Unit?): MutableList<CapitalDto> =
        mNetworkRepositoryOnCoroutine.getAllCapitals()


    override val mIsParamsRequired: Boolean
        get() = false

}