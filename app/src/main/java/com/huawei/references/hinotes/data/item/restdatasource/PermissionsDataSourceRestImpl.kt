package com.huawei.references.hinotes.data.item.restdatasource

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.model.Permission
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.data.item.restdatasource.model.PermissionRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToPermission

class PermissionsDataSourceRestImpl(
    private val apiCallAdapter: ApiCallAdapter,
    private val itemRestService: ItemRestService
) :
    PermissionsDataSource {

    override suspend fun getPermissions(userId: String): DataHolder<List<Permission>> =
        apiCallAdapter.adapt<PermissionRestDTO> {
            val query = ""
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DataHolder.Success -> {
                    if (it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToPermission() })
                }
                is DataHolder.Fail -> it
                is DataHolder.Loading -> it
            }
        }

    override suspend fun addPermission(
        userId: String,
        itemId: Int,
        role: UserRole
    ): DataHolder<Any> {
        TODO("Not yet implemented")
    }


}
