package com.huawei.references.hinotes.data.item.restdatasource.model

import com.google.gson.annotations.SerializedName
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import java.util.*

data class ItemRestDTO(@SerializedName("itemId") var itemId:Int,
                       @SerializedName("createdAt") val createdAt: Date?,
                       @SerializedName("updatedAt") val updatedAt: Date?,
                       @SerializedName("type") val type:Int,
                       @SerializedName("isOpen") val isOpen:Boolean,
                       @SerializedName("lat") val lat:Double?,
                       @SerializedName("lng") val lng:Double?,
                       @SerializedName("poiDescription") val poiDescription:String?,
                       @SerializedName("title") val title:String,
                       @SerializedName("isChecked") val isChecked:Boolean?,
                       @SerializedName("isPinned") val isPinned:Boolean?,
                       @SerializedName("role") val role:Int?
) : CloudDBZoneObject()