package com.lavyshyk.countrycity.data

import com.google.gson.annotations.SerializedName

data class CountryData (

    @SerializedName("name")
    val name : String,
    @SerializedName("alpha3Code")
    val alpha3Code : String,
    @SerializedName("capital")
    val capital : String,
    @SerializedName("region")
    val region : String,
    @SerializedName("population")
    val population : Long,
    @SerializedName("area")
    val area : Float,
    @SerializedName("numericCode")
    val numericCode : Int,
    @SerializedName("currencies")
    val currencies : List<Currencies>,
    @SerializedName("languages")
    val languages : List<Languages>,

)
{

    data class Languages (

        @SerializedName("name")
        val name : String,
        @SerializedName("nativeName")
        val nativeName : String
    )
    data class Currencies (

        @SerializedName("code")
        val code : String,
        @SerializedName("name")
        val name : String,
        @SerializedName("symbol")
        val symbol : String
    )
}
