package com.lavyshyk.data.repository.flow

import com.lavyshyk.data.ext.modifyFlow
import com.lavyshyk.data.model.Capital
import com.lavyshyk.data.network.CountryServiceFlow
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.flow.NetworkFlowCapitalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class NetworkFlowCapitalRepositoryImpl(
    private val service: CountryServiceFlow,
    private val transformerCapital: Transformer<MutableList<Capital>, MutableList<CapitalDto>>,
    private val dispatcher: CoroutineDispatcher
) : NetworkFlowCapitalRepository {

    override fun getAllCapitals(): Flow<Outcome<MutableList<CapitalDto>>> =
        modifyFlow(service.getAllCapitals(),transformerCapital,dispatcher)

}