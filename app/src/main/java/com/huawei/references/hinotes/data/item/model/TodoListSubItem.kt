package com.huawei.references.hinotes.data.item.model

import java.io.Serializable
import java.util.*

data class TodoListSubItem (
    val itemId:Int,
    val createdAt:Date?,
    val updatedAt: Date?,
    var title:String,
    var isChecked:Boolean
) :Serializable

