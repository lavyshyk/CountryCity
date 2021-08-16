package com.lavyshyk.countrycity.ui.countryListFragment

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.CountryApp.Companion.database
import com.lavyshyk.countrycity.CountryApp.Companion.retrofitService
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.countrycity.base.mvvm.executeJob
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.util.transformToCountryDto
import io.reactivex.rxjava3.core.Flowable
import kotlin.math.pow

class CountryListViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    val mCountyLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(COUNTRY_DTO)
    val mSearchQuery =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(SEARCHED_LIST_COUNTRIES)
    val mMyFilter = savedStateHandle.getLiveData<MyFilter>(MY_FILTER_LIVE_DATA_KEY)
    val mSortedCountryList =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>?>(SORTED_COUNTRY_DTO)
    val mCurrentLocation = savedStateHandle.getLiveData<Location>(MY_CURRENT_LOCATION)

    val mDataBase = database?.countryDao()?.getListCountryLiveData()


    private fun getDistanceBettwenLocations(location: Location): Float {
        return (location.distanceTo(mCurrentLocation.value) / 1000)

    }

    fun putCurrentLocation(location: Location) {
        mCurrentLocation.value = location
    }

    fun putMyFilterLiveData(myFilter: MyFilter) {
        mMyFilter.value = myFilter
    }


    fun getSortedListCountry(myFilter: MyFilter) {
        mCompositeDisposable.add(
            executeJob(
                retrofitService.getCountriesInfo()
                    .map { counties -> counties.transformToCountryDto() }
                    .flatMap { list ->
                        Flowable.fromIterable(list)
                            .filter { countryDto3 ->
                                val location = Location(LocationManager.GPS_PROVIDER)
                                location.apply {
                                    location.latitude = countryDto3.latlng[0]
                                    location.longitude = countryDto3.latlng[1]
                                }
                                getDistanceBettwenLocations(location) < myFilter.distance
                            }
                            .filter { countryDto ->
                                val mArea = countryDto.area.toString()
                                if (mArea.contains("E")){
                                    val (a,b) = mArea.split("E")
                                   val country = a.toFloat() * 10F.pow(b.toInt())
                                    country in myFilter.lArea..myFilter.rArea
                                }else{
                                    countryDto.area in myFilter.lArea..myFilter.rArea
                                }

                            }
                            .filter { countryDto2 ->
                                countryDto2.population.toFloat() in myFilter.lPopulation..myFilter.rPopulation
                            }


                    }.toList().toFlowable(), mSortedCountryList
            )
        )
    }


    fun getCountriesInfoApi() {
        mCompositeDisposable.add(
            executeJob(
                retrofitService.getCountriesInfo()
                    .map { it.transformToCountryDto() }, mCountyLiveData
            )
        )
    }


    fun getCountryListSearchByName(name: String) {
        mCompositeDisposable.add(
            executeJob(
                retrofitService.geCountryListByName(name)
                    .map { it.transformToCountryDto() }, mSortedCountryList
            )
        )
    }
}