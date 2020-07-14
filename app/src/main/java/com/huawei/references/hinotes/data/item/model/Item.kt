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
    val poiName:String?=null,
    var poiDescription:String?=null,
    var title:String,
    val todoListSubItems:MutableList<TodoListSubItem>?= mutableListOf(),
    var isChecked:Boolean?=false,
    val role: UserRole?=null,
    var isPinned:Boolean?=false
) :Serializable

