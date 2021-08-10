package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.ui.countryDetails.CountryDetailPresenter
import com.lavyshyk.countrycity.ui.countryDetails.CountryDetailsFragment
import org.koin.dsl.module

val countryDetailModule = module {
    scope<CountryDetailsFragment> {
        scoped { CountryDetailPresenter(get()) }
    }
}