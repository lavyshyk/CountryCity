package com.lavyshyk.countrycity.ui.mapCountry

import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepository

class MapCountryPresenter(private val mNetworkRepository: NetworkRepository) : BaseMvpPresenter<IMapCountryView>() {

    fun getAllCountryData() {
        addDisposable(
            inBackground(
                handleProgress(mNetworkRepository.getInfoAboutAllCountryForMap())

            ).subscribe({
                getView()?.showCountryOnMap( it )
            }, {
                it.message?.let { i ->
                    getView()?.showError(i, it)
                }
            })
        )
    }
}