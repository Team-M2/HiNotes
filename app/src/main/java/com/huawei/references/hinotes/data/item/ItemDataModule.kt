package com.huawei.references.hinotes.data.item

import org.koin.dsl.module

val itemDataModule = module{

    factory {
        ItemRepository(get())
    }

    factory {
        SubItemRepository(get())
    }

    factory {
        ReminderRepository(get())
    }

}