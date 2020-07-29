package com.huawei.references.hinotes.ui

import com.huawei.references.hinotes.ui.itemdetail.notedetail.noteDetailUIModule
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.todoListDetailUIModule
import com.huawei.references.hinotes.ui.itemlist.itemListModule
import com.huawei.references.hinotes.ui.itemlist.todolist.todoListModule

val uiModule =
        itemListModule +
        noteDetailUIModule +
        todoListDetailUIModule +
        todoListModule
