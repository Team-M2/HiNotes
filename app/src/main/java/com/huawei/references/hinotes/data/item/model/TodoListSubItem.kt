package com.wooz.notes.data.item.model

import java.io.Serializable
import java.util.*

data class TodoListSubItem (
    val itemId:Int,
    val createdAt:Date?,
    val updatedAt: Date?,
    val title:String,
    var isChecked:Boolean
) :Serializable

