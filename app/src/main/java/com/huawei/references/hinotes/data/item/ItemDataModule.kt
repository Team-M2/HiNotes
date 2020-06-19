package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.item.abstractions.*
import com.huawei.references.hinotes.data.item.clouddbdatasource.*
import org.koin.dsl.module

val itemDataModule = module{

    factory {
        ItemRepository(get(),get(),get(),get())
    }

    single {
        ItemsLiveDataSourceCDBImpl(get()) as ItemsLiveDataSource
    }

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