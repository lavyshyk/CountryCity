package com.lavyshyk.countrycity.di

import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.ui.capitaslListOfCountries.CapitalListViewModel
import com.lavyshyk.countrycity.ui.capitaslListOfCountries.FragmentListOfCapitals
import com.lavyshyk.data.iterator.IteratorFilterByNameCapital
import com.lavyshyk.data.iterator.IteratorFilterCountyByCapitalName
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCapitalsFromApiUseCase
import com.lavyshyk.domain.useCase.implementetion.netCase.flow.GetAllCapitalsFromApiFlowUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val capitalListModel = module {
    scope<FragmentListOfCapitals> {
        scoped { GetAllCapitalsFromApiUseCase(get()) }
        scoped { GetAllCapitalsFromApiFlowUseCase(get()) }
        scoped { IteratorFilterByNameCapital(get(), get()) }
        scoped { IteratorFilterCountyByCapitalName(get(), get()) }


        viewModel { (handle: SavedStateHandle) ->
            CapitalListViewModel(
                handle,
                get(),
                get(),
                get(),
                get()
            )
        }
    }
}