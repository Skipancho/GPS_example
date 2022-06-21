package com.jjsh.gpsexample

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jjsh.gpsexample.datas.SubwayStation

class App : Application() {
    companion object{
        val stationData = mutableListOf<SubwayStation>()
        val progressOn = MutableLiveData(false)
        var clickable = true
    }
}