package com.jjsh.gpsexample.viewmodels

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jjsh.gpsexample.utils.GPSTracker
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    var context : Context? = null

    val locationText = MutableLiveData("")

    fun testOnClick(){
        val con = context ?: return
        viewModelScope.launch {
            val gpsTracker = GPSTracker(con)
            var result = ""
            result += "${gpsTracker.latitude}\n"
            result += "${gpsTracker.longitude}\n"
            result += getAddress(gpsTracker.latitude,gpsTracker.longitude)
            locationText.postValue(result)
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context)
        var addressList = mutableListOf<Address>()
        try {
            addressList = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return "서비스 불가 지역"
        }
        if (addressList.isNullOrEmpty()) {
            return "주소 미발견"
        }
        return addressList[0].getAddressLine(0)
    }
}