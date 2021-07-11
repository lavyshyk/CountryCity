package com.lavyshyk.countrycity.dto

data class CountryDataDto(

    var name: String?,
    var capital: String?,
    var region: String?,
    var population: Long?,
    var area: Float?,
    //  var numericCode : Int? = 0,
    var languages: MutableList<LanguageDto>?
   //  var currencies : List<CurrenciesDto>? = listOf(),
) {


//    data class CurrenciesDto(
//
//        var code: String? = "",
//        var name: String? = "",
//        var symbol: String? = ""
//    )
}
