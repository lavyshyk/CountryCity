package com.lavyshyk.domain.repository.flow

import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow


interface NetworkFlowCapitalRepository {

    fun getAllCapitals(): Flow<Outcome<MutableList<CapitalDto>>>

}