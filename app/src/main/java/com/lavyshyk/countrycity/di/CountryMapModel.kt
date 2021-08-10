package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.ui.mapCountry.MapCountryFragment
import com.lavyshyk.countrycity.ui.mapCountry.MapCountryPresenter
import org.koin.dsl.module

val countryMapModel = module {
    scope<MapCountryFragment> {
        scoped { MapCountryPresenter(get()) }
    }
}