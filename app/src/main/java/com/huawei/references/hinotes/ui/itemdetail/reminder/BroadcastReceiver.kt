package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.huawei.hms.location.GeofenceData
import com.huawei.references.hinotes.MainActivity
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.itemdetail.reminder.ReminderByTimeFragment.Companion.notification_


class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderType = intent!!.getIntExtra("reminderType", -1)
        if(reminderType == 1){
            val notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(
                    "1001",
                    "NOTIFICATION_CHANNEL_NAME",
                    importance
                )
                assert(notificationManager != null)
                notificationManager!!.createNotificationChannel(notificationChannel)
            }
            val id = intent!!.getIntExtra("notificationId", 0)
            assert(notificationManager != null)
            notificationManager!!.notify(id, notification_)
        }
        else{
            if (intent != null) {
                val action = intent.action
                val sb = StringBuilder()
                val next = "\n"
                if (ACTION_PROCESS_LOCATION == action) {
                    val geofenceData = GeofenceData.getDataFromIntent(intent)
                    if (geofenceData != null) {
                        showNotification(context)
                        val errorCode = geofenceData.errorCode
                        val conversion = geofenceData.conversion
                        val list =
                            geofenceData.convertingGeofenceList
                        val mLocation: Location = geofenceData.convertingLocation
                        val status = geofenceData.isSuccess
                        sb.append("errorCode: $errorCode$next")
                        sb.append("conversion: $conversion$next")
                        for (i in list.indices) {
                            sb.append("geoFence id :" + list[i].uniqueId + next)
                        }
                        sb.append(
                            "location is :" + mLocation.getLongitude()
                                .toString() + " " + mLocation.getLatitude().toString() + next
                        )
                        sb.append("is successful :$status")
                    }
                }
            }
        }
    }

    private fun showNotification(context: Context?) {
        val channelId="channelId"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelName"
            val descriptionText = "channelDescp"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.reminder_icon)
            .setContentTitle("My reminder notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    companion object {
        const val ACTION_PROCESS_LOCATION =
            "com.huawei.hmssample.geofence.GeoFenceBroadcastReceiver.ACTION_PROC ESS_LOCATION"
    }
}