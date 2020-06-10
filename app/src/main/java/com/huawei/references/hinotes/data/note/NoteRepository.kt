package com.huawei.references.hinotes.data.note

import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.base.DataHolder

class NoteRepository(private val agConnectCloudDB: AGConnectCloudDB) {

    fun createDbZone(){
        agConnectCloudDB.openCloudDBZone(CloudDBZoneConfig("zoneName",CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC),true)
    }

}