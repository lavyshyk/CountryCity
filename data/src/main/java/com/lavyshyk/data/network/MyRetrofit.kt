package com.lavyshyk.data.network

import com.lavyshyk.data.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object MyRetrofit {

    private lateinit var logging: HttpLoggingInterceptor
    private lateinit var httpClient: OkHttpClient.Builder


     private fun getRetrofit(): Retrofit {
        logging = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BASIC
        )

       httpClient = OkHttpClient()
            .newBuilder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun createService(): RESTCountryService = getRetrofit().create(RESTCountryService::class.java)
}