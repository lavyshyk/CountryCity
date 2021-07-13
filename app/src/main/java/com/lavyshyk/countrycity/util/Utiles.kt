package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.CountryApp
import com.lavyshyk.countrycity.dto.CountryDataDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.Country
import com.lavyshyk.countrycity.room.Language


fun MutableList<Country>. transformEntitiesToCountryDto(): MutableList<CountryDataDto> {
    var list: MutableList<CountryDataDto> = mutableListOf()
    var listOfLanguage: MutableList<LanguageDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            //val languageList = CountryApp.database?.languageDao()?.setLanguage()
            list.add(
                CountryDataDto(
//                    item.idCountry,
                    item.name,
                    item.capital,
                    item.region,
                    item.population,
                    item.area,
                    listOf(),
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
    //
    this.let {
        it.forEach { item ->
            list.add(
                Country(
                    item.name.orEmpty(),
                    item.capital.orEmpty(),
                    item.region.orEmpty(),
                    item.population ?: 0L,
//                    LatLng(item.latlng[0], item.latlng[1]),
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
            item.latlng?.let { CountryApp.database?.latLngDao()?.setLatLang(com.lavyshyk.countrycity.room.LatLng(it[0],it[1]))
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


