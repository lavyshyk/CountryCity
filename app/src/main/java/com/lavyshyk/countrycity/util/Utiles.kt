package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.data.CountryDataDto
import com.lavyshyk.countrycity.data.LanguageDto
import com.lavyshyk.countrycity.room.Country
import com.lavyshyk.countrycity.room.Language


fun MutableList<Country>.transformEntitiesToCountryDto(): MutableList<CountryDataDto> {
    val list: MutableList<CountryDataDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            list.add(
                CountryDataDto(
//                    item.idCountry,
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


