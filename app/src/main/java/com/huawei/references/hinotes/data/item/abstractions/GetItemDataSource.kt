package com.wooz.notes.data.item.abstractions

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item

interface GetItemDataSource {

    suspend fun getItemById(itemId: Int): DataHolder<Item>

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<Item>>

    suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>>
}