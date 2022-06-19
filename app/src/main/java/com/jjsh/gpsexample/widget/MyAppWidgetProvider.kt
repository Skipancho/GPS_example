package com.jjsh.gpsexample.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.utils.GPSTracker

class MyAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
        remoteViews.setTextViewText(R.id.loc_tv,getLocation(context))
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    private fun getLocation(context: Context) : String{
        val gpsTracker = GPSTracker(context)
        return "위도 : ${gpsTracker.latitude}\n" +
                "경도 : ${gpsTracker.longitude}"
    }
}