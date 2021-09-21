package com.lavyshyk.countrycity.util

import com.lavyshyk.domain.dto.country.CountryDataDetailDto
import com.lavyshyk.domain.dto.country.CountryDto
import kotlin.math.pow


fun CountryDto.convertToCorrectArea(): Long {
    val area = this.area.toString()
    val currentArea = if (area.contains("E")) {
        val (a, b) = area.split("E")
        (a.toFloat() * 10F.pow(b.toInt())).toLong()
    } else {
        this.area.toLong()
    }
    return currentArea
}

fun CountryDataDetailDto.convertToCorrectArea(): Long {
    val area = this.area.toString()
    val currentArea = if (area.contains("E")) {
        val (a, b) = area.split("E")
        (a.toFloat() * 10f.pow(b.toInt())).toLong()
    } else {
        this.area.toLong()
    }
    return currentArea
}

fun getCurrentZoomFofMap(country: CountryDataDetailDto):Float {
    var zoom = 0F
    when(country.area){
        in 0F .. 1000F -> { zoom = 10F }
        in 1001F..10000F -> { zoom = 8F }
        in 10001F..70000F -> { zoom = 6F }
        in 700001F..120000F -> { zoom = 5F }
        in 120001F..1600000F -> { zoom = 4F }
        in 1600001F..3000000F -> { zoom = 3F }
        in 3000001F..10000000F -> { zoom = 2F }
        else -> { zoom }
    }
    return zoom
}



