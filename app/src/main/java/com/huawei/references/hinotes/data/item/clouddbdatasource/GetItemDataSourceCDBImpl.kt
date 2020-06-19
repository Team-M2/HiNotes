package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemDTO
import com.huawei.references.hinotes.data.item.model.mapToItem
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetItemDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?,
                               private val permissionsDataSource: PermissionsDataSource
                     ) :
    GetItemDataSource {

    override suspend fun getItemById(itemId: Int): DataHolder<ItemDTO> {
        return suspendCoroutine<DataHolder<ItemDTO>> {continuation ->
            cloudDBZone?.let {
                val query = CloudDBZoneQuery.where(ItemDTO::class.java).apply {
                    equalTo("itemId", itemId)
                }
                val queryTask = it.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
                ).apply {
                    addOnSuccessListener{snapShot->
                        if(snapShot.snapshotObjects.size()==0){
                            continuation.resume(DataHolder.Fail(baseError = NoRecordFoundError()))
                        }
                        else{
                            continuation.resume(DataHolder.Success(snapShot.snapshotObjects[0]))
                        }
                    }
                    addOnFailureListener{
                        continuation.resume(DataHolder.Fail(errStr = it.localizedMessage
                            ?: DataConstants.DEFAULT_ERROR_STR))
                    }
                }
            }
        }
    }

    override suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<ItemDTO>> {
       return suspendCoroutine<DataHolder<List<ItemDTO>>> {continuation->
           cloudDBZone?.let {
               val query = CloudDBZoneQuery.where(ItemDTO::class.java).apply {
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
                           val itemList= mutableListOf<ItemDTO>()
                           while (snapShot.snapshotObjects.hasNext()){
                               itemList.add(snapShot.snapshotObjects.next())
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

    }

    override suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>> =
        try {
            when (val permissionsResult = permissionsDataSource.getPermissions(userId)) {
                is DataHolder.Success -> {
                    when (val itemsResult =
                        getItemByIds(permissionsResult.data.map {
                            it.itemId
                        })) {
                        is DataHolder.Success -> {
                            DataHolder.Success(itemsResult.data.map {
                                it.mapToItem()
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

}
