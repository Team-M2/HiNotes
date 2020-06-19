package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.item.abstractions.DeleteItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.UpsertItemDataSource
import com.huawei.references.hinotes.data.item.clouddbdatasource.DeleteItemDataSourceCDBImpl
import com.huawei.references.hinotes.data.item.clouddbdatasource.GetItemDataSourceCDBImpl
import com.huawei.references.hinotes.data.item.clouddbdatasource.PermissionsDataSourceCDBImpl
import com.huawei.references.hinotes.data.item.clouddbdatasource.UpsertItemDataSourceCDBImpl
import org.koin.dsl.module

val itemDataModule = module{

    factory {
        ItemRepository(get(),get(),get())
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