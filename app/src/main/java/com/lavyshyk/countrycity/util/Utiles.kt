package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.countrycity.dto.CountryDataDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.Country
import com.lavyshyk.countrycity.room.Language


fun MutableList<Country>.transformEntitiesToCountryDto(): MutableList<CountryDataDto> {
    var list: MutableList<CountryDataDto> = mutableListOf()
    var listOfLanguage: MutableList<LanguageDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            //val languageList = CountryApp.database?.languageDao()?.setLanguage()
            list.add(
                CountryDataDto(
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    item.area,
                    mutableListOf(),
                    item.flag,
                )
            )
        }
    }

    return list
}

fun MutableList<CountryDataDto>.transformEntitiesToCountry(): MutableList<Country> {
    val list: MutableList<Country> = mutableListOf()

    this.let { it ->
        it.forEach { item ->
            list.add(
                Country(
                    item.name.orEmpty(),
                    item.capital.orEmpty(),
                    item.region.orEmpty(),
                    item.population ?: 0L,
                    item.area ?: 0.0F,
                    item.flag.orEmpty(),
                )
            )

            item.languages.let {
                it?.forEach {
                    CountryApp.database?.languageDao()
                        ?.setLanguage(Language(it.name.orEmpty(), it.nativeName.orEmpty()))
                }
            }
        }
    }

    return list
}

fun LanguageDto.transformDtoToLanguage(): Language {
    return Language(this.name.orEmpty(), this.nativeName.orEmpty())
}

fun Language.transformLanguageToLanguageDto(): LanguageDto {
    return LanguageDto(this.name, this.nativeName)
}


