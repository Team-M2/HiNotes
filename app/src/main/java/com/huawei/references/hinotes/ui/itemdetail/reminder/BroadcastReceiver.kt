package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.huawei.hms.location.GeofenceData
import com.huawei.references.hinotes.MainActivity
import com.huawei.references.hinotes.R


class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderType = intent!!.getIntExtra("reminderType", -1)
        val reminderTitle = intent.getStringExtra("reminderTitle")
        var notificationDescription:String?=null

        if(reminderType == 1){
            notificationDescription = "It is time for the $reminderTitle note."
            showNotification(context, reminderTitle, notificationDescription)
        }
        else if(reminderType == 0){
            val action = intent.action
            if (ACTION_PROCESS_LOCATION == action) {
                val geofenceData = GeofenceData.getDataFromIntent(intent)
                if (geofenceData != null) {
                    notificationDescription=
                        "You have reached or left the area you have created for the $reminderTitle note."
                    showNotification(context,reminderTitle,notificationDescription)
                }
            }
        }
    }

    private fun showNotification(context: Context?,title: String?, notificationDescription:String?) {
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
            .setContentTitle(title)
            .setContentText(notificationDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    companion object {
        const val ACTION_PROCESS_LOCATION =
            "com.huawei.references.hinotes.ui.itemdetail.reminder.BroadcastReceiver.ACTION_PROCESS_LOCATION"
    }
}