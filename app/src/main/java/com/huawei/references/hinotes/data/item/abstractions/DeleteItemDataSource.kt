package com.huawei.references.hinotes.data.item.abstractions

import com.wooz.hinotes.data.base.DataHolder
import com.wooz.hinotes.data.item.model.Item

interface DeleteItemDataSource {
    suspend fun deleteItem(item: Item, userId: String) : DataHolder<Any>
}