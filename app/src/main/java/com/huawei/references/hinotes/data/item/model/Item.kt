package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class Item (
    var itemId:Int=-1,
    val createdAt:Date?=null,
    val updatedAt: Date?=null,
    val type:ItemType,
    val isOpen:Boolean=false,
    var lat:Double?=null,
    var lng:Double?=null,
    var poiName:String?=null,
    var poiDescription:String?=null,
    var description:String?=null,
    var title:String,
    val todoListSubItems:MutableList<TodoListSubItem>?= mutableListOf(),
    var isChecked:Boolean?=false,
    val role: UserRole?=null,
    var reminder: Reminder?=null,
    var isPinned:Boolean?=false
) :Serializable

