package com.wooz.notes.ui

import com.wooz.notes.ui.notes.notesUIModule
import com.wooz.notes.ui.todolist.todoListUIModule
import org.koin.dsl.module

val uiCoreModule = module {

}

val uiModule = uiCoreModule + notesUIModule + todoListUIModule

