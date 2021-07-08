package com.lavyshyk.countrycity.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lavyshyk.countrycity.room.CountryDatabase.Companion.LATEST_VERSION

@Database(entities = [Country::class, Language::class], version = LATEST_VERSION,
//autoMigrations = [
//    AutoMigration(
//        from = 1,
//        to = 2,
//    spec = CountryDatabase.CountryDatabaseMigration::class
//    )
//]
)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun languageDao(): LanguageDao

    companion object {
        const val LATEST_VERSION = 1
    }
//    @RenameTable(fromTableName = "country-database", toTableName = "country-database-lang")
//    class CountryDatabaseMigration: AutoMigrationSpec {
//
//    }
}
