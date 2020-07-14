package com.huawei.references.hinotes.data.item.restdatasource.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.LiveDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.SubscriptionParam


class LiveDataSourceRestImpl() : LiveDataSource<Item> {

    private var isSubscribing=false

    private val mutableLiveData=MutableLiveData<DataHolder<List<Item>>>()
    private val liveData : LiveData<DataHolder<List<Item>>>
        get() = mutableLiveData

    override fun subscribe(subscriptionParam: SubscriptionParam):
            LiveData<DataHolder<List<Item>>> {
        isSubscribing=true
        return liveData
    }

    override fun unsubscribe(): Boolean {
        isSubscribing=false
        return true
    }

    override suspend fun setItems(listDataHolder: DataHolder<List<Item>>) {
        if(isSubscribing)
            mutableLiveData.postValue(listDataHolder)
    }


}