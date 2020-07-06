package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType

interface ItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<Item>

    suspend fun getItemByIds(itemIds: List<Int>,itemType: ItemType): DataHolder<List<Item>>

    suspend fun getItemsByUserId(userId: String,itemType: ItemType) : DataHolder<List<Item>>

    suspend fun upsertItem(item: Item, userId: String, isNew:Boolean) : DataHolder<Any>

    suspend fun deleteItem(item: Item, userId: String) : DataHolder<Any>

    suspend fun deleteItems(items: List<Item>, userId: String) : DataHolder<Any>

}