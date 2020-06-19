package com.wooz.notes.ui.todolist

import com.wooz.notes.ui.notes.NotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val todoListUIModule= module {
    viewModel {
        ToDoListsViewModel(get())
    }
}