package com.lavyshyk.data.iterator

import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.FilterFlowRep
import com.lavyshyk.domain.repository.flow.NetworkFlowCountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class IteratorFilterCountyByCapitalName (
    private val networkFlowCountryRepository: NetworkFlowCountryRepository,
    private val filterFlowRepository: FilterFlowRep
        ) {

    fun getFiltratedDataCountry(): Flow<Outcome<MutableList<CountryDto>>> {
        return filterFlowRepository.getFilterSubject()
            .debounce(500)
            .flatMapMerge { filter ->
                networkFlowCountryRepository.getCountriesInfo().map { outcome ->
                    if (outcome is Outcome.Success<MutableList<CountryDto>>) {
                        return@map (Outcome.success(outcome.data.filter {
                            it.capital.lowercase().contains( filter.name, true )
                        }.toMutableList()))
                    } else {
                        return@map outcome
                    }
                }
            }
    }

    fun setCapitalQuery(query : String) = filterFlowRepository.processNewQuery(query)
    }