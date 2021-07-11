package com.lavyshyk.countrycity.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setLanguage(language: Language)


    @Query("SELECT nativeName FROM languagesDB WHERE name = :name")
    fun getLanguageByCountry(name: String): List<String>

}