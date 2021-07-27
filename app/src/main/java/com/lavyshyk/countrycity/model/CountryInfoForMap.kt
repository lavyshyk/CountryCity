package com.lavyshyk.countrycity.model

import com.google.gson.annotations.SerializedName

class CountryInfoForMap(
    @SerializedName("name") val name: String?,
    @SerializedName("capital") val capital: String?,
    @SerializedName("latlng") val latlng : MutableList<Double>?

)
