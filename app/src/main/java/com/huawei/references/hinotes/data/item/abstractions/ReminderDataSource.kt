package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Reminder

interface ReminderDataSource {
    
    suspend fun getRemindersByItemId(itemId:Int) : DataHolder<List<Reminder>>

    suspend fun upsert(reminder: Reminder, itemId:Int, isNew: Boolean) : DataHolder<Int>

    suspend fun deleteReminder(reminderId: Int) : DataHolder<Any>

    suspend fun deleteReminders(reminderIds: List<Int>) : DataHolder<Any>

    suspend fun insertMultiple(reminderList:List<Reminder>, itemId:Int) : DataHolder<Any>

    suspend fun updateMultiple(reminderList: List<Reminder>, itemId: Int): DataHolder<Any>

}