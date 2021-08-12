package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepository
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository

class CountryDetailPresenter(
    private val mNetworkRepository: NetworkRepository,
    private val mSharedPrefRepository: SharedPrefRepository
) : BaseMvpPresenter<ICountryDetailsView>() {
    lateinit var nameCountry: String

    fun getSharedPrefString(key: String): String =
        mSharedPrefRepository.getStringFromSharedPref(key)
    fun putSharedPrefString(key: String, string: String){
        mSharedPrefRepository.putStringSharedPref(key, string)
    }


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