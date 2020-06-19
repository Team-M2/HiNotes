package com.huawei.references.hinotes.data.item

import androidx.lifecycle.LiveData
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.DeleteItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.ItemsLiveDataSource
import com.huawei.references.hinotes.data.item.abstractions.UpsertItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import java.util.*

class ItemRepository(private val getItemDataSource: GetItemDataSource,
                     private val upsertItemDataSource: UpsertItemDataSource,
                     private val deleteItemDataSource: DeleteItemDataSource,
                     private val itemsLiveDataSource: ItemsLiveDataSource
                     ) {

    suspend fun getNotesDummy(): DataHolder<List<Item>> {
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
                    UserRole.Owner,
                    false
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
                    UserRole.Owner,
                    true
                ),
                Item(
                    3,
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
                    4,
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
                    5,
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

    suspend fun getItems(userId: String): DataHolder<List<Item>> =
        getItemDataSource.getItemsByUserId(userId)

    suspend fun getItemsLiveData() : LiveData<DataHolder<List<Item>>> =
        itemsLiveDataSource.getItemsLiveData()

}

