package com.lavyshyk.domain.dto

data class CountryDto(
    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    val latlng : MutableList<Double>,
    var area: Float,
    val nativeName: String,
    var languages: MutableList<LanguageDto>,
)
