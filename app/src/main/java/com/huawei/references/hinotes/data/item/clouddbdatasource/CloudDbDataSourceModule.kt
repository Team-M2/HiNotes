package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.references.hinotes.data.item.abstractions.DeleteItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.UpsertItemDataSource
import org.koin.dsl.module

val cloudDbDataSourceModule = module {
    factory {
        GetItemDataSourceCDBImpl(
            get(),
            get()
        ) as GetItemDataSource
    }

    factory {
        UpsertItemDataSourceCDBImpl(
            get(),
            get(),
            get()
        ) as UpsertItemDataSource
    }

    factory {
        PermissionsDataSourceCDBImpl(
            get()
        ) as PermissionsDataSource
    }

    factory {
        DeleteItemDataSourceCDBImpl(
            get(),get(),get()
        ) as DeleteItemDataSource
    }
}