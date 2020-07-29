package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.pick_date_fragment.view.*
import java.lang.String.format
import java.util.*
import android.text.format.DateFormat;

class DateFragment(var item:Item):Fragment() {
    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.date_picker.minDate = System.currentTimeMillis() - 1000
        view.date_picker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.YEAR, year)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.MONTH, monthOfYear)
        }
        view.date_picker.apply {
            minDate = System.currentTimeMillis() - 1000
            updateDate(Integer.parseInt(DateFormat.format("yyyy", item.reminder?.date).toString()),Integer.parseInt(DateFormat.format("MM", item.reminder?.date).toString()),Integer.parseInt(DateFormat.format("dd", item.reminder?.date).toString()))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pick_date_fragment, container, false)
    }
}