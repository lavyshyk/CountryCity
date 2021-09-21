package com.lavyshyk.countrycity.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


fun Context.checkLocationPermission(): Boolean =
    if (ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        true
    } else {
//        val list = ArrayList<String>()
//        list.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        ActivityCompat.requestPermissions(
//            this as Activity, list.toTypedArray(),
//            PERMISSION_ACCESS_LOCATION_REQUEST_STORAGE
//        )
//        true
        false
    }


