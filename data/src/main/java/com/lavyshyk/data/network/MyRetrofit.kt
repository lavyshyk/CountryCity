package com.lavyshyk.data.network

import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lavyshyk.data.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object MyRetrofit {

    private var logging = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BASIC
    )

    private var httpClient = OkHttpClient()
        .newBuilder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient.build())
            .build()


    private fun getRetrofitCoroutine(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(httpClient.build())
            .build()


    private fun getRetrofitFlow(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .client(httpClient.build())
            .build()


    fun createService(): RESTCountryService = getRetrofit().create(RESTCountryService::class.java)

    fun createServiceCoroutine(): CountryServiceCoroutine = getRetrofitCoroutine().create(
        CountryServiceCoroutine::class.java
    )

    fun createServiceFlow(): CountryServiceFlow =
        getRetrofitFlow().create(CountryServiceFlow::class.java)
}