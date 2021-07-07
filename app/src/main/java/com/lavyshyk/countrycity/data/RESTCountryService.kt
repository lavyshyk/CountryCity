package com.lavyshyk.countrycity.data

import retrofit2.Call
import retrofit2.http.GET


interface  RESTCountryService {
    @GET("all")
    fun getCountryInfo(): Call<MutableList<CountryDataDto>>
}
