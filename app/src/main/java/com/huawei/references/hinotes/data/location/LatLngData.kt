package com.huawei.references.hinotes.data.location

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LatLngData (@SerializedName("lat") val lat: Double,
                       @SerializedName("lng") val lng: Double) : Serializable