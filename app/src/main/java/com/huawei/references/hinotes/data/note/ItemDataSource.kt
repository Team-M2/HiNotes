package com.huawei.references.hinotes.data.note

import com.huawei.agconnect.cloud.database.*
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.note.model.Item
import com.huawei.references.hinotes.data.note.model.ItemDTO
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ItemDataSource(private val cloudDBZone: CloudDBZone?) {

    suspend fun getItemById(itemId: Int): DataHolder<ItemDTO> {
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

    suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<ItemDTO>> {
       return suspendCoroutine<DataHolder<List<ItemDTO>>> {continuation->
           cloudDBZone?.let {
               val query = CloudDBZoneQuery.where(ItemDTO::class.java).apply {
                   val array : Array<out Int> = itemIds.toTypedArray()
                   `in`("itemId", array)
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
                           val itemList= mutableListOf<ItemDTO>()
                           while (snapShot.snapshotObjects.hasNext()){
                               itemList.add(snapShot.snapshotObjects.next())
                           }
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
}
