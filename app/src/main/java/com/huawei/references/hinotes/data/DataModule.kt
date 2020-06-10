package com.huawei.references.hinotes.data

import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import org.koin.core.module.Module
import org.koin.dsl.module


val dataModule = module{
    single {
        AGConnectCloudDB.getInstance().apply {
            AGConnectCloudDB.initialize(get())
        }
    }
}