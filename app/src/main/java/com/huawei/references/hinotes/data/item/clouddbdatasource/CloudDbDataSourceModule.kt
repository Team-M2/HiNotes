package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import org.koin.dsl.module

val cloudDbDataSourceModule = module {
    factory {
        ItemDataSourceCDBImpl(
            get(),
            get()
        ) as ItemDataSource
    }

    factory {
        PermissionsDataSourceCDBImpl(
            get()
        ) as PermissionsDataSource
    }

}