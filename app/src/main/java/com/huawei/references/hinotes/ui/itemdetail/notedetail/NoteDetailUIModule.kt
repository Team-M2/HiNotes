package com.huawei.references.hinotes.ui.itemdetail.notedetail

import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val noteDetailUIModule= module {
    viewModel{
        DetailNoteViewModel(get(),get(),get())
    } bind ItemDetailViewModel::class
}