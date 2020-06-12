package com.huawei.references.hinotes.data.note.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import java.util.*

data class ItemDTO(val itemId:Int,
                   val createdAt: Date?,
                   val updatedAt: Date?,
                   val type:Int,
                   val isOpen:Boolean,
                   val lat:Double?,
                   val lng:Double?,
                   val poiDescription:String?,
                   val role:Int) : CloudDBZoneObject()