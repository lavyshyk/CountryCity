package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepositoryImpl
import com.lavyshyk.data.database.CountryDatabase


import com.lavyshyk.data.network.MyRetrofit
import com.lavyshyk.data.repository.DataBaseRepositoryImpl
import com.lavyshyk.data.repository.FilterRepositoryImpl
import com.lavyshyk.data.repository.NetworkRepositoryImpl
import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.repository.FilterRepository
import com.lavyshyk.domain.repository.NetworkRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { CountryDatabase.getInstance( get() ) }
    single { MyRetrofit.createService() }
    single(named("coroutine")) {MyRetrofit.createServiceCoroutine()  }
    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { DataBaseRepositoryImpl(get()) as DataBaseRepository }
    single { FilterRepositoryImpl() as FilterRepository }
    single { SharedPrefRepositoryImpl(get()) as SharedPrefRepository }
}
