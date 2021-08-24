package com.lavyshyk.data.database.room.dao

import androidx.room.*
import com.lavyshyk.data.database.room.entyties.Country
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setCountry(country: Country)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveListCountry(list: MutableList<Country>)

    @Query("SELECT * FROM countries_table")
    fun getListCountry(): Flowable<MutableList<Country?>>

//    @Query("SELECT * FROM countries_table")
//    fun getListCountrySimple(): MutableList<Country>

//    @Query("SELECT * FROM countries_table")
//    fun getListCountryLiveData(): LiveData<MutableList<Country>>

    @Update
    fun updateCountry(country: Country)

    @Update
    fun updateListCountry(list: MutableList<Country>)

    @Delete
    fun deleteListCountry(list: MutableList<Country>)


// quick query for check BD on contain
    @Query("SELECT nameCountry FROM countries_table")
    fun getListCountryName(): Flowable<MutableList<String>>

}