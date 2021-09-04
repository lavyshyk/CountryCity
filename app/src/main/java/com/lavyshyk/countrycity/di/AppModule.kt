package com.lavyshyk.countrycity.di


import com.lavyshyk.countrycity.DISPATCHER_IO
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepositoryImpl
import com.lavyshyk.data.database.CountryDatabase
import com.lavyshyk.data.ext.ListCapitalTransformer
import com.lavyshyk.data.ext.ListCountryTransformer
import com.lavyshyk.data.model.Capital
import com.lavyshyk.data.model.CountryDataInfo
import com.lavyshyk.data.network.MyRetrofit
import com.lavyshyk.data.repository.DataBaseRepositoryImpl
import com.lavyshyk.data.repository.FilterRepositoryImpl
import com.lavyshyk.data.repository.NetworkRepositoryImpl
import com.lavyshyk.data.repository.NetworkRepositoryOnCoroutineImpl
import com.lavyshyk.data.repository.flow.FilterFlowRepImpl
import com.lavyshyk.data.repository.flow.NetworkFlowCapitalRepositoryImpl
import com.lavyshyk.data.repository.flow.NetworkFlowCountryRepositoryImpl
import com.lavyshyk.domain.dto.CapitalDto
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.outcome.Transformer
import com.lavyshyk.domain.repository.DataBaseRepository
import com.lavyshyk.domain.repository.FilterRepository
import com.lavyshyk.domain.repository.NetworkRepository
import com.lavyshyk.domain.repository.NetworkRepositoryOnCoroutine
import com.lavyshyk.domain.repository.flow.FilterFlowRep
import com.lavyshyk.domain.repository.flow.NetworkFlowCapitalRepository
import com.lavyshyk.domain.repository.flow.NetworkFlowCountryRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {

    //Model level
    single { CountryDatabase.getInstance(get()) }
    single { MyRetrofit.createService() }
    single { MyRetrofit.createServiceCoroutine() }
    single { MyRetrofit.createServiceFlow() }
    single(named(DISPATCHER_IO)) { Dispatchers.IO }

    //Data level

    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { DataBaseRepositoryImpl(get()) as DataBaseRepository }
    single { FilterRepositoryImpl() as FilterRepository }
    single<FilterFlowRep> { FilterFlowRepImpl() }
    single { SharedPrefRepositoryImpl(get()) as SharedPrefRepository }
    single<NetworkRepositoryOnCoroutine> { NetworkRepositoryOnCoroutineImpl(get()) }

    single<Transformer<MutableList<Capital>, MutableList<CapitalDto>>>(named("capital")) { ListCapitalTransformer() }
    single<Transformer<MutableList<CountryDataInfo>, MutableList<CountryDto>>>(named("country")) { ListCountryTransformer() }
    single<NetworkFlowCapitalRepository> {
        NetworkFlowCapitalRepositoryImpl(
            get(),
            //get(named("country")),  почему то меняет местами модели страны и столицы?????????
            get(named("capital")),
            get(named(DISPATCHER_IO))
        )
    }
    single<NetworkFlowCountryRepository> {
        NetworkFlowCountryRepositoryImpl(
            get(),
            get(named("country")),
            get(named(DISPATCHER_IO))
        )
    }
}
