package com.huawei.references.hinotes.data.note

import com.huawei.agconnect.cloud.database.*
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.note.model.Item
import com.huawei.references.hinotes.data.note.model.ItemDTO
import com.huawei.references.hinotes.data.note.model.PermissionDTO
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionsDataSource(private val cloudDBZone: CloudDBZone?) {

    suspend fun getPermissions(userId: String): DataHolder<List<PermissionDTO>> {
        return suspendCoroutine<DataHolder<List<PermissionDTO>>> {continuation ->
            cloudDBZone?.let {
                val query = CloudDBZoneQuery.where(PermissionDTO::class.java).apply {
                    equalTo("userId", userId)
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
                            val permissionList= mutableListOf<PermissionDTO>()
                            while (snapShot.snapshotObjects.hasNext()){
                                permissionList.add(snapShot.snapshotObjects.next())
                            }
                            continuation.resume(DataHolder.Success(permissionList))
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
