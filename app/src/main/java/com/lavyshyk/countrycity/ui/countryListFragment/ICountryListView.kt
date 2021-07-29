package com.lavyshyk.countrycity.ui.countryListFragment

import com.lavyshyk.countrycity.base.mpv.IBaseMvpView
import com.lavyshyk.countrycity.dto.CountryDto

interface ICountryListView : IBaseMvpView {

    fun showCountryData(countries: MutableList<CountryDto>)
}