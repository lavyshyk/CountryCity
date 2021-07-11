package com.lavyshyk.countrycity.room

import androidx.room.*

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setCountry(country: Country)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveListCountry(list: MutableList<Country>)

    @Query("SELECT * FROM countryDB")
    fun getListCountry(): MutableList<Country>

    @Update
    fun updateCountry(country: Country)

    @Update
    fun updateListCountry(list: MutableList<Country>)

    @Delete
    fun deleteListCountry(list: List<Country>)

}