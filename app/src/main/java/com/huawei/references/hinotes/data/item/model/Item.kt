package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class Item (
    val itemId:Int=-1,
    val createdAt:Date?=null,
    val updatedAt: Date?=null,
    val type:ItemType,
    val isOpen:Boolean=false,
    val lat:Double?=null,
    val lng:Double?=null,
    var poiDescription:String?=null,
    var title:String,
    val todoListSubItems:List<TodoListSubItem>?=listOf(),
    val isChecked:Boolean?=false,
    val isPinned:Boolean?=false
) :Serializable

