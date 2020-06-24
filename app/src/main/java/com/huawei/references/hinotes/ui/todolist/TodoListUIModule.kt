package com.huawei.references.hinotes.ui.todolist

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val todoListUIModule= module {
    viewModel {
        ToDoListsViewModel(get())
    }
}