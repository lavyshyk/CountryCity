package com.lavyshyk.data.repository.flow

import com.lavyshyk.data.ext.modifyFlow
import com.lavyshyk.data.model.countries.CountryDataInfo
import com.lavyshyk.data.network.CountryServiceFlow
import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.flow.NetworkFlowCountryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class NetworkFlowCountryRepositoryImpl(
    private val service: CountryServiceFlow,
    private val transformerCountry: Transformer<MutableList<CountryDataInfo>, MutableList<CountryDto>>,
    private val dispatcher: CoroutineDispatcher
): NetworkFlowCountryRepository {

    override fun getCountriesInfo(): Flow<Outcome<MutableList<CountryDto>>> =
        modifyFlow(service.getCountriesInfo(),transformerCountry,dispatcher)


}