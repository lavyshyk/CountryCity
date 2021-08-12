package com.lavyshyk.countrycity.ui.countryListFragment

import com.lavyshyk.countrycity.base.mpv.BaseMvpPresenter

class CountryListPresenter : BaseMvpPresenter<ICountryListView>() {

//    fun getCountyDataFromAPI() {
//        addDisposable(
//
//            inBackground(
//                handleProgress(
//                    retrofitService.getCountriesInfo()
//
//                    .map { it.transformToCountryDto() }
//                    .doOnNext { response ->
//                        println("DATA - запись в бд " + Thread.currentThread().name)
//                        database?.countryDao()
//                            ?.saveListCountry(response.transformEntitiesToCountry())
//                    }
//                )
//            ).subscribe({
//                getView()?.showCountryData(it)
//                println("DATA - показ во вью из API")
//            }, {
//                it.message?.let { i -> getView()?.showError(i, it) }
//            })
//        )
//
//    }
//
//    fun getCountryDataFromDataBase() {
//
//        addDisposable(
//            inBackground(
//                handleProgress(
//
//                    database?.countryDao()?.getListCountry()!!
//                    .map { it.transformEntitiesToCountryDto() }
//
//                )
//            ).subscribe({
//                println("DATA - вывод из  бд  во вью" + Thread.currentThread().name)
//                getView()?.showCountryData(it)
//            }, {
//                it.message?.let { i -> getView()?.showError(i, it) }
//            })
//        )
//
//
//    }
}