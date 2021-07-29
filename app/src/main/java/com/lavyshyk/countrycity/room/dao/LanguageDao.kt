package com.lavyshyk.countrycity.room.dao

import androidx.room.*
import com.lavyshyk.countrycity.room.entyties.Language
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveLanguage(language: Language)

    @Transaction
    @Query("SELECT languageName FROM languages_database WHERE languageName = :name")
    fun getLanguageByCountry(name: String): Flowable<MutableList<String>>

}