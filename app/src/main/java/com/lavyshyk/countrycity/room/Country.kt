package com.lavyshyk.countrycity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countryDB")
data class Country(
    @PrimaryKey
    var name: String,
    var capital: String,
    var region: String,
    var population: Long,
    var area: Float,
    var flag: String
    //  var languages: List<Language>
//    @Ignore
//    val currencies : List<CountryDataDto.Currencies> = mutableListOf() ,
//    @Ignore
)