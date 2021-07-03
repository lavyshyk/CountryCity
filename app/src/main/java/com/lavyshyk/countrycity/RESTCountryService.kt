package com.lavyshyk.countrycity

import com.lavyshyk.countrycity.data.CountryData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RESTCountryService {

    @GET("all")
    fun getCountryInfo(): Call<MutableList<CountryData>>

}
const val BASE_URL = "https://restcountries.eu/rest/v2/"

var logging:HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

var httpClient: OkHttpClient.Builder =  OkHttpClient().newBuilder().addInterceptor(logging)

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient.build())
    .build()

object CountryApi {
   val retrofitService: RESTCountryService  by lazy { retrofit.create(RESTCountryService::class.java) }

}
