package com.lavyshyk.countrycity.repository.networkRepository

import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.CountryInfoMapDto
import com.lavyshyk.countrycity.network.RESTCountryService
import com.lavyshyk.countrycity.util.transformToCountryDetailDto
import com.lavyshyk.countrycity.util.transformToCountryDto
import com.lavyshyk.countrycity.util.transformToCountryInfoMapDto
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: RESTCountryService) : NetworkRepository {

    override fun getInfoAboutCountry(nameCountry: String): Flowable<MutableList<CountryDataDetailDto>> =
        mService.getInfoAboutCountry(nameCountry).map { it.transformToCountryDetailDto() }

    override fun geCountryListByName(nameCountry: String): Flowable<MutableList<CountryDto>> =
        mService.geCountryListByName(nameCountry).map { it.transformToCountryDto() }

    override fun getCountriesInfo(): Flowable<MutableList<CountryDto>> =
        mService.getCountriesInfo().map { it.transformToCountryDto() }

    override fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoMapDto>> =
        mService.getInfoAboutAllCountryForMap().map { it.transformToCountryInfoMapDto() }
}