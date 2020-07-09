package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.model.Permission
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.data.item.restdatasource.model.PermissionRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToPermission
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.DBResult
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService

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
                is DBResult.ResultList<PermissionRestDTO> ->{
                    DataHolder.Success(it.data.map { it.mapToPermission() })
                }
                is DBResult.EmptyQueryResult ->{
                    DataHolder.Fail(baseError = NoRecordFoundError())
                }
                else -> DataHolder.Fail()
            }
        }

    override suspend fun upsertPermission(
        userId: String,
        itemId: Int,
        role: UserRole,
        isNew: Boolean
    ): DataHolder<Any> =

        apiCallAdapter.adapt<PermissionRestDTO> {
            val query=
                if (isNew) "insert into hinotesschema.permission(\"userId\",\"itemId\",\"role\") values ($userId,$itemId,${role.role})"
                else "update hinotesschema.permission SET \"userId\"=$userId,\"itemId\"=$itemId,\"role\"=${role.role} WHERE \"userId\"=$userId AND \"itemId\"=$itemId"
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DBResult.EmptyQueryResult ->{
                    DataHolder.Success(Any())
                }
                else -> DataHolder.Fail()
            }
        }



}
