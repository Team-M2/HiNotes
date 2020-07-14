package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.LiveDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.model.Item
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

    factory {
        LiveDataSourceCDBImpl(get()) as LiveDataSource<Item>
    }

}