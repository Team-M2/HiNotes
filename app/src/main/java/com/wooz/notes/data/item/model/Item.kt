package com.wooz.notes.data.item.model

import java.io.Serializable
import java.util.*

data class Item (
    val itemId:Int,
    val createdAt:Date?,
    val updatedAt: Date?,
    val type:ItemType,
    val isOpen:Boolean,
    val lat:Double?,
    val lng:Double?,
    val poiDescription:String?,
    val title:String,
    val todoListSubItems:List<TodoListSubItem>?,
    val isChecked:Boolean?,
    val role:UserRole,
    val isPinned:Boolean
) :Serializable

