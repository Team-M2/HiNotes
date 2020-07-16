package com.huawei.references.hinotes.data.item.restdatasource.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class ReminderRestDTO(@SerializedName ("id") var id:Int?=-1,
                           @SerializedName ("itemId") var itemId:Int=-1,
                           @SerializedName ("title") var title:String?=null,
                           @SerializedName ("lat") var lat: Double?=null,
                           @SerializedName ("lng") var lng: Double?=null,
                           @SerializedName ("radius") var radius:Double?=null,
                           @SerializedName ("date") var date: Date?=null,
                           @SerializedName ("reminderType") var reminderType: Int?=null)