package com.huawei.references.hinotes.data.item.abstractions

import androidx.lifecycle.LiveData
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.SubscriptionParam

interface LiveDataSource<T> {
    fun subscribe(subscriptionParam: SubscriptionParam) : LiveData<DataHolder<List<T>>>

    fun unsubscribe() : Boolean

    suspend fun setItems(listDataHolder: DataHolder<List<T>>)
}