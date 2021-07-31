package com.lavyshyk.countrycity.dto

data class CountryDto(
    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    var area: Float,
    var languages: MutableList<LanguageDto>,
)
