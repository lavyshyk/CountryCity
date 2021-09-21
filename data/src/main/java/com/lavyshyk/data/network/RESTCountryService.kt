package com.lavyshyk.data.network


import com.lavyshyk.data.model.countries.CountryDataDetail
import com.lavyshyk.data.model.countries.CountryDataInfo
import com.lavyshyk.data.model.countries.CountryInfoForMap
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path


interface RESTCountryService {

    @GET("name/{name}")
    fun getInfoAboutCountry(@Path("name") nameCountry: String): Flowable<MutableList<CountryDataDetail>>

    @GET("name/{name}")
    fun geCountryListByName(@Path("name") nameCountry: String): Flowable<MutableList<CountryDataInfo>>

    @GET("all")
    fun getCountriesInfo(): Flowable<MutableList<CountryDataInfo>>

    @GET("all")
    fun getInfoAboutAllCountryForMap(): Flowable<MutableList<CountryInfoForMap>>


}
