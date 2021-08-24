package com.lavyshyk.countrycity.ui.mapCountry

import com.lavyshyk.countrycity.base.mvp.BaseMvpPresenter
import com.lavyshyk.domain.useCase.implementetion.netCase.GetCountriesForMapFromApiUseCase

class MapCountryPresenter(
private val mGetCountriesForMapFromApiUseCase: GetCountriesForMapFromApiUseCase) : BaseMvpPresenter<IMapCountryView>() {

    fun getAllCountryData() {
        addDisposable(
            inBackground(
                handleProgress(mGetCountriesForMapFromApiUseCase.execute())
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