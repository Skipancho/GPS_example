package com.jjsh.gpsexample

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jjsh.gpsexample.datas.SubwayStation
import com.naver.maps.map.NaverMapSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naver_map_client_id))

    }
    companion object{
        val stationData = mutableListOf<SubwayStation>()
        val progressOn = MutableLiveData(false)
        var selectedPosition = doubleArrayOf(0.0,0.0)
        lateinit var instance : App
    }
}
