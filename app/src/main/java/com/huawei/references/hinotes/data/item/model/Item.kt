package com.huawei.references.hinotes.data.item.model

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
    val role:UserRole,
    val isPinned:Boolean
) :Serializable

