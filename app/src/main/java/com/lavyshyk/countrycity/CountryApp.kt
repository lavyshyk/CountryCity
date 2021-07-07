package com.lavyshyk.countrycity

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lavyshyk.countrycity.room.CountryDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryApp : Application() {


    companion object {
        private lateinit var logging: HttpLoggingInterceptor
        private lateinit var retrofit: Retrofit
        private lateinit var httpClient: OkHttpClient.Builder
        const val BASE_URL = "https://restcountries.eu/rest/v2/"
        lateinit var retrofitService: RESTCountryService
        private var database: CountryDatabase? = null
    }


    override fun onCreate() {
        super.onCreate()
        retrofit = getRetrofit()
        retrofitService = retrofit.create(RESTCountryService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        logging = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BASIC
        )

        httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    private fun getInstance(context: Context): CountryDatabase? {
        if (database == null) {
            synchronized(CountryDatabase::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CountryDatabase::class.java, "country-database"
                )
                    .allowMainThreadQueries()
                    .build().also { database = it }
            }
        }
        return database!!
    }
}