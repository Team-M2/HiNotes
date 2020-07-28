package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.pick_date_fragment.view.*
import java.util.*

class DateFragment:Fragment() {
    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.date_picker.minDate = System.currentTimeMillis() - 1000
        view.date_picker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            /*val calendar = Calendar.getInstance()
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear
            viewModel.data.value = calendar
             */
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.YEAR, year)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.MONTH, monthOfYear)
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