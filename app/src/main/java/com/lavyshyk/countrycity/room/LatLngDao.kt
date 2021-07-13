package com.lavyshyk.countrycity.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy


@Dao
interface LatLngDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setLatLang(latLng: LatLng)
}