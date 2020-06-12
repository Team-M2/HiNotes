package com.huawei.references.hinotes.data.note

import android.content.Context
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.DataConstants.Companion.DB_ZONE_NAME
import org.koin.dsl.module

val noteDataModule = module{

    factory {
        NoteRepository(get(),get())
    }

}