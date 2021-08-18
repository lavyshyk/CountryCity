package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mvp.IBaseMvpView
import com.lavyshyk.domain.dto.CountryDataDetailDto

interface ICountryDetailsView : IBaseMvpView {

    fun showCountryDetail(country: CountryDataDetailDto, zoom: Float)

    fun showSvgFlag(url: String)
}