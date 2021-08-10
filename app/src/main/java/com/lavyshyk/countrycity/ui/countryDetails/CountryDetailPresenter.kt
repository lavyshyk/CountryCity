package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepository

class CountryDetailPresenter(
    private val mNetworkRepository: NetworkRepository
) : BaseMvpPresenter<ICountryDetailsView>() {
    lateinit var nameCountry: String


    fun setArgumentFromView(argyment : String){
        nameCountry = argyment
    }

    fun getCountryByName(isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(
                    mNetworkRepository.getInfoAboutCountry(nameCountry), isRefresh
                )
            ).subscribe({
                getView()?.showCountryDetail(
                    it[0]
                )
            }, {
                it.message?.let { i ->
                    getView()?.showError(i, it)
                }
            })
        )

    }

}