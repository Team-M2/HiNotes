package com.wooz.notes.data.item.restdatasource

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.base.NoRecordFoundError
import com.wooz.notes.data.item.abstractions.PermissionsDataSource
import com.wooz.notes.data.item.model.Permission
import com.wooz.notes.data.item.restdatasource.model.PermissionRestDTO
import com.wooz.notes.data.item.restdatasource.model.mapToPermission

class PermissionsDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                    private val itemRestService: ItemRestService) :
    PermissionsDataSource {

    override suspend fun getPermissions(userId: String): DataHolder<List<Permission>> =
        apiCallAdapter.adapt<PermissionRestDTO> {
            val query=""
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DataHolder.Success ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToPermission() })
                }
                is DataHolder.Fail -> it
                is DataHolder.Loading -> it
            }
        }

}
