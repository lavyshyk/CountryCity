package com.lavyshyk.countrycity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lavyshyk.countrycity.room.dao.CountryDao
import com.lavyshyk.countrycity.room.dao.LanguageDao
import com.lavyshyk.countrycity.room.entyties.Country
import com.lavyshyk.countrycity.room.entyties.Language

@Database(
    entities = [Country::class, Language::class], version = 1, exportSchema = false

)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun languageDao(): LanguageDao

    companion object {
        private var database: CountryDatabase? = null

        fun getInstance(context: Context): CountryDatabase? {
            if (database == null) {
                synchronized(CountryDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        CountryDatabase::class.java, "country-database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return database
        }



    }
}
