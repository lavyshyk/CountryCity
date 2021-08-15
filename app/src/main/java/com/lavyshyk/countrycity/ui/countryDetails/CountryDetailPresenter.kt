package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mvp.BaseMvpPresenter
import com.lavyshyk.countrycity.dto.CountryDataDetailDto
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


    fun setArgumentFromView(argument : String){
        nameCountry = argument
    }

    fun getCountryByName(isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(
                    mNetworkRepository.getInfoAboutCountry(nameCountry), isRefresh
                )
            ).subscribe({
                val zoom = getCurrentZoomFofMap(it[0])
                getView()?.showCountryDetail(
                    it[0],zoom
                )
            }, {
                it.message?.let { i ->
                    getView()?.showError(i, it)
                }
            })
        )

    }
    private fun getCurrentZoomFofMap(country: CountryDataDetailDto):Float {
        var zoom = 1F
        when(country.area){
            in 0F .. 1000F -> { zoom = 10F }
            in 1001F..10000F -> { zoom = 8F }
            in 10001F..70000F -> { zoom = 6F }
            in 700001F..120000F -> { zoom = 5F }
            in 120001F..1600000F -> { zoom = 4F }
            in 1600001F..3000000F -> { zoom = 3F }
            in 3000001F..10000000F -> { zoom = 2F }
        }
        return zoom
    }
}