package com.jjsh.gpsexample.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.activities.MainActivity
import com.jjsh.gpsexample.utils.GPSTracker
import splitties.toast.toast

class MyAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            val remoteViews = addViews(context)
            appWidgetManager.updateAppWidget(it,remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val widgetManager = AppWidgetManager.getInstance(context)
        val views = RemoteViews(context.packageName,R.layout.widget)
        val thisAppWidget = ComponentName(context.packageName,MyAppWidgetProvider::class.java.name)
        val action = intent.action

        if (action == MY_ACTION){
            //views.setTextViewText(R.id.loc_tv,getLocation(context)
            widgetManager.updateAppWidget(thisAppWidget,views)
        }
    }

    private fun getLocation(context: Context) : String{
        val gpsTracker = GPSTracker(context)
        return "위도 : ${gpsTracker.latitude}\n" +
                "경도 : ${gpsTracker.longitude}"
    }

    private fun addViews(context: Context) : RemoteViews{
        val views = RemoteViews(context.packageName,R.layout.widget)
        //views.setOnClickPendingIntent(R.id.widget_btn,setMyAction(context))
        views.setTextViewText(R.id.loc_tv,getLocation(context))
        //views.setOnClickPendingIntent(R.id.loc_tv,setMyAction(context))
        return views
    }

    private fun setMyAction(context: Context) : PendingIntent{
        /*val intent = Intent(context,MyAppWidgetProvider::class.java).apply {
            action = MY_ACTION
        }
        return PendingIntent.getBroadcast(context, 0,intent,0)*/
        val intent = Intent(context,MainActivity::class.java).apply { action = MY_ACTION }
        return PendingIntent.getActivity(context,0,intent,0)
    }

    companion object{
        const val MY_ACTION = "android.action.MY_ACTION"
        //const val MY_ACTION_REQUEST_CODE = 1004
    }
}