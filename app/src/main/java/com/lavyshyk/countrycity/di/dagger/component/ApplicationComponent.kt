package com.lavyshyk.countrycity.di.dagger.component

import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.countrycity.di.dagger.module.ApplicationModule
import com.lavyshyk.countrycity.di.dagger.module.NetworkModule
import com.lavyshyk.countrycity.di.dagger.module.viewModelModule.DaggerViewModelFactory
import com.lavyshyk.countrycity.di.dagger.module.viewModelModule.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, NetworkModule::class, ViewModelModule::class])
@Singleton
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(app: CountryApp): Builder
    }

    fun provideDaggerViewModelFactory(): DaggerViewModelFactory
}