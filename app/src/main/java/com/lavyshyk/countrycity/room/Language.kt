package com.lavyshyk.countrycity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languagesDB")
class Language(
    @PrimaryKey
    var name: String,
    var nativeName: String
)