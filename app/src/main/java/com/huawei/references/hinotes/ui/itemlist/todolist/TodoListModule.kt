package com.huawei.references.hinotes.ui.itemlist.todolist

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val todoListModule= module {
    viewModel {
        TodoListViewModel(get())
    }
}