package com.lavyshyk.countrycity

import android.app.Application
import com.lavyshyk.countrycity.di.appModule
import com.lavyshyk.countrycity.di.countryDetailModule
import com.lavyshyk.countrycity.di.countryListModule
import com.lavyshyk.countrycity.di.countryMapModel
import com.lavyshyk.countrycity.network.RESTCountryService
import com.lavyshyk.countrycity.room.CountryDatabase
import com.lavyshyk.countrycity.room.CountryDatabase.Companion.getInstance
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
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

    }


    override fun onCreate() {
        super.onCreate()
        retrofit = getRetrofit()
        retrofitService = retrofit.create(RESTCountryService::class.java)
        database = getInstance(this)

        startKoin {
            androidLogger()
            androidContext(this@CountryApp)
            modules(
                appModule,
                countryListModule,
                countryDetailModule,
                countryMapModel
            )
        }
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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }
}