package com.jjsh.gpsexample.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jjsh.gpsexample.App
import com.jjsh.gpsexample.utils.GPSTracker
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MainViewModel(app: Application) : AndroidViewModel(app) {
    @SuppressLint("StaticFieldLeak")
    var context : Context? = null

    val locationText = MutableLiveData("")
    val subwayText = MutableLiveData("")

    fun testOnClick(){
        val con = context ?: return
        viewModelScope.launch {
            App.progressOn.postValue(true)
            val gpsTracker = GPSTracker(con)
            var result = ""
            result += "${gpsTracker.latitude}\n"
            result += "${gpsTracker.longitude}\n\n"
            result += getAddress(gpsTracker.latitude,gpsTracker.longitude)
            locationText.postValue(result)

            getData()

            App.progressOn.postValue(false)
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context)
        val addressList : MutableList<Address>
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
        var result  = ""
        addressList.forEach { address ->
            for (i in 0 .. address.maxAddressLineIndex){
                result += "${address.getAddressLine(i)}\n"
            }
        }
        return result
    }

    private fun getLocation(address : String) : String{
        val geocoder = Geocoder(context)
        var latitude  = 0.0
        var longitude = 0.0
        try {
            val list = geocoder.getFromLocationName(address,5)
            val location = list[0].let{
                latitude = it.latitude
                longitude = it.longitude
            }
        }catch (e : Exception){

        }
        return "위도 : $latitude\n경도 : $longitude"
    }

    private fun getData(){
        val result = StringBuilder()
        App.stationData.forEach {
            result.append("${it.name} : ${it.line} : ${it.address}\n ${it.latitude} \n ${it.longitude}\n")
        }
        subwayText.postValue(result.toString())
    }
}