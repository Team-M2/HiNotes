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
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    UserRole.Owner,
                    false
                ),
                Item(
                    "itemID2",
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    UserRole.Owner,
                    true
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
                    ,false
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
                    UserRole.Owner,
                    false
                ),
                Item(
                    "itemID5",
                    Date(),
                    Date(),
                    ItemType.Note,
                    false,
                    42.4,
                    27.3,
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    UserRole.Owner,
                    false
                )
            )
        )
    }

}