package com.lavyshyk.countrycity.repository.database

import androidx.lifecycle.LiveData
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.entyties.Country
import io.reactivex.rxjava3.core.Flowable

interface DataBaseRepository {

    /**
     * Country
     */
    fun setCountry(country: Country)

    fun saveListCountry(list: MutableList<CountryDto>)

    fun getListCountry(): Flowable<MutableList<CountryDto>>

    fun getListCountryLiveData(): LiveData<MutableList<Country>>

    fun getListCountrySimple(): MutableList<CountryDto>

    fun updateCountry(country: Country)

    fun updateListCountry(list: MutableList<CountryDto>)

    fun deleteListCountry(list: List<Country>)

    // quick query for check BD on contain
    fun getListCountryName(): MutableList<String>

    /**
     * Language
      */
    fun saveLanguage(language: LanguageDto)

    fun getLanguageByCountry(name: String): Flowable<MutableList<String>>

}