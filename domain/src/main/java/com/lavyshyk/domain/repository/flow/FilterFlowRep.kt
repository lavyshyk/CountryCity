package com.lavyshyk.domain.repository.flow

import com.lavyshyk.domain.filter.Filter
import kotlinx.coroutines.flow.MutableStateFlow

interface FilterFlowRep {

    fun getFilterSubject(): MutableStateFlow<Filter>

    fun processNewQuery(query: String)

    fun processNewArea(minArea: Float, maxArea: Float)

    fun processNewPopulation(minPopulation: Float, maxPopulation: Float)

    fun processNewDistance(distance: Float)
}