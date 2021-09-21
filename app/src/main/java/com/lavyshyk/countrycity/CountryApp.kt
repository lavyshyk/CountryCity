package com.lavyshyk.countrycity

import android.app.Application
import com.lavyshyk.countrycity.di.*
import com.lavyshyk.countrycity.di.dagger.component.ApplicationComponent
import com.lavyshyk.countrycity.di.dagger.component.DaggerApplicationComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CountryApp : Application() {

    companion object {
        lateinit var appComponents: ApplicationComponent
    }

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
               // startCountryModule
            )
        }
        appComponents = initDI()
    }
    private fun initDI(): ApplicationComponent =
        DaggerApplicationComponent.builder().application(this).build()
}