package com.lavyshyk.countrycity.ui.countryListFragment

import com.lavyshyk.countrycity.base.mvp.IBaseMvpView
import com.lavyshyk.domain.dto.CountryDto

interface ICountryListView : IBaseMvpView {

    fun showCountryData(countries: MutableList<CountryDto>)
}