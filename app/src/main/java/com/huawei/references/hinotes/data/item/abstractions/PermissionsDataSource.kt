package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Permission
import com.huawei.references.hinotes.data.item.model.UserRole

interface PermissionsDataSource {
    suspend fun getPermissions(userId: String): DataHolder<List<Permission>>

    suspend fun addPermission(userId: String,itemId: Int,role: UserRole): DataHolder<Any>
}