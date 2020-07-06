package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val todoListDetailUIModule= module {

    viewModel {
        TodoListDetailViewModel(get(),get())
    }
}