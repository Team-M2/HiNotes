package com.huawei.references.hinotes.data.item.clouddbdatasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.OnSnapshotListener
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.LiveDataSource
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.ItemCDBDTO
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.mapToItem
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.SubscriptionParam


class LiveDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?) : LiveDataSource<Item> {

    override fun subscribe(subscriptionParam: SubscriptionParam): LiveData<DataHolder<List<Item>>> {
        val mutableLiveData=MutableLiveData<DataHolder<List<Item>>>()
        cloudDBZone?.let {
            val mSnapshotListener: OnSnapshotListener<ItemCDBDTO> =
                OnSnapshotListener<ItemCDBDTO> { cloudDBZoneSnapshot, e ->
                    e?.let {
                        Log.w(TAG, "onSnapshot: " + e.message)
                        mutableLiveData.postValue(DataHolder.Fail(errStr = it.message ?:
                            DataConstants.DEFAULT_ERROR_STR))
                        return@OnSnapshotListener
                    }
                    val items: MutableList<Item> = ArrayList()
                    try {
                        cloudDBZoneSnapshot.snapshotObjects?.let {
                            while (it.hasNext()) {
                                items.add(it.next().mapToItem())
                            }
                        }
                        mutableLiveData.postValue(DataHolder.Success(items))
                    } catch (snapshotException: AGConnectCloudDBException) {
                        Log.w(TAG, "onSnapshot:(getObject) " + snapshotException.message)
                        mutableLiveData.postValue(DataHolder.Fail(errStr = snapshotException.message ?:
                        DataConstants.DEFAULT_ERROR_STR))
                    } finally {
                        cloudDBZoneSnapshot.release()
                    }
                }
        }
        return mutableLiveData as LiveData<DataHolder<List<Item>>>
    }

    override fun unsubscribe(): Boolean {
        TODO("Not yet implemented")
    }

    companion object{
        const val TAG="liveDataListener"
    }

    override suspend fun setItems(listDataHolder: DataHolder<List<Item>>) {
        TODO("Not yet implemented")
    }
}