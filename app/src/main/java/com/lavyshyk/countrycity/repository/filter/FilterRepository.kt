package com.lavyshyk.countrycity.repository.filter

import io.reactivex.rxjava3.subjects.BehaviorSubject

interface FilterRepository {

    fun getFilterSubject(): BehaviorSubject<CountryFilter>

    fun processNewQuery(query: String)

    fun processNewArea(minArea: Float, maxArea: Float)

    fun processNewPopulation(minPopulation: Float, maxPopulation: Float)

    fun processNewDistance(distance: Float)



}