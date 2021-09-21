package com.lavyshyk.countrycity.di.dagger.module

import android.content.Context
import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.data.ext.NewsCountryTransformer
import com.lavyshyk.data.network.NewsServiceFlow
import com.lavyshyk.data.repository.flow.NewsNetworkRepositoryOnFlowForDaggerImpl
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {
    @Provides
    fun provideApplicationContext(application: CountryApp): Context = application.applicationContext

    @Provides
    fun providesTransformer(): NewsCountryTransformer = NewsCountryTransformer()

    @Provides
    fun providesNewsNetworkRepositoryAccess(api: NewsServiceFlow, transformer: NewsCountryTransformer): NewsNetworkRepositoryOnFlowDagger =
        NewsNetworkRepositoryOnFlowForDaggerImpl(api,transformer)
}