package com.wooz.notes.data.user

import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.wooz.notes.data.base.DataHolder

class UserRepository(private val agConnectCloudDB: AGConnectCloudDB) {

    fun createDbZone(){
        agConnectCloudDB.openCloudDBZone(CloudDBZoneConfig("zoneName",CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC),true)
    }

}