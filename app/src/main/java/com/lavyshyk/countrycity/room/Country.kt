package com.lavyshyk.countrycity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country-database")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val idCountry: Int,
    var name : String = "",
    val alpha3Code : String,
    var capital : String = "",
    val region : String,
    val population : Long,
    val area : Float,
    val numericCode : Int,
//    @Ignore
//    val currencies : List<CountryData.Currencies>,
//    @Ignore
//    val languages : List<CountryData.Languages>,
) {
}