package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.ui.mapCountry.MapCountryFragment
import com.lavyshyk.countrycity.ui.mapCountry.MapCountryPresenter
import com.lavyshyk.domain.useCase.implementetion.netCase.GetCountriesForMapFromApiUseCase
import org.koin.dsl.module

val countryMapModule = module {
    scope<MapCountryFragment> {
        scoped { GetCountriesForMapFromApiUseCase(get()) }
        scoped { MapCountryPresenter(get()) }
    }
}