package com.lavyshyk.countrycity.di.dagger.component

import com.lavyshyk.countrycity.base.mvi.RootFragment
import com.lavyshyk.countrycity.di.dagger.annotetions.FragmentScope
import com.lavyshyk.countrycity.di.dagger.module.FragmentModule
import dagger.Component

@FragmentScope
@Component(modules = [FragmentModule::class], dependencies = [ApplicationComponent::class])
interface FragmentComponent {

  fun inject(rootFragment: RootFragment)
}