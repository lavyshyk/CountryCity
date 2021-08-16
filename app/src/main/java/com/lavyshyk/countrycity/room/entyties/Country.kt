package com.lavyshyk.countrycity.room.entyties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_table")
data class Country(
    @PrimaryKey
    val nameCountry: String,
    val capital: String,
    val region: String,
    val population: Long,
    val lat : Double,
    val lng : Double,
    val area: Float,
    val nativeName: String,
)
