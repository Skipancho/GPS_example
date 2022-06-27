package com.jjsh.gpsexample.utils

import kotlin.math.*

object DistanceCalculator {

    fun distance2(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Double{
        val lat = lat1 - lat2
        val lon = lon1 - lon2

        val latR : Int = lat.toInt()
        val latM : Int = ((lat - latR) * 60).toInt()
        val latS : Double = ((lat - latR) * 60 - latM) * 60

        val lonR : Int = lon.toInt()
        val lonM : Int = ((lon - lonR) * 60).toInt()
        val lonS : Double = ((lon - lonR) * 60 - lonM) * 60

        val dist1 = lonR * 88.9036 + lonM * 1.4817 + lonS * 0.0246
        val dist2 = latR * 111.3194 + latM * 1.8553 + latS * 0.0309

        return sqrt(dist1*dist1 + dist2*dist2) * 1000
    }


    fun distance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Double{
        val theta = lon1 - lon2
        var dist = sin(degreeToRadian(lat1)) * sin(degreeToRadian(lat2))
        + cos(degreeToRadian(lat1)) * cos(degreeToRadian(lat2)) * cos(degreeToRadian(theta))

        dist = acos(dist)
        dist = radianToDegree(dist)
        dist *= 60 * 1.1515
        //dist *= 1609.344

        return dist
    }

    private fun degreeToRadian(degree : Double) : Double
        = (degree * Math.PI / 180.0)

    private fun radianToDegree(radian : Double) : Double
        = (radian * 180 / Math.PI)

}