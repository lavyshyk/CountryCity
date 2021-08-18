package com.lavyshyk.data.repository

import com.lavyshyk.domain.filter.CountryFilter
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FilterRepositoryImpl : com.lavyshyk.domain.repository.FilterRepository {

    private val mFilterSubject = BehaviorSubject.createDefault(CountryFilter())

    override fun getFilterSubject(): BehaviorSubject<CountryFilter> = mFilterSubject


    override fun processNewQuery(query: String) {
        mFilterSubject.value.name = query
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun processNewArea(minArea: Float, maxArea: Float) {
        mFilterSubject.value.minArea = minArea
        mFilterSubject.value.maxArea = maxArea
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun processNewPopulation(minPopulation: Float, maxPopulation: Float) {
        mFilterSubject.value.minPopulation = minPopulation
        mFilterSubject.value.maxPopulation = maxPopulation
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun processNewDistance(distance: Float) {
        mFilterSubject.value.distance = distance
        mFilterSubject.onNext(mFilterSubject.value)
    }

    override fun cleanFilterSubject() {
        mFilterSubject.value.name = ""
        mFilterSubject.value.minArea = 0F
        mFilterSubject.value.maxArea = 0F
        mFilterSubject.value.minPopulation = 0F
        mFilterSubject.value.maxPopulation = 0F
        mFilterSubject.value.distance = 0F
        mFilterSubject.onNext(mFilterSubject.value)
    }
}