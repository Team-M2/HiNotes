package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item

interface ItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<Item>

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<Item>>

    suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>>

    suspend fun upsertItem(item: Item, userId: String, isNew:Boolean) : DataHolder<Any>

    suspend fun deleteItem(item: Item, userId: String) : DataHolder<Any>

}