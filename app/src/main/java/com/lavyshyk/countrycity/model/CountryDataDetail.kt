package com.lavyshyk.countrycity.model

import com.google.gson.annotations.SerializedName

class CountryDataDetail(
    @SerializedName("name") val name : String?,
    @SerializedName("capital") val capital : String?,
    @SerializedName("region") val region : String?,
    @SerializedName("population") val population : Long?,
    @SerializedName("latlng") val latlng : MutableList<Double>?,
    @SerializedName("area") val area : Float?,
    @SerializedName("flag") val flag : String?,
    @SerializedName("languages") val languages : MutableList<CountryDataInfo.LanguageData>?,
    )