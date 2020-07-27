package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.ItemCDBDTO
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.mapToItem
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.mapToItemDTO
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemSaveResult
import com.huawei.references.hinotes.data.item.model.ItemType
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ItemDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?,
                            private val permissionsDataSource: PermissionsDataSource
) : ItemDataSource {

    override suspend fun getItemById(itemId: Int): DataHolder<Item> =
        suspendCoroutine{ continuation ->
            cloudDBZone?.let {
                val query = CloudDBZoneQuery.where(ItemCDBDTO::class.java).apply {
                    equalTo("itemId", itemId)
                }
                val queryTask = it.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_PRIOR
                ).apply {
                    addOnSuccessListener{snapShot->
                        if(snapShot.snapshotObjects.size()==0){
                            continuation.resume(DataHolder.Fail(baseError = NoRecordFoundError()))
                        }
                        else{
                            continuation.resume(DataHolder.Success(snapShot.snapshotObjects[0].mapToItem()))
                        }
                    }
                    addOnFailureListener{
                        it.printStackTrace()
                        continuation.resume(DataHolder.Fail(errStr = it.localizedMessage
                            ?: DataConstants.DEFAULT_ERROR_STR))
                    }
                }
            }
        }

    override suspend fun getItemByIds(itemIds: List<Int>,itemType: ItemType): DataHolder<List<Item>> =
        suspendCoroutine{ continuation->
            cloudDBZone?.let {
                val query = CloudDBZoneQuery.where(ItemCDBDTO::class.java).apply {
                    val array : Array<out Int> = itemIds.toTypedArray()
                    `in`("itemId", array)
                }
                it.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
                ).apply {
                    addOnSuccessListener{snapShot->
                        if(snapShot.snapshotObjects.size()==0){
                            snapShot.release()
                            continuation.resume(DataHolder.Fail(baseError = NoRecordFoundError()))
                        }
                        else{
                            val itemList= mutableListOf<Item>()
                            while (snapShot.snapshotObjects.hasNext()){
                                itemList.add(snapShot.snapshotObjects.next().mapToItem())
                            }
                            snapShot.release()
                            continuation.resume(DataHolder.Success(itemList))
                        }
                    }
                    addOnFailureListener{
                        continuation.resume(DataHolder.Fail(errStr = it.localizedMessage
                            ?: DataConstants.DEFAULT_ERROR_STR))
                    }
                }
            }
        }

    override suspend fun getItemsByUserId(userId: String,itemType: ItemType): DataHolder<List<Item>> =
        try {
            when (val permissionsResult = permissionsDataSource.getPermissions(userId)) {
                is DataHolder.Success -> {
                    when (val itemsResult =
                        getItemByIds(permissionsResult.data.map {
                            it.itemId
                        },itemType)) {
                        is DataHolder.Success -> {
                            DataHolder.Success(itemsResult.data.map {
                                it
                            })
                        }
                        is DataHolder.Fail -> {
                            (itemsResult.baseError as? NoRecordFoundError)?.let {
                                // no record found. Still success with zero items
                                DataHolder.Success(listOf<Item>())
                            } ?: let {
                                permissionsResult as DataHolder.Fail
                            }
                        }
                        is DataHolder.Loading -> itemsResult as DataHolder.Loading
                    }
                }
                is DataHolder.Fail -> {
                    (permissionsResult.baseError as? NoRecordFoundError)?.let {
                        // no record found. Still success with zero items
                        DataHolder.Success(listOf<Item>())
                    } ?: let {
                        permissionsResult as DataHolder.Fail
                    }
                }
                is DataHolder.Loading -> permissionsResult as DataHolder.Loading
            }
        }
        catch (e:Exception){
            DataHolder.Fail(errStr = e.message ?: DataConstants.DEFAULT_ERROR_STR)
        }


    override suspend fun upsertItem(
        item: Item,
        userId: String,
        isNew: Boolean,
        subItemIdsToDelete: List<Int>,
        reminderIdsToDelete: List<Int>
    ): DataHolder<ItemSaveResult> {
        try {
            var itemSize=0
            var lastItemId=0
            // getting lastItemId and itemSize
            when(val res=getItemsByUserId(userId,item.type)){
                is DataHolder.Success -> {
                    if(res.data.isNotEmpty()){
                        itemSize=res.data.size
                        lastItemId=res.data.last().itemId
                    }
                }
                is DataHolder.Fail -> {
                    (res.baseError as? NoRecordFoundError)?.let {
                        // no record found. Still success with zero items
                        0
                    } ?: let {
                        return res
                    }
                }
                is DataHolder.Loading -> res
            }
            val itemToInsert=if(isNew){
                item.mapToItemDTO().apply {
                    itemId=lastItemId+1
                }
            }
            else item.mapToItemDTO()

            cloudDBZone?.executeUpsert(itemToInsert)?.apply {
                await()
            }?.result?.let {updateResultItemSize->
                if(isNew){
                    if(updateResultItemSize-itemSize==1){
                        return DataHolder.Success(ItemSaveResult(1))
                    }
                    //item size not increased
                    else DataHolder.Fail()
                }
                else{
                    return DataHolder.Success(ItemSaveResult(1))
                }
            }?: kotlin.run {
                return DataHolder.Fail()
            }
        }
        catch (e:Exception){
            return DataHolder.Fail(errStr = e.message ?: DataConstants.DEFAULT_ERROR_STR)
        }
        return DataHolder.Fail()
    }

    override suspend fun deleteItem(item: Item, userId: String): DataHolder<Any> =
        try {
            var itemSize=0
            // getting lastItemId and itemSize
            when(val res=getItemsByUserId(userId,item.type)){
                is DataHolder.Success -> {
                    if(res.data.isNotEmpty()){
                        itemSize=res.data.size
                    }
                }
                is DataHolder.Fail -> {
                    (res.baseError as? NoRecordFoundError)?.let {
                        // no record found. Still success with zero items
                        0
                    } ?: let {
                        return res
                    }
                }
                is DataHolder.Loading -> {
                    res
                }
            }

            cloudDBZone?.executeUpsert(item.mapToItemDTO())?.apply {
                await()
            }?.result?.let {updateResultItemSize->
                if(itemSize-updateResultItemSize==1){
                    DataHolder.Success(Any())
                }
                else{
                    DataHolder.Fail()
                }
            }?: kotlin.run {
                DataHolder.Fail()
            }
        }
        catch (e:Exception){
            DataHolder.Fail(errStr = e.message ?: DataConstants.DEFAULT_ERROR_STR)
        }

    override suspend fun deleteItems(items: List<Item>, userId: String): DataHolder<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun checkUncheckTodoItem(
        userId: String,
        item: Item,
        isChecked: Boolean
    ): DataHolder<Any> {
        TODO("Not yet implemented")
    }

}