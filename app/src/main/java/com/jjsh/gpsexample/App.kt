package com.jjsh.gpsexample

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.jjsh.gpsexample.datas.SubwayStation

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object{
        val stationData = mutableListOf<SubwayStation>()
        val progressOn = MutableLiveData(false)
        val selectedPosition = doubleArrayOf(0.0,0.0)
        lateinit var instance : App
    }
}
