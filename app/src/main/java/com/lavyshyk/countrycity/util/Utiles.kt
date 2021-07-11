package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.dto.CountryDataDto
import com.lavyshyk.countrycity.dto.LanguageDto
import com.lavyshyk.countrycity.room.Country
import com.lavyshyk.countrycity.room.Language


fun MutableList<Country>.transformEntitiesToCountryDto(): MutableList<CountryDataDto> {
    val list: MutableList<CountryDataDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            //val languageList = CountryApp.database.languageDao().getLanguageByCountry(item.name)
            list.add(
                CountryDataDto(
//                    item.idCountry,
                    item.name,
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

fun MutableList<CountryDataDto>.transformEntitiesToCountry(): MutableList<Country> {
    val list: MutableList<Country> = mutableListOf()
    this.let {
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
        }
    }

    return list
}

fun LanguageDto.transformDtoToLanguage(): Language {
    return Language(this.name, this.nativeName)
}


