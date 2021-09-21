package com.lavyshyk.data

import com.lavyshyk.data.database.room.entyties.Country
import com.lavyshyk.data.database.room.entyties.Language
import com.lavyshyk.data.model.capitals.Capital
import com.lavyshyk.data.model.countries.CountryDataDetail
import com.lavyshyk.data.model.countries.CountryDataInfo
import com.lavyshyk.data.model.countries.CountryInfoForMap
import com.lavyshyk.domain.dto.capital.CapitalDto
import com.lavyshyk.domain.dto.country.CountryDataDetailDto
import com.lavyshyk.domain.dto.country.CountryDto
import com.lavyshyk.domain.dto.country.CountryInfoMapDto
import com.lavyshyk.domain.dto.country.LanguageDto


fun MutableList<Capital>.transformToListCapitalDto(): MutableList<CapitalDto> {
    val list = mutableListOf<CapitalDto>()
    this.forEach { list.add(CapitalDto(it.capital ?: "")) }
    return list
}


fun MutableList<Country?>.transformDbEntitiesToCountryDto(): MutableList<CountryDto> {
    val list: MutableList<CountryDto> = mutableListOf()
    this.let { it ->
        it.forEach { item ->
            list.add(
                CountryDto(
                    item?.nameCountry ?: "",
                    item?.capital ?: "",
                    item?.region ?: "",
                    item?.population ?: 0L,
                    mutableListOf(item?.lat ?: 0.0, item?.lng ?: 0.0),
                    item?.area ?: 0F,
                    item?.nativeName ?: "",
                    mutableListOf(),
                    0f,
                    0,
                    0
                )
            )
        }
    }
    return list
}

fun Country.transformCountryBDToEntitiesDto(): CountryDto {
    return CountryDto(
        this.nameCountry,
        this.capital,
        this.region,
        this.population,
        mutableListOf(this.lat, this.lng),
        this.area,
        this.nativeName,
        mutableListOf(),
        0f,
        0,
        0

    )
}

fun CountryDto.transformCountryDtoToDbEntities(): Country =
    Country(
        this.name,
        this.capital,
        this.region,
        this.population,
        this.latlng[0],
        this.latlng[1],
        this.area,
        this.nativeName,
    )

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
        },
        0f,
        0,
        0
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

fun LanguageDto.transformDtoToLanguageData(): CountryDataInfo.LanguageData =
    CountryDataInfo.LanguageData(this.name, this.nativeName)

fun CountryDataInfo.LanguageData?.transformLanguageDataToLanguageDto(): LanguageDto {
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
                        mutableListOf(
                            item.latlng[0] ?: 0.0,
                            item.latlng[1] ?: 0.0
                        )
                    },
                    item.area ?: 0.0F,
                    item.nativeName ?: "",
                    listOfLanguageDto.apply {
                        item.languages?.forEach { it ->
                            it.transformLanguageDataToLanguageDto()
                                .also { i -> listOfLanguageDto.add(i) }
                        }
                    },
                    0f,
                    0,
                    0
                )
            )
        }
    }
    return listOfCountryDto
}

fun MutableList<CountryDto>.transformToCountry(): MutableList<CountryDataInfo> {
    val listOfLanguageData: MutableList<CountryDataInfo.LanguageData> = mutableListOf()
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




