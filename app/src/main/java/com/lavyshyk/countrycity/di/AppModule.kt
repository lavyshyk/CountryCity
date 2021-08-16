package com.lavyshyk.countrycity.di

import com.lavyshyk.countrycity.network.Retrofit
import com.lavyshyk.countrycity.repository.database.DataBaseRepository
import com.lavyshyk.countrycity.repository.database.DataBaseRepositoryImpl
import com.lavyshyk.countrycity.repository.filter.FilterRepository
import com.lavyshyk.countrycity.repository.filter.FilterRepositoryImpl
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepository
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepositoryImpl
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepositoryImpl
import com.lavyshyk.countrycity.room.CountryDatabase
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { CountryDatabase.getInstance( get() ) }
    single { Retrofit.createService() }
    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { DataBaseRepositoryImpl(get()) as DataBaseRepository}
    single { FilterRepositoryImpl() as FilterRepository }
    single { SharedPrefRepositoryImpl(get()) as SharedPrefRepository }
}
