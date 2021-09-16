package com.lavyshyk.data.network

import com.lavyshyk.data.model.capitals.Capital
import com.lavyshyk.data.model.countries.CountryDataInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface CountryServiceFlow {

    @GET("all?fields=capital")
    fun getAllCapitals(): Flow<MutableList<Capital>>

    @GET("all")
    fun getCountriesInfo(): Flow<MutableList<CountryDataInfo>>

}