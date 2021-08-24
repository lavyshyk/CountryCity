package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.ui.countryDetails.CountryDetailPresenter
import com.lavyshyk.countrycity.ui.countryDetails.CountryDetailsFragment
import com.lavyshyk.domain.useCase.implementetion.netCase.GetCountyDetailInfoFromApiUseCase
import org.koin.dsl.module

val countryDetailModule = module {
    scope<CountryDetailsFragment> {
        scoped { GetCountyDetailInfoFromApiUseCase(get()) }
        scoped { CountryDetailPresenter(get(), get()) }
    }
}