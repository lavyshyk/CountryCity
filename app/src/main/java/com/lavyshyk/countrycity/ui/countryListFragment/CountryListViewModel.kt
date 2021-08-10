package com.lavyshyk.countrycity.ui.countryListFragment

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.countrycity.base.mvvm.executeJob
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.repository.database.DataBaseRepository
import com.lavyshyk.countrycity.repository.filter.CountryFilter
import com.lavyshyk.countrycity.repository.filter.FilterRepository
import com.lavyshyk.countrycity.repository.networkRepository.NetworkRepository
import com.lavyshyk.countrycity.room.CountryDatabase
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlin.math.pow

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkRepository: NetworkRepository,
    mCountryDatabase: CountryDatabase,
    private val mDataBaseRepository: DataBaseRepository,
    private val mFilterRepository: FilterRepository

) : BaseViewModel(savedStateHandle) {

    val mCountyLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(COUNTRY_DTO)
    val mSearchQuery =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(SEARCHED_LIST_COUNTRIES)
    val mMyFilter = savedStateHandle.getLiveData<Outcome<CountryFilter>>(MY_FILTER_LIVE_DATA_KEY)
    val mSortedCountryList =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>?>(SORTED_COUNTRY_DTO)
    val mCurrentLocation = savedStateHandle.getLiveData<Location>(MY_CURRENT_LOCATION)

    val mDataBase = mDataBaseRepository.getListCountryLiveData()


    private fun getDistanceBettwenLocations(location: Location): Float {
        return (location.distanceTo(mCurrentLocation.value) / 1000)

    }

    fun putCurrentLocation(location: Location) {
        mCurrentLocation.value = location
    }

//    fun putMyFilterLiveData(countryFilter: CountryFilter) {
//        mMyFilter.value = countryFilter
//    }


    fun getSortedListCountry(countryFilter: CountryFilter) {
        mCompositeDisposable.add(
            executeJob(
                mNetworkRepository.getCountriesInfo()
                    .flatMap { list ->
                        Flowable.fromIterable(list)
                            .filter { countryDto4 ->
                                countryDto4.name.contains(countryFilter.name)
                            }
                            .filter { countryDto3 ->
                                val location = Location(LocationManager.GPS_PROVIDER)
                                location.apply {
                                    location.latitude = countryDto3.latlng[0]
                                    location.longitude = countryDto3.latlng[1]
                                }
                                getDistanceBettwenLocations(location) < countryFilter.distance
                            }
                            .filter { countryDto ->
                                val mArea = countryDto.area.toString()
                                if (mArea.contains("E")) {
                                    val (a, b) = mArea.split("E")
                                    val country = a.toFloat() * 10F.pow(b.toInt())
                                    country in countryFilter.minArea..countryFilter.maxArea
                                } else {
                                    countryDto.area in countryFilter.minArea..countryFilter.maxArea
                                }
                            }
                            .filter { countryDto2 ->
                                countryDto2.population.toFloat() in countryFilter.minPopulation..countryFilter.maxPopulation
                            }

                    }.toList().toFlowable(), mSortedCountryList
            )
        )
    }


    fun getCountriesInfoApi() {
        mCompositeDisposable.add(
            executeJob(
                mNetworkRepository.getCountriesInfo(), mCountyLiveData
            )
        )
    }


    fun getCountryListSearchByName(name: String) {
        mCompositeDisposable.add(
            executeJob(
                mNetworkRepository.geCountryListByName(name), mSortedCountryList
            )
        )
    }

    fun getCountryFilter(behaviorSubject: BehaviorSubject<CountryFilter>) {
        executeJob(behaviorSubject.toFlowable(BackpressureStrategy.LATEST), mMyFilter)
    }
}