package com.huawei.references.hinotes.data.item.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey

class PermissionDTOBackup(@PrimaryKey val itemId:Int,
                          @PrimaryKey val userId:String,
                          @PrimaryKey val role:Int) : CloudDBZoneObject() {
}
