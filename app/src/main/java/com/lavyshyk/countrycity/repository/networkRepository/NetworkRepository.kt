package com.lavyshyk.countrycity.repository.networkRepository

import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.CountryInfoMapDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {


    fun getInfoAboutCountry(nameCountry: String): Flowable<MutableList<CountryDataDetailDto>>

    fun geCountryListByName(nameCountry: String): Flowable<MutableList<CountryDto>>

    fun getCountriesInfo(): Flowable<MutableList<CountryDto>>

    fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoMapDto>>

}