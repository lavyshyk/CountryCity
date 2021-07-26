package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter
import com.lavyshyk.countrycity.util.transformToCountryDetailDto

class CountryDetailPresenter : BaseMvpPresenter<ICountryDetailsView>() {


    fun getCountryByName(nameCountry: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(
                    retrofitService.getInfoAboutCountry(nameCountry), isRefresh
                )
            ).subscribe({
                getView()?.showCountryDetail(
                    it.transformToCountryDetailDto()[0]
                )
            }, {
                it.message?.let { i -> getView()?.showError(i,it)
                }
            })
        )

    }

}