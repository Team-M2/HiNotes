package com.wooz.notes.data.item.abstractions

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item

interface DeleteItemDataSource {
    suspend fun deleteItem(item: Item, userId: String) : DataHolder<Any>
}