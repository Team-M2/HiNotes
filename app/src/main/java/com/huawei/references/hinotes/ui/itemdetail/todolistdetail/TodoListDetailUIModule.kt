package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val todoListDetailUIModule= module {

    viewModel() {
        TodoListDetailViewModel(get(),get(),get())
    } bind ItemDetailViewModel::class
}