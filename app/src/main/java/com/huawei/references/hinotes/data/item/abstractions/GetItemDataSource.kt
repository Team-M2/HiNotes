package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item

interface GetItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<Item>

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<Item>>

    suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>>
}