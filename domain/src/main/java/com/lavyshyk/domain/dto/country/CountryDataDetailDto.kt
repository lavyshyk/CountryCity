package com.lavyshyk.domain.dto.country

class CountryDataDetailDto(
    val name: String,
    val capital: String,
    val region: String,
    val population: Long,
    val latlng: MutableList<Double>,
    val area: Float,
    val flag: String,
    val languages: MutableList<LanguageDto>,
)