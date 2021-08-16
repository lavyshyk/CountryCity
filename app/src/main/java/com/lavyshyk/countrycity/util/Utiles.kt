package com.lavyshyk.countrycity.util

import android.content.Context
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.CountryInfoMapDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.model.CountryDataDetail
import com.lavyshyk.countrycity.model.CountryDataInfo
import com.lavyshyk.countrycity.model.CountryDataInfo.LanguageData
import com.lavyshyk.countrycity.model.CountryInfoForMap
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.room.entyties.Language


fun  MutableList<Country>.transformDbEntitiesToCountryDto(): MutableList<CountryDto> {
    val list: MutableList<CountryDto> = mutableListOf()
    this.let { it ->
        it.forEach { item ->
            list.add(
                CountryDto(
                    item.nameCountry,
                    item.capital,
                    item.region,
                    item.population,
                    mutableListOf(item.lat, item.lng),
                    item.area,
                    item.nativeName,
                    mutableListOf()
                )
            )
        }
    }
    return list
}

fun Pair<Country, MutableList<Language>>.transformDbEntityToCountryDto(): CountryDto {
    val listOfLanguage: MutableList<LanguageDto> = mutableListOf()
    val country = this.first
    val list = this.second
    return CountryDto(
        country.nameCountry,
        country.capital,
        country.region,
        country.population,
        mutableListOf(country.lat, country.lng),
        country.area,
        country.nativeName,
        listOfLanguage.apply {
            list.forEach { language ->
                this.add(language.transformLanguageToLanguageDto())
            }
        }
    )

}

fun MutableList<CountryDto>.transformEntitiesToCountry(): MutableList<Country> {
    val list: MutableList<Country> = mutableListOf()

    this.let { it ->
        it.forEach { item ->
            list.add(
                Country(
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    item.latlng[0],
                    item.latlng[1],
                    item.area,
                    item.nativeName,
                )
            )
        }
    }
    return list
}

fun MutableList<CountryDto>.transformEntitiesDtoToCountryDbAndLanguageDb(): Pair<MutableList<Country>, MutableList<Language>> {
    val listCountry: MutableList<Country> = mutableListOf()
    val listLanguage: MutableList<Language> = mutableListOf()
    this.let { it ->
        it.forEach { item ->
            listCountry.add(
                Country(
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    item.latlng[0],
                    item.latlng[1],
                    item.area,
                    item.nativeName
                )
            )
            item.languages.let {
                it.forEach { language -> listLanguage.add(language.transformDtoToLanguage(item.name)) }
            }
        }
    }
    return Pair(listCountry, listLanguage)
}

/*
a few functions to transform repo entities to Dto and vice-versa
 */

fun LanguageDto.transformDtoToLanguageData(): LanguageData =
    LanguageData(this.name, this.nativeName)

fun LanguageData?.transformLanguageDataToLanguageDto(): LanguageDto {
    return LanguageDto(this?.name.orEmpty(), this?.nativeName.orEmpty())
}

fun LanguageDto.transformDtoToLanguage(countryName: String): Language =
    Language(this.name, this.nativeName, countryName)

fun Language?.transformLanguageToLanguageDto(): LanguageDto {
    return LanguageDto(this?.languageName.orEmpty(), this?.nativeName.orEmpty())
}

fun MutableList<CountryDataInfo>.transformToCountryDto(): MutableList<CountryDto> {
    val listOfLanguageDto: MutableList<LanguageDto> = mutableListOf()
    val listOfCountryDto: MutableList<CountryDto> = mutableListOf()

    this.let {
        it.forEach { item ->
            listOfCountryDto.add(
                CountryDto(
                    item.name ?: "",
                    item.capital ?: "",
                    item.region ?: "",
                    item.population ?: 0L,
                    if (item.latlng.isNullOrEmpty()) {
                        mutableListOf<Double>(0.0, 0.0)
                    } else {
                        mutableListOf(item.latlng[0], item.latlng[1])
                    },
                    item.area ?: 0.0F,
                    item.nativeName ?: "",
                    listOfLanguageDto.apply {
                        item.languages?.forEach { it ->
                            it.transformLanguageDataToLanguageDto()
                                .also { i -> listOfLanguageDto.add(i) }
                        }
                    }
                )
            )
        }
    }
    return listOfCountryDto
}

fun MutableList<CountryDto>.transformToCountry(): MutableList<CountryDataInfo> {
    val listOfLanguageData: MutableList<LanguageData> = mutableListOf()
    val listOfCountryData: MutableList<CountryDataInfo> = mutableListOf()

    this.let {
        it.forEach { item ->
            listOfCountryData.add(
                CountryDataInfo(
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    mutableListOf(item.latlng[0], item.latlng[1]),
                    item.area,
                    item.nativeName,
                    listOfLanguageData.apply {
                        item.languages.forEach { it ->
                            it.transformDtoToLanguageData().also { i -> listOfLanguageData.add(i) }
                        }
                    }
                )
            )
        }
    }
    return listOfCountryData

}

fun MutableList<CountryDataDetail>.transformToCountryDetailDto(): MutableList<CountryDataDetailDto> {
    val list: MutableList<LanguageDto> = mutableListOf()
    val countriesDtoList: MutableList<CountryDataDetailDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            countriesDtoList.add(
                CountryDataDetailDto(
                    item.name ?: "",
                    item.capital ?: "",
                    item.region ?: "",
                    item.population ?: 0L,
                    if (item.latlng.isNullOrEmpty()) {
                        mutableListOf<Double>(0.0, 0.0)
                    } else {
                        mutableListOf(item.latlng[0], item.latlng[1])
                    },
                    item.area ?: 0.0F,
                    item.flag ?: "",
                    list.apply {
                        item.languages?.forEach { it ->
                            it.transformLanguageDataToLanguageDto().also { i -> list.add(i) }
                        }
                    }
                )
            )
        }
    }
    return countriesDtoList
}

fun getDescription(countryDataDetail: CountryDataDetailDto, context: Context): String {

    return "\t${countryDataDetail.name}. \n${
        context.getString(
            R.string.capital_is,
            countryDataDetail.capital
        )
    }. \n${
        context.getString(
            R.string.description_of_country,
            countryDataDetail.area.toString(),
            countryDataDetail.population.toString()
        )
    } \n${
        context.getString(
            R.string.region,
            countryDataDetail.name,
            countryDataDetail.region
        )
    }"
}


fun MutableList<CountryInfoForMap>.transformToCountryInfoMapDto(): MutableList<CountryInfoMapDto> {
    val listCountryInfoMapDto: MutableList<CountryInfoMapDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            listCountryInfoMapDto.add(
                CountryInfoMapDto(
                    item.name ?: "",
                    item.capital ?: "",
                    if (item.latlng.isNullOrEmpty()) {
                        mutableListOf<Double>(0.0, 0.0)
                    } else {
                        mutableListOf(item.latlng[0], item.latlng[1])
                    }
                )
            )
        }
    }
    return listCountryInfoMapDto
}




