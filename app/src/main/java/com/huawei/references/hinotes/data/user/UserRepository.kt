package com.huawei.references.hinotes.data.user

import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig

class UserRepository(private val agConnectCloudDB: AGConnectCloudDB) {

    fun createDbZone(){
        agConnectCloudDB.openCloudDBZone(CloudDBZoneConfig("zoneName",CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC),true)
    }

}