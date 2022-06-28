package com.jjsh.gpsexample.utils

import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.jjsh.gpsexample.App
import com.jjsh.gpsexample.datas.GeoData
import org.json.JSONArray

object Prefs {
    private const val GEO_MAP = "geo_map"
    val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.instance)
    }
    var geoMap : MutableMap<String,GeoData>
        get() {
            val map = mutableMapOf<String,GeoData>()
            val json = prefs.getString(GEO_MAP,null)
            val gson = GsonBuilder().create()
            try {
                json?.let {
                    val jsonArray = JSONArray(it)
                    for (i in 0 until jsonArray.length()){
                        val geoData = gson.fromJson(
                            jsonArray[i].toString(),
                            GeoData::class.java
                        )
                        map[geoData.address] = geoData
                    }
                }
            }catch (e : Exception){
                Log.e("Prefs","geoMap get err")
            }
            return map
        }
        set(value) {
            val gson = GsonBuilder().create()
            val jsonArray = JSONArray()
            val list = value.values.toList()
            for (item in list){
                val json = gson.toJson(item, GeoData::class.java)
                jsonArray.put(json)
            }
            if (value.isNotEmpty())
                prefs.edit().putString(GEO_MAP,jsonArray.toString()).apply()
            else
                prefs.edit().putString(GEO_MAP,null).apply()
        }
}