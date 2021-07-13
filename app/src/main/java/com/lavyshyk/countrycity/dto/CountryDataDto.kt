package com.lavyshyk.countrycity.dto

import com.google.gson.annotations.SerializedName

data class CountryDataDto(

    @SerializedName("name")
    var name: String?,
    @SerializedName("capital")
    var capital: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("population")
    var population: Long?,
    @SerializedName("area")
    var area: Float?,
    @SerializedName("latlng")
    var latlng: List<Double>?,
    //@SerializedName("name")
    //  var numericCode : Int? = 0,
    @SerializedName("languages")
    var languages: MutableList<LanguageDto>?,
    @SerializedName("flag")
    var flag: String?
    //  var currencies : List<CurrenciesDto>? = listOf(),
) {


//    data class CurrenciesDto(
//
//        var code: String? = "",
//        var name: String? = "",
//        var symbol: String? = ""
//    )
}
