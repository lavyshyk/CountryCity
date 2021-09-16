package com.lavyshyk.domain.repository


import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.dto.country.LanguageDto
import io.reactivex.rxjava3.core.Flowable

interface DataBaseRepository {

    /**
     * Country
     */
    fun setCountry(country: CountryDto)

    fun saveListCountry(list: MutableList<CountryDto>): Flowable<Unit>

    fun getListCountry(): Flowable<MutableList<CountryDto>>

   // fun getListCountryLiveData(): LiveData<MutableList<Country>>

   // fun getListCountrySimple(): MutableList<CountryDto>

    fun updateCountry(country: CountryDto)

    fun updateListCountry(list: MutableList<CountryDto>): Flowable<Unit>

   fun deleteListCountry(list: MutableList<CountryDto>)

    // quick query for check BD on contain
    fun getListCountryName(): Flowable<MutableList<String>>

    /**
     * Language
      */
    fun saveLanguages(countryName:String,languages: MutableList<LanguageDto>) : Flowable<Unit>

    fun updateLanguages(countryName:String,languages: MutableList<LanguageDto>): Flowable<Unit>

    fun getLanguageByCountry(name: String): Flowable<MutableList<String>>

   // fun getListLanguageByCountry(name: String): Flowable<MutableList<LanguageDto>>


}