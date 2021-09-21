package com.lavyshyk.countrycity.ui.countryDetails

import com.lavyshyk.countrycity.base.mvp.BaseMvpPresenter
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.countrycity.util.getCurrentZoomFofMap
import com.lavyshyk.domain.useCase.implementetion.netCase.GetCountyDetailInfoFromApiUseCase

class CountryDetailPresenter(
    private val mGetCountyDetailInfoFromApiUseCase: GetCountyDetailInfoFromApiUseCase,
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
                   mGetCountyDetailInfoFromApiUseCase.setParams(nameCountry).execute(), isRefresh
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
}