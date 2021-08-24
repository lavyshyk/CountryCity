package com.lavyshyk.countrycity.di

import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.ui.capitaslListOfCountries.CapitalListViewModel
import com.lavyshyk.countrycity.ui.capitaslListOfCountries.FragmentListOfCapitals
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCapitalsFromApiUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val capitalListModel = module {
    scope<FragmentListOfCapitals> {
        scoped { GetAllCapitalsFromApiUseCase(get()) }

        viewModel { (handle: SavedStateHandle) -> CapitalListViewModel(handle, get()) }
    }
}