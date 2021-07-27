package com.lavyshyk.countrycity.ui.mapCountry

import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter
import com.lavyshyk.countrycity.util.transformToCountryInfoMapDto

class MapCountryPresenter : BaseMvpPresenter<IMapCountryView>() {

    fun getAllCountryData() {
        addDisposable(
            inBackground(
                retrofitService.getInfoAboutAllCountryForMap()
            ).subscribe({
                getView()?.showCountryOnMap( it.transformToCountryInfoMapDto() )
            }, {
                it.message?.let { i ->
                    getView()?.showError(i, it)
                }
            })
        )
    }
}