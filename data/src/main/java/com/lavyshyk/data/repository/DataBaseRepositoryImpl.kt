package com.lavyshyk.data.repository



import com.lavyshyk.data.*
import com.lavyshyk.data.database.CountryDatabase
import com.lavyshyk.data.database.room.entyties.Country
import com.lavyshyk.data.database.room.entyties.Language
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.dto.LanguageDto
import com.lavyshyk.domain.repository.DataBaseRepository
import io.reactivex.rxjava3.core.Flowable

class DataBaseRepositoryImpl(private val dataBase: CountryDatabase) :
    DataBaseRepository {

    override fun setCountry(countryDTO: CountryDto) {
        val country: Country = countryDTO.transformCountryDtoToDbEntities()
        dataBase.countryDao().setCountry(country)
    }

    override fun saveListCountry(list: MutableList<CountryDto>): Flowable<Unit> {
        val pair = list.transformEntitiesDtoToCountryDbAndLanguageDb()
        dataBase.countryDao().saveListCountry(pair.first)
        dataBase.languageDao().saveLanguage(pair.second)
        return Flowable.just(Unit)
    }

    override fun getListCountry(): Flowable<MutableList<CountryDto>> {
        return dataBase.countryDao().getListCountry().map { it.transformDbEntitiesToCountryDto() }
    }


//    override fun getListCountryLiveData(): LiveData<MutableList<Country>> =
//        dataBase.countryDao().getListCountryLiveData()

//    override fun getListCountrySimple(): MutableList<CountryDto> {
//        TODO("Not yet implemented")
//    }

    override fun updateCountry(countryDto: CountryDto) {
        val country: Country = countryDto.transformCountryDtoToDbEntities()
        dataBase.countryDao().updateCountry(country)
    }

    override fun updateListCountry(list: MutableList<CountryDto>): Flowable<Unit> {
        TODO("Not yet implemented")
    }
//
//    override fun updateListCountry(list: MutableList<CountryDto>): Flowable<Unit> {
//        val pair = list.transformEntitiesDtoToCountryDbAndLanguageDb()
////        Flowable.create({list ->
////
////                        onNext()
//        },BackpressureStrategy.LATEST)
//        dataBase.countryDao().updateListCountry(pair.first)
//        dataBase.languageDao().updateLanguage(pair.second)
//        return Flowable.just(Unit)
//    }

    override fun deleteListCountry(listDto: MutableList<CountryDto>) {
        val list = listDto.transformEntitiesToCountry()
        dataBase.countryDao().deleteListCountry(list)
    }

    override fun getListCountryName(): Flowable<MutableList<String>> =
        dataBase.countryDao().getListCountryName()

    /**
     * Language
     */

    override fun saveLanguages(
        countryName: String,
        languages: MutableList<LanguageDto>
    ): Flowable<Unit> {
        val list = mutableListOf<Language>()
        languages.forEach { list.add(it.transformDtoToLanguage(countryName)) }
        dataBase.languageDao().saveLanguage(list)
        return Flowable.just(Unit)
    }

    override fun updateLanguages(
        countryName: String,
        languages: MutableList<LanguageDto>
    ): Flowable<Unit> {
        val list = mutableListOf<Language>()
        languages.forEach { list.add(it.transformDtoToLanguage(countryName)) }
        dataBase.languageDao().updateLanguage(list)
        return Flowable.just(Unit)
    }

    override fun getLanguageByCountry(name: String): Flowable<MutableList<String>> =
        dataBase.languageDao().getLanguageByCountry(name)


}