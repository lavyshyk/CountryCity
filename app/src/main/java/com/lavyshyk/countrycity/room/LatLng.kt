package com.lavyshyk.countrycity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationDB")
class LatLng(
    @PrimaryKey
    var latitude: Double,
    var longitude: Double
) {
}