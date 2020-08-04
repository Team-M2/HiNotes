package com.huawei.references.hinotes.data.item.model

enum class ItemType(val type:Int) {
    Note(0),
    TodoList(1);

    companion object {
        fun valueOf(value: Int) = ItemType.values().find { it.type == value }
    }
}