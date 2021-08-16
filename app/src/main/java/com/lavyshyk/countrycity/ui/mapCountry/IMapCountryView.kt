package com.lavyshyk.countrycity.ui.mapCountry

import com.lavyshyk.countrycity.base.mvp.IBaseMvpView
import com.lavyshyk.countrycity.dto.CountryInfoMapDto

interface IMapCountryView : IBaseMvpView {

    fun showCountryOnMap(countries: MutableList<CountryInfoMapDto>)
}