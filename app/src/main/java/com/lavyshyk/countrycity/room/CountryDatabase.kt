package com.lavyshyk.countrycity.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}
