package com.wooz.notes.data.item.model

import com.huawei.references.hinotes.data.item.model.TodoListSubItem
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
    val isPinned:Boolean?
) :Serializable

