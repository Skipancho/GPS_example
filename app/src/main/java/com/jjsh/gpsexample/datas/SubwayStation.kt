package com.jjsh.gpsexample.datas

data class SubwayStation(
    val name : String,
    val line : String,
    val address : String,
    val latitude : Double,
    val longitude : Double,
    var distance : Double = 0.0
)
