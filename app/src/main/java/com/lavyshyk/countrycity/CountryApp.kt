package com.lavyshyk.countrycity

import android.app.Application
import com.lavyshyk.countrycity.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CountryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CountryApp)
            modules(
                appModule,
                countryListModule,
                countryDetailModule,
                countryMapModule,
                capitalListModule,
                startCountryModule
            )
        }
    }
}