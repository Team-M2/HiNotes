package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.ReminderDataSource
import com.huawei.references.hinotes.data.item.model.Reminder

class ReminderRepository(private val reminderDataSource: ReminderDataSource)  {

    suspend fun getRemindersByItemId(itemId:Int) : DataHolder<List<Reminder>> =
        reminderDataSource.getRemindersByItemId(itemId)
}