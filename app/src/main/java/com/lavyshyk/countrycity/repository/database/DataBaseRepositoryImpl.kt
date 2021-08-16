package com.lavyshyk.countrycity.repository.database

import androidx.lifecycle.LiveData
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.CountryDatabase
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.room.entyties.Language
import com.lavyshyk.countrycity.util.transformDbEntityToCountryDto
import com.lavyshyk.countrycity.util.transformDtoToLanguage
import com.lavyshyk.countrycity.util.transformEntitiesDtoToCountryDbAndLanguageDb
import io.reactivex.rxjava3.core.Flowable

class DataBaseRepositoryImpl(private val dataBase: CountryDatabase) : DataBaseRepository {

    override fun setCountry(country: Country) {
        dataBase.countryDao().setCountry(country)
    }

    override fun saveListCountry(list: MutableList<CountryDto>) {
        val pair = list.transformEntitiesDtoToCountryDbAndLanguageDb()
        dataBase.countryDao().saveListCountry(pair.first)
        dataBase.languageDao().saveLanguage(pair.second)
    }

    override fun getListCountry(): Flowable<MutableList<CountryDto>> {
        return dataBase.countryDao().getListCountry()?.map {
            it.forEach { country ->
                (dataBase.languageDao().getListLanguageByCountry(country.nameCountry)
                    .map { languages ->
                        Pair(country, languages).transformDbEntityToCountryDto()
                    }).toList().toFlowable()
            }
        } as Flowable<MutableList<CountryDto>>

    }

    override fun getListLanguageByCountry(name: String): Flowable<MutableList<Language>> =
        dataBase.languageDao().getListLanguageByCountry(name)

    override fun getListCountryLiveData(): LiveData<MutableList<Country>> =
        dataBase.countryDao().getListCountryLiveData()

//    override fun getListCountrySimple(): MutableList<CountryDto> {
//        TODO("Not yet implemented")
//    }

    override fun updateCountry(country: Country) {
        dataBase.countryDao().updateCountry(country)
    }

    override fun updateListCountry(list: MutableList<CountryDto>) {
        val pair = list.transformEntitiesDtoToCountryDbAndLanguageDb()
        dataBase.countryDao().updateListCountry(pair.first)
        dataBase.languageDao().updateLanguage(pair.second)
    }

    override fun deleteListCountry(list: List<Country>) {
        dataBase.countryDao().deleteListCountry(list)
    }

    override fun getListCountryName(): Flowable<MutableList<String>> =
        dataBase.countryDao().getListCountryName()

    /**
     * Language
     */

    override fun saveLanguages(countryName: String, languages: MutableList<LanguageDto>) {
        val list = mutableListOf<Language>()
        languages.forEach { list.add(it.transformDtoToLanguage(countryName)) }
        dataBase.languageDao().saveLanguage(list)
    }

    override fun updateLanguages(countryName: String, languages: MutableList<LanguageDto>) {
        val list = mutableListOf<Language>()
        languages.forEach { list.add(it.transformDtoToLanguage(countryName)) }
        dataBase.languageDao().updateLanguage(list)
    }

    override fun getLanguageByCountry(name: String): Flowable<MutableList<String>> =
        dataBase.languageDao().getLanguageByCountry(name)

}