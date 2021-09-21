package com.lavyshyk.domain.dto.country

data class CountryDto(
    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    val latlng : MutableList<Double>,
    var area: Float,
    val nativeName: String,
    var languages: MutableList<LanguageDto>,
    var distance: Float,
    var like: Int,
    var dislike: Int
)

fun CountryDto.setLike(){
    this.like++
}fun CountryDto.setDislike(){
    this.dislike++
}
