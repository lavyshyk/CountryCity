package com.lavyshyk.data.database.room.entyties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages_database")
class Language(
    @PrimaryKey
    val languageName: String,
    val nativeName: String,
    val countryName: String,
)