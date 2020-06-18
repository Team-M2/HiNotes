package com.wooz.notes.data.item.abstractions

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item

interface UpsertItemDataSource {
    suspend fun upsertItem(item: Item, userId: String, isNew:Boolean) : DataHolder<Any>
}