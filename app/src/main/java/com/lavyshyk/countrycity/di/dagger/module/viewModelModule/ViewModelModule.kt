package com.lavyshyk.countrycity.di.dagger.module.viewModelModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lavyshyk.countrycity.di.dagger.annotetions.ViewModelKey
import com.lavyshyk.countrycity.ui.startFragment.StartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.InternalCoroutinesApi

@Module
abstract class ViewModelModule {

    @InternalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: StartViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}