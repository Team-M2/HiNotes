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
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.ItemDetailBottomSheetFragment
import com.huawei.references.hinotes.ui.base.ItemDetailBottomSheetType
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.TimeReminderTabAdapter
import kotlinx.android.synthetic.main.reminder_by_time_fragment.view.*
import java.util.*

class ReminderByTimeFragment(var item:Item) : ItemDetailBottomSheetFragment() {
    companion object{
        var reminderStaticCalendar: Calendar?=null
    }
    private var reminderCalendar: Calendar?=null
    private var notification_:Notification?=null
    private val default_notification_channel_id = "default"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.reminder_by_time_fragment, container, false)

    override val itemDetailBottomSheetType: ItemDetailBottomSheetType =
        ItemDetailBottomSheetType.REMINDER

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminderCalendar = Calendar.getInstance()
        reminderStaticCalendar = Calendar.getInstance()
        val adapter = TimeReminderTabAdapter(this.childFragmentManager)
        adapter.addFragment(DateFragment(item), "Date")
        adapter.addFragment(HourFragment(item), "Hour")
        view.viewPager.adapter=adapter
        view.tabLayout.setupWithViewPager(view.viewPager)

        println("mcmc $item")

        view.save_text.setOnClickListener {
            scheduleNotification(getNotification("Notification")!!,60000)
            (activity as? ItemDetailBaseActivity)?.timeReminderSelected(reminderStaticCalendar?.time!!)
            this.dismiss()
        }

        view.delete_text.setOnClickListener {
            this.dismiss()
            (activity as? ItemDetailBaseActivity)?.bottomSheetDeleteButtonClicked(itemDetailBottomSheetType)
        }

        if(item.reminder != null){

        }
    }

    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(this.activity, BroadcastReceiver::class.java)
        notification_ = notification
        notificationIntent.putExtra("notificationId", 1)
        notificationIntent.putExtra("reminderTitle",item.title)
        notificationIntent.putExtra("reminderDescription",item.description)
        notificationIntent.putExtra("reminderType",1)
        val pendingIntent = PendingIntent.getBroadcast(
            this.activity,
            item.itemId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        //val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        //var x :Long = ((reminderStaticCalendar?.timeInMillis!!).toString().substring(0, 10)).toLong();
        val alarmManager: AlarmManager =
            (this.context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
        alarmManager.set(AlarmManager.RTC_WAKEUP,
            reminderStaticCalendar?.timeInMillis!!, pendingIntent)
    }

    private fun getNotification(content: String): Notification? {
        val builder =
            NotificationCompat.Builder(this.context!!,
                default_notification_channel_id
            )
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setSmallIcon(R.drawable.reminder_icon)
        builder.setAutoCancel(true)
        builder.setChannelId("1001")
        return builder.build()
    }
}