package com.huawei.references.hinotes.data

import android.content.Context
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.item.itemDataModule
import com.huawei.references.hinotes.data.item.restdatasource.restDataModule
import org.koin.dsl.module

val dataCoreModule = module {

    single {
        val context : Context = get()
        AGConnectCloudDB.initialize(context)
        AGConnectCloudDB.getInstance()
    }

    single {
        val agConnectCloudDBInstance : AGConnectCloudDB = get()
        openDbZone(agConnectCloudDBInstance)
    }

}

fun openDbZone(agConnectCloudDBInstance : AGConnectCloudDB) : CloudDBZone? =
    try{
        agConnectCloudDBInstance.openCloudDBZone(
            CloudDBZoneConfig(
                DataConstants.DB_ZONE_NAME,
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC).apply {
                persistenceEnabled=true
            },
            true)
    }
    catch (e: Exception){
        e.printStackTrace()
        null
    }

// decide which dataSource module will be used
val dataModule = dataCoreModule + itemDataModule + restDataModule
//val dataModule = dataCoreModule + itemDataModule + cloudDbDataSourceModule