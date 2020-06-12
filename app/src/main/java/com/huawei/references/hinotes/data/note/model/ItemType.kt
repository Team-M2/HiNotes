package com.huawei.references.hinotes.data.note.model

enum class ItemType(val type:Int) {
    Note(0),
    TodoList(1);

    companion object {
        fun valueOf(value: Int) = ItemType.values().find { it.type == value }
    }
}