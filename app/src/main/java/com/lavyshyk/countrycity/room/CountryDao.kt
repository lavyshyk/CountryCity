package com.lavyshyk.countrycity.room

import androidx.room.*
import com.lavyshyk.countrycity.data.CountryData

@Dao
interface CountryDao {
    @Insert
    fun setCountry(country: Country)

    @Insert
    fun setListCountry(list: MutableList<Country>)

    @Query("SELECT * FROM `country-database`")
    fun getListCountry(): MutableList<CountryData>

    @Update
    fun updateCountry(country: Country)

    @Update
    fun updateListCountry(list: MutableList<Country>)

    @Delete
    fun deleteListCountry(list: List<Country>)

}