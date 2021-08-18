package com.lavyshyk.data.repository

import com.lavyshyk.data.network.RESTCountryService
import com.lavyshyk.data.transformToCountryDetailDto
import com.lavyshyk.data.transformToCountryDto
import com.lavyshyk.data.transformToCountryInfoMapDto
import com.lavyshyk.domain.dto.CountryDataDetailDto
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.dto.CountryInfoMapDto
import com.lavyshyk.domain.repository.NetworkRepository
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: RESTCountryService) :
    NetworkRepository {

    override fun getInfoAboutCountry(nameCountry: String): Flowable<MutableList<CountryDataDetailDto>> =
        mService.getInfoAboutCountry(nameCountry).map { it.transformToCountryDetailDto() }

    override fun geCountryListByName(nameCountry: String): Flowable<MutableList<CountryDto>> =
        mService.geCountryListByName(nameCountry).map { it.transformToCountryDto() }

    override fun getCountriesInfo(): Flowable<MutableList<CountryDto>> =
        mService.getCountriesInfo().map { it.transformToCountryDto() }

    override fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoMapDto>> =
        mService.getInfoAboutAllCountryForMap().map { it.transformToCountryInfoMapDto() }
}