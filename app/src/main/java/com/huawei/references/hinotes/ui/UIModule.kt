package com.huawei.references.hinotes.ui

import com.huawei.references.hinotes.ui.itemdetail.notedetail.noteDetailUIModule
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.todoListDetailUIModule
import com.huawei.references.hinotes.ui.itemlist.itemListModule
import com.huawei.references.hinotes.ui.itemlist.todolist.todoListModule
import org.koin.dsl.module

val uiCoreModule = module {

}

val uiModule = uiCoreModule +
        itemListModule +
        noteDetailUIModule +
        todoListDetailUIModule +
        todoListModule
