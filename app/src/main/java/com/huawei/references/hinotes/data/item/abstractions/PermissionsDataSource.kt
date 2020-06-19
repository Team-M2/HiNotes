package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Permission

interface PermissionsDataSource {
    suspend fun getPermissions(userId: String): DataHolder<List<Permission>>
}