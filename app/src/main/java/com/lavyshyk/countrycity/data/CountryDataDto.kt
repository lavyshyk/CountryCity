package com.lavyshyk.countrycity.data

data class CountryDataDto(

    var name : String,
    var capital : String? = "",
    var region : String? = "",
    var population : Long? = 0L,
    var area : Float? = 0.0F,
  //  var numericCode : Int? = 0,
//   var currencies : List<CurrenciesDto>? = listOf(),
    var languages : List<LanguageDto>? = listOf(),

    )
{

//    data class CurrenciesDto(
//
//        var code: String? = "",
//        var name: String? = "",
//        var symbol: String? = ""
//    )
}
