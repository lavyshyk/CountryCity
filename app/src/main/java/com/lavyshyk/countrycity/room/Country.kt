package com.lavyshyk.countrycity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countryDB")
data class Country(
    @PrimaryKey
    //var idCountry: Int? = 0,
    var name: String ,
    var capital: String?,
    var region: String?,
    var population: Long?,
    var area: Float?,
    //var numericCode: Int?
    //val languages : List<CountryDataDto.Languages> = mutableListOf(),
//    @Ignore
//    val currencies : List<CountryDataDto.Currencies> = mutableListOf() ,
//    @Ignore
)