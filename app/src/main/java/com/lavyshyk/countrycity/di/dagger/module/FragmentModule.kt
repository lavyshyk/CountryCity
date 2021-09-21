package com.lavyshyk.countrycity.di.dagger.module

import android.content.Context
import com.lavyshyk.countrycity.base.mvi.RootFragment
import com.lavyshyk.data.ext.NewsCountryTransformer
import com.lavyshyk.data.network.NewsServiceFlow
import com.lavyshyk.data.repository.flow.NewsNetworkRepositoryOnFlowForDaggerImpl
import com.lavyshyk.domain.repository.flow.NewsNetworkRepositoryOnFlowDagger
import dagger.Module
import dagger.Provides

@Module
class FragmentModule constructor(private val fragment: RootFragment) {

    @Provides
    fun providesContext(): Context = fragment.requireContext()

    @Provides
    fun providesFragment(): RootFragment = fragment
    @Provides
    fun providesTransformer(): NewsCountryTransformer = NewsCountryTransformer()

    @Provides
    fun providesNewsNetworkRepository(api: NewsServiceFlow, transformer: NewsCountryTransformer): NewsNetworkRepositoryOnFlowDagger =
        NewsNetworkRepositoryOnFlowForDaggerImpl(api,transformer)

}