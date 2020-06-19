package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item

interface DeleteItemDataSource {
    suspend fun deleteItem(item: Item, userId: String) : DataHolder<Any>
}