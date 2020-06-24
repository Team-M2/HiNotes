package com.huawei.references.hinotes.data.item.abstractions

import androidx.lifecycle.LiveData
import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item

interface ItemsLiveDataSource {
    suspend fun getItemsLiveData() : LiveData<DataHolder<List<Item>>>
}