package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.ui.startFragment.StartFragment
import com.lavyshyk.countrycity.ui.startFragment.StartViewModel
import com.lavyshyk.domain.useCase.implementetion.netCase.flow.GetNewsFromApiFlowUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val startCountryModule = module {
    scope<StartFragment> {
        scoped { GetNewsFromApiFlowUseCase(get()) }
        viewModel { StartViewModel(get()) }
    }
}