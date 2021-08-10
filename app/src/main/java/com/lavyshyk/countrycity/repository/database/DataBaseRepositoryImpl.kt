package com.lavyshyk.countrycity.repository.database

import androidx.lifecycle.LiveData
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.CountryDatabase
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.util.transformDtoToLanguage
import com.lavyshyk.countrycity.util.transformEntitiesToCountry
import com.lavyshyk.countrycity.util.transformEntitiesToCountryDto
import io.reactivex.rxjava3.core.Flowable

class DataBaseRepositoryImpl(private val dataBase: CountryDatabase): DataBaseRepository {

    override fun setCountry(country: Country) {
        dataBase.countryDao().setCountry(country)
    }

    override fun saveListCountry(list: MutableList<CountryDto>) {
        dataBase.countryDao().saveListCountry(list.transformEntitiesToCountry())
    }

    override fun getListCountry(): Flowable<MutableList<CountryDto>>
    = dataBase.countryDao().getListCountry()!!.map { it.transformEntitiesToCountryDto() }



    override fun getListCountryLiveData(): LiveData<MutableList<Country>> {
      return  dataBase.countryDao().getListCountryLiveData()

    }

    override fun getListCountrySimple(): MutableList<CountryDto> =
        dataBase.countryDao().getListCountrySimple().transformEntitiesToCountryDto()


    override fun updateCountry(country: Country) {
        dataBase.countryDao().updateCountry(country)
    }

    override fun updateListCountry(list: MutableList<CountryDto>) {
        dataBase.countryDao().updateListCountry(list.transformEntitiesToCountry())
    }

    override fun deleteListCountry(list: List<Country>) {
        dataBase.countryDao().deleteListCountry(list)
    }

    override fun getListCountryName(): MutableList<String> = dataBase.countryDao().getListCountryName()

    override fun saveLanguage(language: LanguageDto) {
        dataBase.languageDao().saveLanguage(language.transformDtoToLanguage())
    }

    override fun getLanguageByCountry(name: String): Flowable<MutableList<String>> =
        dataBase.languageDao().getLanguageByCountry(name)

}