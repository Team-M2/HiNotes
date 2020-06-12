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
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
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
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    UserRole.Owner
                ),
                Item(
                    "itemID3",
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    UserRole.Owner
                ),
                Item(
                    "itemID4",
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
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

