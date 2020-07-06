package com.huawei.references.hinotes.data.item

import org.koin.dsl.module

val itemDataModule = module{

    factory {
        ItemRepository(get(),get())
    }

    factory {
        SubItemRepository(get())
    }

}