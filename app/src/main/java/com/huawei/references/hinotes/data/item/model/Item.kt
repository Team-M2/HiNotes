package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Item (
    val itemId:Int,
    val createdAt:Date?,
    val updatedAt: Date?,
    val type:ItemType,
    val isOpen:Boolean,
    val lat:Double?,
    val lng:Double?,
    var poiDescription:String?,
    var title:String,
    var todoListSubItems:ArrayList<TodoListSubItem> = arrayListOf(),
    var isChecked:Boolean?,
    val role:UserRole,
    val isPinned:Boolean
) :Serializable

