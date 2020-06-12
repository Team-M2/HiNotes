package com.huawei.references.hinotes.data.note

import android.provider.ContactsContract
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.note.model.*
import java.util.*
import kotlin.coroutines.resume

class NoteRepository(
    private val agConnectCloudDB: AGConnectCloudDB,
    private val cloudDBZone: CloudDBZone
) {

    suspend fun getNotes(): DataHolder<List<Item>> {
        return DataHolder.Success(
            listOf(
                Item(
                    1,
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    null,
                    UserRole.Owner
                ),
                Item(
                    2,
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    null,
                    UserRole.Owner
                )
            )
        )
    }

    suspend fun getItems(userId: String): DataHolder<List<Item>> =

        try {
            when (val permissionsResult = PermissionsDataSource(cloudDBZone).getPermissions(userId)) {
                is DataHolder.Success -> {
                    when (val itemsResult =
                        ItemDataSource(cloudDBZone).getItemByIds(permissionsResult.data.map {
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

