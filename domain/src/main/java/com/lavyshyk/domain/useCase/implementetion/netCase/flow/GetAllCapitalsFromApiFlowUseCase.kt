package com.lavyshyk.domain.useCase.implementetion.netCase.flow

import com.lavyshyk.domain.dto.capital.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.NetworkFlowCapitalRepository
import com.lavyshyk.domain.useCase.implementetion.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetAllCapitalsFromApiFlowUseCase(private val mNetworkFlowCapitalRepository: NetworkFlowCapitalRepository
):
    FlowUseCase<Unit, Flow<Outcome<MutableList<CapitalDto>>>>() {
    override fun buildFlow(params: Unit?): Flow<Outcome<MutableList<CapitalDto>>> =
        mNetworkFlowCapitalRepository.getAllCapitals()

    override val mIsParamsRequired: Boolean
        get() = false
}