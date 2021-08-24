package com.lavyshyk.domain.repository

import com.lavyshyk.domain.dto.CountryDataDetailDto
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.dto.CountryInfoMapDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {


    fun getInfoAboutCountry(nameCountry: String): Flowable<MutableList<CountryDataDetailDto>>

    fun geCountryListByName(nameCountry: String): Flowable<MutableList<CountryDto>>

    fun getCountriesInfo(): Flowable<MutableList<CountryDto>>

    fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoMapDto>>

}