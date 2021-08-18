package com.lavyshyk.countrycity.ui.countryListFragment

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.COUNTRY_DTO
import com.lavyshyk.countrycity.MY_CURRENT_LOCATION
import com.lavyshyk.countrycity.SORTED_COUNTRY_DTO
import com.lavyshyk.countrycity.TIME_PAUSE_500
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.Outcome
import com.lavyshyk.countrycity.base.mvvm.executeJob
import com.lavyshyk.countrycity.repository.sharedPreference.SharedPrefRepository
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.repository.FilterRepository
import com.lavyshyk.domain.useCase.implementetion.databaseCase.GetCountiesFromDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.GetCountryNamesFromDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.SaveCountiesInDatBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.databaseCase.UpdateCountriesInDataBaseUseCase
import com.lavyshyk.domain.useCase.implementetion.netCase.GetAllCountiesFromApiUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetAllCountiesFromApiUseCase: GetAllCountiesFromApiUseCase,
    private val mGetCountiesFromDataBaseUseCase: GetCountiesFromDataBaseUseCase,
    private val mGetCountryNamesFromDataBaseUseCase: GetCountryNamesFromDataBaseUseCase,
    private val mUpdateCountriesInDataBaseUseCase: UpdateCountriesInDataBaseUseCase,
    private val mSaveCountiesInDatBaseUseCase: SaveCountiesInDatBaseUseCase,
    private val mFilterRepository: FilterRepository,
    private val mSharedPrefRepository: SharedPrefRepository
) : BaseViewModel(savedStateHandle) {


    val mCountyLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(COUNTRY_DTO)
    val mSortedCountryList =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(SORTED_COUNTRY_DTO)
    private val mCurrentLocation = savedStateHandle.getLiveData<Location>(MY_CURRENT_LOCATION)
    private val mFilterSubject = mFilterRepository.getFilterSubject()


    fun getSharedPrefString(key: String): String =
        mSharedPrefRepository.getStringFromSharedPref(key)

    fun putSharedPrefString(key: String, string: String) {
        mSharedPrefRepository.putStringSharedPref(key, string)
    }

    fun getSharedPrefBoolean(key: String): Boolean =
        mSharedPrefRepository.getBooleanFromSharedPref(key)

    fun putSharedPrefBoolean(key: String, boolean: Boolean) {
        mSharedPrefRepository.putBooleanSharedPref(key, boolean)
    }


    private fun saveDataFromApiToDB(data: MutableList<CountryDto>) {
        mCompositeDisposable.add(
            mGetCountryNamesFromDataBaseUseCase.execute()
                .flatMap { it ->
                    if (it.count() > 0) {
                        mUpdateCountriesInDataBaseUseCase.setParams(data).execute()
                            .map { action -> Pair<MutableList<String>, Unit>(it, action) }
                    } else {
                        mSaveCountiesInDatBaseUseCase.setParams(data).execute()
                            .map { action -> Pair<MutableList<String>, Unit>(it, action) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.first.count() > 0) {
                            Log.i("SUCCESS", "DataBase was updated")
                        } else {
                            Log.i("SUCCESS", "DataBase was  saved")
                        }
                    },
                    {
                        Log.e(" ERROR", "error save or update DB")
                    },
                )
        )
    }

    fun getCountryListFromDB() {
        mCompositeDisposable.add(
            executeJob(mGetCountiesFromDataBaseUseCase.execute(), mCountyLiveData)
        )
    }

    private fun getDistanceBettwenLocations(location: Location): Float {
        return (location.distanceTo(mCurrentLocation.value) / 1000)
    }

    fun putCurrentLocation(location: Location) {
        location?.let { mCurrentLocation.value = it }
    }

    fun getSortedListCountry() {
        mCompositeDisposable.add(
            executeJob(
                mFilterSubject
                    .toFlowable(BackpressureStrategy.LATEST)
                    .debounce(TIME_PAUSE_500, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .flatMap { countryFilter ->
                        mGetAllCountiesFromApiUseCase.execute().map { list ->
                            list.filter { countryDto3 ->
                                val location = Location(LocationManager.GPS_PROVIDER)
                                location.apply {
                                    location.latitude = countryDto3.latlng[0]
                                    location.longitude = countryDto3.latlng[1]
                                }
                                getDistanceBettwenLocations(location) < countryFilter.distance
                            }
                            list.filter { countryDto ->
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
                                    countryDto2
                                        .population
                                        .toFloat() in countryFilter.minPopulation..countryFilter.maxPopulation
                                }.filter { countryDto4 ->
                                    countryDto4.name.contains(countryFilter.name, true)
                                }.toMutableList()
                        }
                    }, mSortedCountryList
            )
        )
    }

    /**
     * очень длолго ((
     */
//    fun getCountriesFromApi() {
//        mCompositeDisposable.add(
//            executeJob(
//                mGetAllCountiesFromApiUseCase.execute()
//                    .flatMap { listCountry ->
//                        mGetCountryNamesFromDataBaseUseCase.execute()
//                            .map { Pair(listCountry, it) }
//                    }
//                    .flatMap {
//                        if (it.second.count() > 0) {
//                            mUpdateCountriesInDataBaseUseCase.setParams(it.first).execute()
//                        } else {
//                            mSaveCountiesInDatBaseUseCase.setParams(it.first).execute()
//                        }
////                        return@flatMap (it.first)
//                        Flowable.just(it.first)
//                    }, mCountyLiveData
//            )
//        )
//    }

    fun getCountriesFromApi() {
        mCompositeDisposable.add(
            executeJob(
                mGetAllCountiesFromApiUseCase.execute()
                    .doOnNext { saveDataFromApiToDB(it) }, mCountyLiveData
            )
        )
    }

}