package com.huawei.references.hinotes.data.item.abstractions

import androidx.lifecycle.LiveData
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item

interface ItemsLiveDataSource {
    suspend fun getItemsLiveData() : LiveData<DataHolder<List<Item>>>
}