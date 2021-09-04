package com.lavyshyk.domain.repository.flow

import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow

interface NetworkFlowCountryRepository {

    fun getCountriesInfo(): Flow<Outcome<MutableList<CountryDto>>>

}