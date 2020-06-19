package com.huawei.references.hinotes.ui.notes

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notesUIModule= module {

    viewModel {
        NotesViewModel(get())
    }
}