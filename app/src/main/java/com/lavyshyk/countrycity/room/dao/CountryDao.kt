package com.lavyshyk.countrycity.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lavyshyk.countrycity.room.entyties.Country
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setCountry(country: Country)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveListCountry(list: MutableList<Country>)

    @Query("SELECT * FROM countries_table")
    fun getListCountry(): Flowable<MutableList<Country>>?

    @Query("SELECT * FROM countries_table")
    fun getListCountryObserve(): MutableList<Country>

    @Query("SELECT * FROM countries_table")
    fun getListCountryLiveData(): LiveData<MutableList<Country>>

    @Update
    fun updateCountry(country: Country)

    @Update
    fun updateListCountry(list: MutableList<Country>)

    @Delete
    fun deleteListCountry(list: List<Country>)


// quick query for check BD on contain
    @Query("SELECT nameCountry FROM countries_table")
    fun getListCountryName(): MutableList<String>

}