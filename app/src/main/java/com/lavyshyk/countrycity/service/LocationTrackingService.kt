package com.lavyshyk.countrycity.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.lavyshyk.countrycity.*

class LocationTrackingService : Service(), LocationListener {

    private var mLocation: Location? = null
    protected var mLocationManager: LocationManager? = null
    private var mCheckIsGPSTurnedOn = false
    private var mCheckNetworkIsTurnedOn = false

    companion object {
        const val NEW_LOCATION_ACTION = "NEW_LOCATION_ACTION_BY_TRACKING_SERVICE"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (!intent.hasExtra(KILL_SERVICE)) {
                initLocationScan()
                initNotification()
            } else {
                killService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initNotification() {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        //val penIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.foreground_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)

        val mBigTextStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle(getString(R.string.foreground_channel_name))

        mBuilder.apply {
            this.setWhen(System.currentTimeMillis())
                .setStyle(mBigTextStyle)
                .setSmallIcon(R.drawable.navigation)
                .setContentText(getString(R.string.text_notification_location_tracking))
                //.setContentIntent(penIntent) -> to Fragment?
                //.addAction()
                .setFullScreenIntent(pendingIntent, true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.priority = NotificationManager.IMPORTANCE_DEFAULT
            } else {
                this.priority = NotificationCompat.PRIORITY_DEFAULT
            }
        }
        startForeground(SERVICE_ID, mBuilder.build())
    }

    private fun initLocationScan(): Location? {
        try {
            mLocationManager =
                applicationContext?.getSystemService(LOCATION_SERVICE) as LocationManager
            mCheckIsGPSTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            mCheckNetworkIsTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!mCheckIsGPSTurnedOn) {
                Log.e("location alert", "GPS turned off")
            } else {
                applicationContext?.let {
                    if (mCheckIsGPSTurnedOn) {
                        if (ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            mLocationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                TIME_INTERVAL_UPDATES,
                                DISTANCE_CHANGE_FOR_UPDATE,
                                this
                            )
                            if (mLocationManager != null) {
                                mLocation =
                                    mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                Log.e("location", " get location")

                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mLocation
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onLocationChanged(location: Location) {
        val intent = Intent().apply {
            action = NEW_LOCATION_ACTION
            putExtra("location", mLocation)
            Log.e("location", " put location")
        }
        sendBroadcast(intent)
    }

    private fun killService() {
        stopListening()
        stopForeground(true)
        stopSelf()
    }

    private fun stopListening() {
        mLocationManager?.let { locationManager ->
            applicationContext?.let { _ ->
                locationManager.removeUpdates(this@LocationTrackingService)
            }
        }
    }
}