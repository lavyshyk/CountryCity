package com.lavyshyk.data.iterator

import com.lavyshyk.domain.dto.capital.CapitalDto
import com.lavyshyk.domain.outcome.Outcome
import com.lavyshyk.domain.repository.flow.FilterFlowRep
import com.lavyshyk.domain.repository.flow.NetworkFlowCapitalRepository
import kotlinx.coroutines.flow.*

class IteratorFilterByNameCapital(
    private val filterFlowRepository: FilterFlowRep,
    private val networkFlowCapitalRepository: NetworkFlowCapitalRepository,
) {

    fun getFiltratedDataCapital(): Flow<Outcome<MutableList<CapitalDto>>> {
        return filterFlowRepository.getFilterSubject()
            .debounce(500)
            .flatMapMerge { filter ->
                networkFlowCapitalRepository.getAllCapitals().map { outcome ->
                    if (outcome is Outcome.Success<MutableList<CapitalDto>>) {
                        return@map (Outcome.success(outcome.data.filter {
                            it.capital.contains(filter.name, true)
                        }.toMutableList()))
                    } else {
                        return@map outcome
                    }
                }
            }
    }

    fun setNewQuery(query : String) = filterFlowRepository.processNewQuery(query)
}