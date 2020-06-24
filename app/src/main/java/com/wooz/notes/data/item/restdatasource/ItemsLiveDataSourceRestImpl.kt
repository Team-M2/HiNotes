package com.wooz.notes.data.item.restdatasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huawei.references.hinotes.data.item.abstractions.ItemsLiveDataSource
import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Item


class ItemsLiveDataSourceRestImpl() : ItemsLiveDataSource {

    // not usable right now
    override suspend fun getItemsLiveData(): LiveData<DataHolder<List<Item>>> {
        val mutableLiveData=MutableLiveData<DataHolder<List<Item>>>()
        return mutableLiveData as LiveData<DataHolder<List<Item>>>
    }

    companion object{
        const val TAG="liveDataListener"
    }
}