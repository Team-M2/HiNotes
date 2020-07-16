package com.huawei.references.hinotes.data.item.model

enum class ReminderType(val type:Int) {
    ByGeofence(0),
    ByTime(1);

    companion object {
        fun valueOf(value: Int) = ReminderType.values().find { it.type == value }
    }
}