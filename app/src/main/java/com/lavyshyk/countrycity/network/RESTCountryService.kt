package com.lavyshyk.countrycity.network

import com.lavyshyk.countrycity.dto.CountryDataDto
import retrofit2.Call
import retrofit2.http.GET


interface  RESTCountryService {
    @GET("all")
    fun getCountryInfo(): Call<MutableList<CountryDataDto>>
}
