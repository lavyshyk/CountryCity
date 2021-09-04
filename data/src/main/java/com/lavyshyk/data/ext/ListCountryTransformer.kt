package com.lavyshyk.data.ext

import com.lavyshyk.data.model.CountryDataInfo
import com.lavyshyk.domain.dto.CountryDto
import com.lavyshyk.domain.outcome.Transformer

class ListCountryTransformer : Transformer<MutableList<CountryDataInfo>, MutableList<CountryDto>> {
    override var convert: (MutableList<CountryDataInfo>) -> MutableList<CountryDto> =
        { data ->
            data.map { item ->
                CountryDto(
                    item.name ?: "",
                    item.capital ?: "",
                    item.region ?: "",
                    item.population ?: 0L,
                    if (item.latlng.isNullOrEmpty()) {
                        mutableListOf<Double>(0.0, 0.0)
                    } else {
                        mutableListOf(item.latlng[0] ?: 0.0, item.latlng[1] ?: 0.0)
                    },
                    item.area ?: 0.0F,
                    item.nativeName ?: "",
                    mutableListOf(),
//                    item.languages?.map { it ->
//                        LanguageDto(it.name.orEmpty(), it.nativeName.orEmpty())
//                    }?.toMutableList() ?: mutableListOf(),
                    0F,
                    0,
                    0
                )
            }.toMutableList()
        }
}