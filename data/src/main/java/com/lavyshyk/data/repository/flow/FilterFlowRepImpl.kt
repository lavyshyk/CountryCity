package com.lavyshyk.data.repository.flow

import com.lavyshyk.domain.filter.Filter
import com.lavyshyk.domain.repository.flow.FilterFlowRep
import kotlinx.coroutines.flow.MutableStateFlow

class FilterFlowRepImpl: FilterFlowRep {
    private val mFilterStateFlow = MutableStateFlow(Filter())

    override fun getFilterSubject(): MutableStateFlow<Filter> = mFilterStateFlow


    override fun processNewQuery(query: String) {
        mFilterStateFlow.value.name = query
    }

    override fun processNewArea(minArea: Float, maxArea: Float) {
        mFilterStateFlow.value.minArea = minArea
        mFilterStateFlow.value.maxArea = maxArea
    }

    override fun processNewPopulation(minPopulation: Float, maxPopulation: Float) {
        mFilterStateFlow.value.minPopulation = minPopulation
        mFilterStateFlow.value.maxPopulation = maxPopulation
    }

    override fun processNewDistance(distance: Float) {
        mFilterStateFlow.value.distance = distance

    }
}