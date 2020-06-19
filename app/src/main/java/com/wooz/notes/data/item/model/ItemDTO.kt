package com.wooz.notes.data.item.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey
import java.util.*

data class ItemDTO(@PrimaryKey var itemId:Int,
                   val createdAt: Date?,
                   val updatedAt: Date?,
                   val type:Int,
                   val isOpen:Boolean,
                   val lat:Double?,
                   val lng:Double?,
                   val poiDescription:String?,
                   val title:String,
                   val todoListSubItems:List<TodoListSubItem>?,
                   val isChecked:Boolean?,
                   val role:Int,
                   val isPinned:Boolean
) : CloudDBZoneObject()