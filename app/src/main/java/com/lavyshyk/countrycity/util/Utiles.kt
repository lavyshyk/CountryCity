package com.lavyshyk.countrycity.util

import com.lavyshyk.countrycity.data.CountryDataDto
import com.lavyshyk.countrycity.room.Country


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


