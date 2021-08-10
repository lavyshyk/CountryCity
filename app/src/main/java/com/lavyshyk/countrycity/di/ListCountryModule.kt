package com.lavyshyk.countrycity.di

import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.ui.countryListFragment.CountriesListFragment
import com.lavyshyk.countrycity.ui.countryListFragment.CountryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {
    scope<CountriesListFragment> {

        viewModel { (handle: SavedStateHandle) -> CountryListViewModel(handle, get(), get(), get(),get()) }

    }
}