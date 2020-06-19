package com.wooz.notes.data.item.abstractions

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.data.item.model.ItemDTO

interface GetItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<ItemDTO>

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<ItemDTO>>

    suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>>
}