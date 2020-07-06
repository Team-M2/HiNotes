package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.SubItemDataSource
import com.huawei.references.hinotes.data.item.model.TodoListSubItem

class SubItemRepository(private val subItemDataSource: SubItemDataSource) {

    suspend fun getSubItemsByItemId(itemId:Int) : DataHolder<List<TodoListSubItem>> =
        subItemDataSource.getSubItemsByItemId(itemId)
}