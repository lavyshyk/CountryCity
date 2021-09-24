package com.lavyshyk.countrycity.ui.countryListFragment

import android.location.Location
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.lavyshyk.countrycity.*
import com.lavyshyk.countrycity.base.mvvm.BaseViewModel
import com.lavyshyk.countrycity.base.mvvm.addToComposite
import com.lavyshyk.countrycity.base.mvvm.executeJob
import com.lavyshyk.countrycity.base.mvvm.executeJobWithHandleProgress
import com.lavyshyk.countrycity.util.convertToCorrectArea
import com.lavyshyk.countrycity.util.getLocationCountry
import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.outcome.Outcome
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

class CountryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetAllCountiesFromApiUseCase: GetAllCountiesFromApiUseCase,
    private val mGetCountiesFromDataBaseUseCase: GetCountiesFromDataBaseUseCase,
    private val mGetCountryNamesFromDataBaseUseCase: GetCountryNamesFromDataBaseUseCase,
    private val mUpdateCountriesInDataBaseUseCase: UpdateCountriesInDataBaseUseCase,
    private val mSaveCountiesInDatBaseUseCase: SaveCountiesInDatBaseUseCase,
    private val mFilterRepository: FilterRepository,
) : BaseViewModel(savedStateHandle) {

    val mCountyLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(COUNTRY_DTO)
    val mSortedCountryList =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDto>>>(SORTED_COUNTRY_DTO)
    private val mCurrentLocation = savedStateHandle.getLiveData<Location>(MY_CURRENT_LOCATION)
    private val mFilterSubject = mFilterRepository.getFilterSubject()


    fun saveDataFromApiToDB(data: MutableList<CountryDto>) {
        (mGetCountryNamesFromDataBaseUseCase.execute()
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
                ).addToComposite(mCompositeDisposable)
    }

    fun getCountryListFromDB() {
        mCompositeDisposable.add(
            executeJob(
                mGetCountiesFromDataBaseUseCase.execute()
                    .doOnNext {
                        it.forEach { countryDto ->
                            countryDto.distance = (getDistanceBettwenLocations(countryDto))
                        }
                    }
                    .doOnError {
                        mGetCountiesFromDataBaseUseCase.execute()
                    }, mCountyLiveData
            )
        )
    }

    private fun getDistanceBettwenLocations(countryDto: CountryDto): Float {
        val location = countryDto.getLocationCountry()
        return mCurrentLocation.value?.let { (location.distanceTo(it) / TO_K_KM) } ?: 0f
    }

    fun putCurrentLocation(location: Location?) {
        location.let { mCurrentLocation.value = it }
    }

    fun getSortedListCountry() {
        (executeJob(
            mFilterSubject
                .toFlowable(BackpressureStrategy.LATEST)
                .debounce(TIME_PAUSE_500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap { countryFilter ->
                    mGetAllCountiesFromApiUseCase.execute().map { list ->
                        list.filter { countryDto3 ->
                            getDistanceBettwenLocations(countryDto3) < countryFilter.distance / 1000
                        }
                            .filter { countryDto ->
                                countryDto.convertToCorrectArea()
                                    .toFloat() in countryFilter.minArea..countryFilter.maxArea
                            }
                            .filter { countryDto2 ->
                                countryDto2
                                    .population
                                    .toFloat() in countryFilter.minPopulation..countryFilter.maxPopulation
                            }.filter { countryDto4 ->
                                countryDto4.name.contains(countryFilter.name, true)
                            }.toMutableList()
                    }.doOnNext {
                        it.forEach { countryDto ->
                            countryDto.distance =
                                (getDistanceBettwenLocations(countryDto))
                        }
                    }
                }, mSortedCountryList
        )).addToComposite(mCompositeDisposable)
    }

    fun getCountriesFromApi(isRefresh: Boolean = false) {
        (executeJobWithHandleProgress(
            mGetAllCountiesFromApiUseCase.execute()
                .doOnNext {
                    it.forEach { countryDto ->
                        countryDto.distance = (getDistanceBettwenLocations(countryDto))
                    }
                }, mCountyLiveData, isRefresh
        )).addToComposite(mCompositeDisposable)
    }
}


