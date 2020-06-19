package com.huawei.references.hinotes.data.item.abstractions

import com.wooz.hinotes.data.base.DataHolder
import com.wooz.hinotes.data.item.model.Item

interface UpsertItemDataSource {
    suspend fun upsertItem(item: Item, userId: String, isNew:Boolean) : DataHolder<Any>
}