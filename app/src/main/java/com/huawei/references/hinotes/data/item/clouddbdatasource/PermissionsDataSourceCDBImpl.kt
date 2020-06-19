package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.wooz.hinotes.data.DataConstants
import com.wooz.hinotes.data.base.DataHolder
import com.wooz.hinotes.data.base.NoRecordFoundError
import com.wooz.hinotes.data.item.abstractions.PermissionsDataSource
import com.wooz.hinotes.data.item.model.Permission
import com.wooz.hinotes.data.item.model.PermissionDTO
import com.wooz.hinotes.data.item.model.mapToPermission
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionsDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?) :
    PermissionsDataSource {

    override suspend fun getPermissions(userId: String): DataHolder<List<Permission>> {
        return suspendCoroutine<DataHolder<List<Permission>>> {continuation ->
            cloudDBZone?.let {
                val query = CloudDBZoneQuery.where(PermissionDTO::class.java).apply {
                    equalTo("userId", userId)
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
                            val permissionList= mutableListOf<Permission>()
                            while (snapShot.snapshotObjects.hasNext()){
                                permissionList.add(snapShot.snapshotObjects.next().mapToPermission())
                            }
                            snapShot.release()
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
