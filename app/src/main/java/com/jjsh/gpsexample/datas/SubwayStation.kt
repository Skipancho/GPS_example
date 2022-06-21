package com.jjsh.gpsexample.datas

data class SubwayStation(
    val name : String,
    val line : String,
    val address : String,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0
)
