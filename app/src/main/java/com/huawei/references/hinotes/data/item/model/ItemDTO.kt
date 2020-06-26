package com.huawei.references.hinotes.data.item.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import java.util.*
import kotlin.collections.ArrayList

data class ItemDTO(var itemId:Int,
                   val createdAt: Date?,
                   val updatedAt: Date?,
                   val type:Int,
                   val isOpen:Boolean,
                   val lat:Double?,
                   val lng:Double?,
                   val poiDescription:String?,
                   val title:String,
                   val todoListSubItems:ArrayList<TodoListSubItem>?,
                   val isChecked:Boolean?,
                   val role:Int,
                   val isPinned:Boolean
) : CloudDBZoneObject()