package com.huawei.references.hinotes.data

import android.content.Context
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.note.ItemDataSource
import com.huawei.references.hinotes.data.note.NoteRepository
import com.huawei.references.hinotes.data.note.noteDataModule
import com.huawei.references.hinotes.ui.todolist.todoListUIModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataCoreModule = module {

    single {
        val context : Context = get()
        AGConnectCloudDB.initialize(context)
        AGConnectCloudDB.getInstance()
    }

    factory {
        val agConnectCloudDBInstance : AGConnectCloudDB = get()
        openDbZone(agConnectCloudDBInstance)
    }

}

fun openDbZone(agConnectCloudDBInstance : AGConnectCloudDB) : CloudDBZone? =
    try {
        agConnectCloudDBInstance.openCloudDBZone(
            CloudDBZoneConfig(
                DataConstants.DB_ZONE_NAME,
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC).apply {
                persistenceEnabled=true
            },
            true)
    }
    catch (e:Exception){
        e.printStackTrace()
        null
    }


val dataModule = dataCoreModule + noteDataModule