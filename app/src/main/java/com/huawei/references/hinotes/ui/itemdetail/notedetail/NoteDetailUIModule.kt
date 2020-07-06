package com.huawei.references.hinotes.ui.itemdetail.notedetail

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteDetailUIModule= module {

    viewModel {
        DetailNoteViewModel(get())
    }
}