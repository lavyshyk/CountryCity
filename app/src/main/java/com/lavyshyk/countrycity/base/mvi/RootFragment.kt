package com.lavyshyk.countrycity.base.mvi

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.di.dagger.component.DaggerFragmentComponent
import com.lavyshyk.countrycity.di.dagger.component.FragmentComponent
import com.lavyshyk.countrycity.di.dagger.module.FragmentModule
import com.lavyshyk.countrycity.di.dagger.module.viewModelModule.DaggerViewModelFactory
import com.lavyshyk.data.ext.NewsCountryTransformer
import javax.inject.Inject

open class RootFragment : Fragment(R.layout.fragment_start) {


    private val fragmentComponent: FragmentComponent by lazy {
        DaggerFragmentComponent.builder().fragmentModule(FragmentModule(this))
            .applicationComponent(CountryApp.appComponents).build()
    }

    @Inject
    lateinit var newsCountryTransformer: NewsCountryTransformer

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

}