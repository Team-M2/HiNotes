package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.LiveDataSource
import com.huawei.references.hinotes.data.item.model.ItemType
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

abstract class LiveRepository<T> (protected val liveDataSource: LiveDataSource<T>) {
    suspend fun <T : Any> handleResult(
        result: DataHolder<T>,
        userId: String, itemType: ItemType
    ): DataHolder<T> =
        when (result) {
            is DataHolder.Success -> {
                withContext(coroutineContext) {
                    async {
                        liveDataSource.setItems(DataHolder.Loading())
                        liveDataSource.setItems(getItems(userId, itemType))
                    }
                    result
                }
            }
            is DataHolder.Loading -> result
            is DataHolder.Fail -> result
        }

    abstract suspend fun getItems(userId: String,itemType: ItemType) : DataHolder<List<T>>
}