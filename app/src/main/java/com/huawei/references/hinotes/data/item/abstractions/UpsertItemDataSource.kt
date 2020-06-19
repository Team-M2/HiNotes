package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item

interface UpsertItemDataSource {
    suspend fun upsertItem(item: Item, userId: String, isNew:Boolean) : DataHolder<Any>
}