package com.huawei.references.hinotes.data.item.model

import com.huawei.hms.maps.model.LatLng
import java.util.*

data class Reminder(
    var id:Int=-1,
    var itemId:Int=-1,
    var title:String?=null,
    var location:LatLng?=null,
    var radius:Double?=null,
    var date:Date?=null,
    var reminderType:ReminderType
)