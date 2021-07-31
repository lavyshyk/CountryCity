package com.lavyshyk.countrycity.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lavyshyk.countrycity.room.dao.CountryDao
import com.lavyshyk.countrycity.room.dao.LanguageDao
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.room.entyties.Language

@Database(entities = [Country::class, Language::class], version = 1, exportSchema = false

)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun languageDao(): LanguageDao

}
