package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.reminder_by_time_fragment.*
import kotlinx.android.synthetic.main.reminder_by_time_fragment.view.*

class ReminderByTimeFragment : BottomSheetDialogFragment() {

    companion object{
        private val default_notification_channel_id = "default"
        var notification_:Notification?=null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.reminder_by_time_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date_picker.minDate = System.currentTimeMillis() - 1000;

        view.save_text.setOnClickListener {
            scheduleNotification(getNotification("Notification")!!,10000)
            this.dismiss()
        }

        view.delete_text.setOnClickListener {
            this.dismiss()
        }
    }

    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(this.activity, BroadcastReceiver::class.java)
        notification_ = notification
        notificationIntent.putExtra("notificationId", 1)
        notificationIntent.putExtra("notification", notification)
        notificationIntent.putExtra("reminderType",1)
        val pendingIntent = PendingIntent.getBroadcast(
            this.activity,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        val alarmManager: AlarmManager =
            (this.context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent)
    }

    private fun getNotification(content: String): Notification? {
        val builder =
            NotificationCompat.Builder(this.context!!,
                default_notification_channel_id
            )
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.reminder_icon)
        builder.setAutoCancel(true)
        builder.setChannelId("1001")
        return builder.build()
    }
}