package com.jjsh.gpsexample.utils

import kotlin.math.*

object DistanceCalculator {

    fun distance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Double{
        val theta = lon1 - lon2
        var dist = sin(degreeToRadian(lat1)) * sin(degreeToRadian(lat2))
        + cos(degreeToRadian(lat1)) * cos(degreeToRadian(lat2)) * cos(degreeToRadian(theta))

        dist = acos(dist)
        dist = radianToDegree(dist)
        dist *= 60 * 1.1515
        dist *= 1609.344

        return dist
    }

    private fun degreeToRadian(degree : Double) : Double
        = (degree * Math.PI / 180.0)

    private fun radianToDegree(radian : Double) : Double
        = (radian * 180 / Math.PI)

}