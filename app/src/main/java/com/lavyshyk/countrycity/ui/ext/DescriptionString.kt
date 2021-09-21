package com.lavyshyk.countrycity.ui.ext

import android.content.Context
import com.lavyshyk.countrycity.R
import com.lavyshyk.domain.dto.country.CountryDataDetailDto

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