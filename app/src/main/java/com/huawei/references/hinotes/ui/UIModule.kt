package com.huawei.references.hinotes.ui

import com.wooz.hinotes.ui.notes.notesUIModule
import com.wooz.hinotes.ui.todolist.todoListUIModule
import org.koin.dsl.module

val uiCoreModule = module {

}

val uiModule = uiCoreModule + notesUIModule + todoListUIModule

