package com.huawei.references.hinotes.data.note

import android.provider.ContactsContract
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.note.model.Item
import com.huawei.references.hinotes.data.note.model.ItemType
import com.huawei.references.hinotes.data.note.model.UserRole
import java.util.*

class NoteRepository(private val agConnectCloudDB: AGConnectCloudDB) {

    suspend fun getNotes(userId: Int): DataHolder<List<Item>> {
        return DataHolder.Success(
            listOf(
                Item(
                    "itemID1",
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
                    "itemID2",
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

}