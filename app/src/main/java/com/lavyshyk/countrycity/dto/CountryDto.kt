package com.lavyshyk.countrycity.dto

data class CountryDto(
    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    val latlng : MutableList<Double>,
    var area: Float,
    var languages: MutableList<LanguageDto>,
)
