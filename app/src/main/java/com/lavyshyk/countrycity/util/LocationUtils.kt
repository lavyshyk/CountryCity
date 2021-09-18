package com.lavyshyk.countrycity.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable


private var locationUser = Location("")

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context) : Location {
    var mLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val task: Task<Location>? = mLocationProviderClient?.lastLocation
    task?.addOnSuccessListener { location ->
        location?.let {
            locationUser = location
        }
    }
    return locationUser
}

@SuppressLint("MissingPermission")
fun getCurrentLocationFlowable(context: Context): Flowable<Location> {
    return Flowable.create<Location>({
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                locationUser = location
            }

        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        locationUser = location
                        it.onNext(location)
                        it.onComplete()
                    } else {
                        it.onError(Exception())
                        it.onComplete()
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }, BackpressureStrategy.LATEST)
}