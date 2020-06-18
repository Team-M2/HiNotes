package com.wooz.notes.ui.notes

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notesUIModule= module {

    viewModel {
        NotesViewModel(get())
    }
}