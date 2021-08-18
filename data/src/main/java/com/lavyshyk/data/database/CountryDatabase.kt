package com.lavyshyk.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lavyshyk.data.database.room.entyties.Country
import com.lavyshyk.data.database.room.dao.CountryDao
import com.lavyshyk.data.database.room.entyties.Language
import com.lavyshyk.data.database.room.dao.LanguageDao


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
