package com.wooz.notes.data.item

import com.wooz.notes.data.item.abstractions.DeleteItemDataSource
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.abstractions.PermissionsDataSource
import com.wooz.notes.data.item.abstractions.UpsertItemDataSource
import com.wooz.notes.data.item.clouddbdatasource.DeleteItemDataSourceCDBImpl
import com.wooz.notes.data.item.clouddbdatasource.GetItemDataSourceCDBImpl
import com.wooz.notes.data.item.clouddbdatasource.PermissionsDataSourceCDBImpl
import com.wooz.notes.data.item.clouddbdatasource.UpsertItemDataSourceCDBImpl
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