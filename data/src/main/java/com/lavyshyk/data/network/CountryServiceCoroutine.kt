package com.lavyshyk.data.network

import com.lavyshyk.data.model.capitals.Capital
import retrofit2.http.GET

interface CountryServiceCoroutine {

    @GET("all?fields=capital")
    suspend fun getAllCapitals(): MutableList<Capital>
}