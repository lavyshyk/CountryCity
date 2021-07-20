package com.lavyshyk.countrycity.model

import com.google.gson.annotations.SerializedName

class CountryDataInfo(
    @SerializedName("name") val name: String?,
    @SerializedName("capital") val capital: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("population") val population: Long?,
    @SerializedName("area") val area: Float?,
    @SerializedName("languages") val languages: MutableList<LanguageData>?

) {
    data class LanguageData (

//        @SerializedName("iso639_1") val iso639_1 : String,
//        @SerializedName("iso639_2") val iso639_2 : String,
        @SerializedName("name") val name : String?,
        @SerializedName("nativeName") val nativeName : String?
    )
}