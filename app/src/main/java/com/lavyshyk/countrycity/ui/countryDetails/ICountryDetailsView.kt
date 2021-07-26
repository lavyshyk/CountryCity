package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mpv.IBaseMvpView
import com.lavyshyk.countrycity.dto.CountryDataDetailDto

interface ICountryDetailsView : IBaseMvpView {

    fun showCountryDetail(country: CountryDataDetailDto)

    fun showSvgFlag(url: String)
}