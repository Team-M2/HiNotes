package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemDTO

interface GetItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<ItemDTO>

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<ItemDTO>>

    suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>>
}