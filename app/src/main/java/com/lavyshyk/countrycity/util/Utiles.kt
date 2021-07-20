package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.countrycity.dto.CountryDataDetailDto
import com.lavyshyk.countrycity.dto.CountryDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.model.CountryDataDetail
import com.lavyshyk.countrycity.model.CountryDataInfo
import com.lavyshyk.countrycity.model.CountryDataInfo.LanguageData
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.room.entyties.Language


fun MutableList<Country>.transformEntitiesToCountryDto(): MutableList<CountryDto> {
    val list: MutableList<CountryDto> = mutableListOf()
    val listOfLanguage: MutableList<LanguageDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            //val languageList = CountryApp.database?.languageDao()?.saveLanguage()
            list.add(
                CountryDto(
                    item.nameCountry,
                    item.capital,
                    item.region,
                    item.population,
                    item.area,
                    mutableListOf()
                )
            )
        }
    }

    return list
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
                    item.area
                )
            )

            item.languages.let {
                it.forEach { it ->
                    CountryApp.database?.languageDao()
                        ?.saveLanguage(Language(it.name, it.nativeName))
                }
            }
        }
    }

    return list
}

/*
a few functions to transform repo entities to Dto and vice-versa
 */

fun LanguageDto.transformDtoToLanguage(): LanguageData =
    LanguageData(this.name, this.nativeName)

fun LanguageData?.transformLanguageToLanguageDto(): LanguageDto {
    return LanguageDto(this?.name.orEmpty(), this?.nativeName.orEmpty())
}

fun MutableList<CountryDataInfo>.transformToCountryDto(): MutableList<CountryDto> {
    val list: MutableList<LanguageDto> = mutableListOf()
    val countriesDtoList: MutableList<CountryDto> = mutableListOf()

    this.let {
        it.forEach { item ->
            countriesDtoList.add(
                CountryDto(
                    item.name ?: "",
                    item.capital ?: "",
                    item.region ?: "",
                    item.population ?: 0L,
                    item.area ?: 0.0F,
                    list.apply {
                        item.languages?.forEach { it ->
                            it.transformLanguageToLanguageDto().also { i -> list.add(i) }
                        }
                    }
                )
            )

        }
    }
    return countriesDtoList
}

fun MutableList<CountryDto>.transformToCountry(): MutableList<CountryDataInfo> {
    val list: MutableList<LanguageData> = mutableListOf()
    val countriesList: MutableList<CountryDataInfo> = mutableListOf()

    this.let {
        it.forEach { item ->
            countriesList.add(
                CountryDataInfo(
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    item.area,
                    list.apply {
                        item.languages.forEach { it ->
                            it.transformDtoToLanguage().also { i -> list.add(i) }
                        }
                    }
                )
            )
        }
    }
    return countriesList

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
                    mutableListOf<Double>(item?.latlng?.get(0) ?: 0.0, item.latlng?.get(1) ?: 0.0),
                    item.area ?: 0.0F,
                    item?.flag ?: "",
                    list.apply {
                        item.languages?.forEach { it ->
                            it.transformLanguageToLanguageDto().also { i -> list.add(i) }
                        }
                    }
                )
            )

        }
    }
    return countriesDtoList
}




