package com.lavyshyk.countrycity.data

data class CountryDataDto(

    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    var area: Float,
    //  var numericCode : Int? = 0,
    var languages: List<LanguageDto>
   //  var currencies : List<CurrenciesDto>? = listOf(),
) {


//    data class CurrenciesDto(
//
//        var code: String? = "",
//        var name: String? = "",
//        var symbol: String? = ""
//    )
}
