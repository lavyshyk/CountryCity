package com.lavyshyk.data.database.room.dao

import androidx.room.*
import com.lavyshyk.data.database.room.entyties.Language
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveLanguage(languages: MutableList<Language>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateLanguage(languages: MutableList<Language>)

    @Transaction
    @Query("SELECT languageName FROM languages_database WHERE languageName = :name")
    fun getLanguageByCountry(@NonNull name: String): Flowable<MutableList<String>>

    @Transaction
    @Query("SELECT * FROM languages_database WHERE countryName = :name")
    fun getListLanguageByCountry(@NonNull name: String): Flowable<MutableList<Language?>>

}