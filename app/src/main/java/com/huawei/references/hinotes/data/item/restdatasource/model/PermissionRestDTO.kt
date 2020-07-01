package com.huawei.references.hinotes.data.item.restdatasource.model

import com.google.gson.annotations.SerializedName

data class PermissionRestDTO(@SerializedName("itemId") val itemId: Int? = null,
                             @SerializedName("userId") val userId: String? = null,
                             @SerializedName("role") val role: Int? = null) {
    


}