package com.wooz.notes.data.item.abstractions

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.model.Permission

interface PermissionsDataSource {
    suspend fun getPermissions(userId: String): DataHolder<List<Permission>>
}