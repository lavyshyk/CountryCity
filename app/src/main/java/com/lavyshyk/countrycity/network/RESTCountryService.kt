package com.lavyshyk.countrycity.network

import com.lavyshyk.countrycity.model.CountryDataDetail
import com.lavyshyk.countrycity.model.CountryDataInfo
import com.lavyshyk.countrycity.model.CountryInfoForMap
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path


interface RESTCountryService {

    @GET("name/{name}")
    fun getInfoAboutCountry(@Path("name") nameCountry: String): Flowable<MutableList<CountryDataDetail>>

    @GET("all")
    fun getCountriesInfo(): Flowable<MutableList<CountryDataInfo>>

    @GET("all")
    fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoForMap>>


}
