package com.jjsh.gpsexample.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.jjsh.gpsexample.activities.MainActivity

class GPSTracker(
    private val context: Context
    ) : LocationListener, Service() {

    private var location : Location? = null
    private lateinit var locationManager : LocationManager
    var latitude = 0.0
    var longitude = 0.0

    init {
        getLocation()
    }

    fun getLocation() : Location?{
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnable || isNetworkEnable){

                for (permission in MainActivity.requiredPermission){
                    val hasPermission = ContextCompat.checkSelfPermission(context,permission)
                    if (hasPermission != PackageManager.PERMISSION_GRANTED ) return null
                }

                if (isNetworkEnable){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                }
                if (isGPSEnable){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        location?.let{
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                    }
                }
            }
        }catch (e : Exception){
            Log.e("GPSTracker","$e")
        }
        return location
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    fun stopUsingGPS(){
        locationManager.removeUpdates(this)
    }

    companion object{
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0F
        const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1L
    }
}