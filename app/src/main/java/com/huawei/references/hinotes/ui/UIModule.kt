package com.huawei.references.hinotes.ui

import com.huawei.references.hinotes.ui.notedetail.noteDetailUIModule
import com.huawei.references.hinotes.ui.notes.notesUIModule
import com.huawei.references.hinotes.ui.todolist.todoListUIModule
import org.koin.dsl.module

val uiCoreModule = module {

}

val uiModule = uiCoreModule + notesUIModule + todoListUIModule + noteDetailUIModule

