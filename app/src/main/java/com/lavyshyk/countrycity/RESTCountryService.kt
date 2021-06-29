package com.lavyshyk.countrycity

import com.lavyshyk.countrycity.data.CountryData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RESTCountryService {

    @GET("all")
    fun getCountryInfo(): Call<MutableList<CountryData>>

}
const val BASE_URL = "https://restcountries.eu/rest/v2/"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object CountryApi {
   val retrofitService: RESTCountryService = retrofit.create(RESTCountryService::class.java)

}
