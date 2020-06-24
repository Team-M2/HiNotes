package com.wooz.notes.data.item

import org.koin.dsl.module

val itemDataModule = module{

    factory {
        ItemRepository(get(),get(),get(),get())
    }

}