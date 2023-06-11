package com.nishanth.notificationexample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App: Application() {
    //if we want to write some code for the start of application and not for any activity , then it is the right place to do it
    private val CHANNEL_1_ID="channel1"
    private val CHANNEL_2_ID="channel2"
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    private fun createNotificationChannels(){
       if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
           val channel1=NotificationChannel(CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH).apply {
               description="This is Channel 1"
           }
           val channel2=NotificationChannel(CHANNEL_2_ID,"Channel 2", NotificationManager.IMPORTANCE_LOW).apply {
               description="This is Channel 2"
           }
           val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
           manager.createNotificationChannel(channel1)
           manager.createNotificationChannel(channel2)
       }
    }
}