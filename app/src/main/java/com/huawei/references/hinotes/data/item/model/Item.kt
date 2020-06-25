package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class Item (
    val itemId:Int=-1,
    val createdAt:Date?=Date(),
    val updatedAt: Date?=Date(),
    val type:ItemType,
    val isOpen:Boolean=false,
    val lat:Double?=null,
    val lng:Double?=null,
    var poiDescription:String?=null,
    var title:String,
    val todoListSubItems:List<TodoListSubItem>?=null,
    val isChecked:Boolean?=false,
    val isPinned:Boolean?=false
) :Serializable

