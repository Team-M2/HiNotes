package com.huawei.references.hinotes.data.item.clouddbdatasource.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey
import com.huawei.references.hinotes.data.item.model.Reminder
import java.util.*

data class ItemCDBDTO(@PrimaryKey
                      var itemId:Int,
                      val createdAt: Date?,
                      val updatedAt: Date?,
                      val type:Int,
                      val isOpen:Boolean,
                      val lat:Double?,
                      val lng:Double?,
                      val poiName:String?,
                      val poiDescription:String?,
                      val title:String,
                      val isChecked:Boolean?,
                      val reminder:Reminder?,
                      val isPinned:Boolean?
) : CloudDBZoneObject()