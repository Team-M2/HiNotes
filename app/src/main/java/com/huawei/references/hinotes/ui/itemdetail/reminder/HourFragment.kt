package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.pick_hour_fragment.*
import java.util.*

class HourFragment:Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        time_picker.setOnTimeChangedListener { view, hourOfDay, minute ->
           /* val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR] = hourOfDay
            calendar[Calendar.MINUTE] = minute
            viewModel.data.value = calendar

            */
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.HOUR, hourOfDay)
            ReminderByTimeFragment.reminderStaticCalendar?.set(Calendar.MINUTE, minute)
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