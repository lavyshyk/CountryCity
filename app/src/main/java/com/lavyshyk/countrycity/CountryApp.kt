package com.lavyshyk.countrycity

import android.app.Application
import androidx.room.Room
import com.lavyshyk.countrycity.network.RESTCountryService
import com.lavyshyk.countrycity.room.CountryDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CountryApp : Application() {


    companion object {
        private lateinit var logging: HttpLoggingInterceptor
        private lateinit var retrofit: Retrofit
        private lateinit var httpClient: OkHttpClient.Builder
        lateinit var retrofitService: RESTCountryService
        var database: CountryDatabase? = null
        var INSTANCE: CountryDatabase? = null
    }


    override fun onCreate() {
        super.onCreate()
        retrofit = getRetrofit()
        retrofitService = retrofit.create(RESTCountryService::class.java)
        database = getInstance()
    }

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
            .client(httpClient.build())
            .build()
    }


    private fun getInstance(): CountryDatabase {
        if (INSTANCE == null) {
            synchronized(CountryDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    CountryDatabase::class.java, "country-database"
                ).allowMainThreadQueries()
                    .build()
            }
        }
        return INSTANCE!!
    }


}