package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.pick_hour_fragment.*
import kotlinx.android.synthetic.main.pick_hour_fragment.view.*
import java.util.*

class HourFragment(var item: Item):Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        time_picker.setOnTimeChangedListener { view, hourOfDay, minute ->
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.MINUTE, minute)
        }

        view.time_picker.apply {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pick_hour_fragment, container, false)
    }
}