package com.lavyshyk.countrycity.di

import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.ui.countryListFragment.CountriesListFragment
import com.lavyshyk.countrycity.ui.countryListFragment.CountryListViewModel
import com.lavyshyk.domain.useCase.implementetion.databaseCase.GetCountiesFromDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.GetCountryNamesFromDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.SaveCountiesInDatBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.UpdateCountriesInDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCountiesFromApiUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {
    scope<CountriesListFragment> {

        scoped { GetAllCountiesFromApiUseCase(get()) }
        scoped { GetCountryNamesFromDataBaseUseCase(get()) }
        scoped { SaveCountiesInDatBaseUseCase(get()) }
        scoped { UpdateCountriesInDataBaseUseCase(get()) }
        scoped { GetCountiesFromDataBaseUseCase(get()) }

        viewModel { (handle: SavedStateHandle) ->
            CountryListViewModel(
                handle, get(), get(), get(), get(), get(), get(), get()
            )
        }

    }
}