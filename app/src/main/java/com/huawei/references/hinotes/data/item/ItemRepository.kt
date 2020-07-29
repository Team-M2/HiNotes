package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemSaveResult
import com.huawei.references.hinotes.data.item.model.ItemType

class ItemRepository(
    private val itemDataSource: ItemDataSource
)  {

    suspend fun getItems(userId: String, itemType: ItemType): DataHolder<List<Item>> =
        itemDataSource.getItemsByUserId(userId, itemType)

    suspend fun upsertItem(item: Item,
                           userId: String,
                           isNew: Boolean,
                           subItemIdsToDelete: List<Int>,
                           reminderIdsToDelete: List<Int>): DataHolder<ItemSaveResult> =
        itemDataSource.upsertItem(item, userId, isNew,subItemIdsToDelete,reminderIdsToDelete)

    suspend fun deleteItem(item: Item, userId: String): DataHolder<Any> =
        itemDataSource.deleteItem(item, userId)

    suspend fun deleteItems(items: List<Item>, userId: String): DataHolder<Any> =
        itemDataSource.deleteItems(items, userId)

    suspend fun checkUncheckTodoItem(
        userId: String,
        item: Item,
        isChecked: Boolean
    ): DataHolder<Any> =
        itemDataSource.checkUncheckTodoItem(userId, item, isChecked)

}

