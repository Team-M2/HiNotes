package com.huawei.references.hinotes.ui.itemlist

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val itemListModule = module {
    viewModel {
        ItemListViewModel(get())
    }
}