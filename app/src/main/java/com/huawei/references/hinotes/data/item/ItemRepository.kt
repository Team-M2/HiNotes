package com.huawei.references.hinotes.data.item

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.LiveDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemSaveResult
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.SubscriptionParam

class ItemRepository(
    private val itemDataSource: ItemDataSource,
    liveDataSource: LiveDataSource<Item>
) : LiveRepository<Item>(liveDataSource) {

    override suspend fun getItems(userId: String, itemType: ItemType): DataHolder<List<Item>> =
        itemDataSource.getItemsByUserId(userId, itemType)

    fun subscribe(subscriptionParam: SubscriptionParam) =
        liveDataSource.subscribe(subscriptionParam)

    fun unSubscribe(): Boolean = liveDataSource.unsubscribe()

    suspend fun upsertItem(item: Item,
                           userId: String,
                           isNew: Boolean,
                           subItemIdsToDelete: List<Int>,
                           reminderIdsToDelete: List<Int>): DataHolder<ItemSaveResult> =
        handleResult(itemDataSource.upsertItem(item, userId, isNew,subItemIdsToDelete,reminderIdsToDelete),userId,item.type)

    suspend fun deleteItem(item: Item, userId: String): DataHolder<Any> =
        handleResult(itemDataSource.deleteItem(item, userId), userId, item.type)

    suspend fun deleteItems(items: List<Item>, userId: String): DataHolder<Any> =
        handleResult(itemDataSource.deleteItems(items, userId), userId, items.first().type)

    suspend fun checkUncheckTodoItem(
        userId: String,
        item: Item,
        isChecked: Boolean
    ): DataHolder<Any> = handleResult(
        itemDataSource.checkUncheckTodoItem(userId, item, isChecked),
        userId, item.type
    )

}

