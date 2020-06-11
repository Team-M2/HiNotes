package com.huawei.references.hinotes.data

import android.content.Context
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.note.noteDataModule
import com.huawei.references.hinotes.ui.todolist.todoListUIModule
import org.koin.dsl.module

val dataCoreModule = module {

    single {
        val context : Context = get()
        AGConnectCloudDB.initialize(context)
        AGConnectCloudDB.getInstance().apply {
            openCloudDBZone(
                CloudDBZoneConfig(
                    DataConstants.DB_ZONE_NAME,
                    CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                    CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC),
                true)
        }
    }
}

val dataModule = dataCoreModule + noteDataModule