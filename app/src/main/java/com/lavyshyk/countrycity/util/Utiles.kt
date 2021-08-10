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
//            println("DATA  save Language in dataBase" + Thread.currentThread().name)
//            item.languages.let {
//                it.forEach { it ->
//                    CountryApp.database?.languageDao()
//                        ?.saveLanguage(Language(it.name, it.nativeName))
//                }
//            }
        }
    }

    return list
}

/*
a few functions to transform repo entities to Dto and vice-versa
 */

fun LanguageDto.transformDtoToLanguageData(): LanguageData =
    LanguageData(this.name, this.nativeName)

fun LanguageData?.transformLanguageDataToLanguageDto(): LanguageDto {
    return LanguageDto(this?.name.orEmpty(), this?.nativeName.orEmpty())
}
fun LanguageDto.transformDtoToLanguage(): Language =
    Language(this.name, this.nativeName)

fun Language?.transformLanguageToLanguageDto(): LanguageDto {
    return LanguageDto(this?.languageName.orEmpty(), this?.nativeName.orEmpty())
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

                    if (item.latlng.isNullOrEmpty()) {
                        mutableListOf<Double>(0.0, 0.0)
                    } else {
                        mutableListOf(item.latlng[0], item.latlng[1])
                    },
                    item.area ?: 0.0F,
                    item.nativeName ?: "",
                    list . apply {
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
                    mutableListOf(item.latlng[0], item.latlng[1]),
                    item.area,
                    item.nativeName,
                    list.apply {
                        item.languages.forEach { it ->
                            it.transformDtoToLanguageData().also { i -> list.add(i) }
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
    val list: MutableList<CountryInfoMapDto> = mutableListOf()
    this.let {
        it.forEach { item ->
            list.add(
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
    return list
}




