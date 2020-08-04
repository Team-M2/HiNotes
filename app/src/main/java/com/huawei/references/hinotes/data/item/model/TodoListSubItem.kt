package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class TodoListSubItem (
    var id:Int=-1,
    val itemId:Int=-1,
    val createdAt:Date?=null,
    var updatedAt: Date?=null,
    var title:String,
    var isChecked:Boolean=false
) :Serializable

