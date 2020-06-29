package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class TodoListSubItem (
    val id:Int=-1,
    val itemId:Int=-1,
    val createdAt:Date?=null,
    val updatedAt: Date?=null,
    val title:String,
    var isChecked:Boolean=false
) :Serializable

