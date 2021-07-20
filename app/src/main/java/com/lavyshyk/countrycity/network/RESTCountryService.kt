package com.lavyshyk.countrycity.network

import com.lavyshyk.countrycity.model.CountryDataDetail
import com.lavyshyk.countrycity.model.CountryDataInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RESTCountryService {

    @GET("name/{name}")
    fun getInfoAboutCountry(@Path("name") nameCountry: String): Call<MutableList<CountryDataDetail>>

    @GET("all")
    fun getCountriesInfo(): Call<MutableList<CountryDataInfo>>


}
