package com.wooz.notes.data.item

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.abstractions.DeleteItemDataSource
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.abstractions.UpsertItemDataSource
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.data.item.model.ItemType
import com.wooz.notes.data.item.model.TodoListSubItem
import com.wooz.notes.data.item.model.UserRole
import java.util.*

class ItemRepository(private val getItemDataSource: GetItemDataSource,
                     private val upsertItemDataSource: UpsertItemDataSource,
                     private val deleteItemDataSource: DeleteItemDataSource
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
                    "Lessons",
                    listOf(TodoListSubItem(11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(12,Date(),Date(),"Buy something else",false)),
                    false,
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
                    "Meetings",
                    listOf(TodoListSubItem(11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(12,Date(),Date(),"Buy something else",false)),
                    false,
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
                    "About medicines",
                    listOf(TodoListSubItem(11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,Date(),Date(),"Buy drink",true),
                        TodoListSubItem(12,Date(),Date(),"Buy something else",true)),
                    true,
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
                    "I will buy from the market.",
                    listOf(TodoListSubItem(11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,Date(),Date(),"Buy drink",true),
                        TodoListSubItem(12,Date(),Date(),"Buy something else",true)),
                    true,
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
                    "what I have to do this month.",
                    listOf(TodoListSubItem(11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(12,Date(),Date(),"Buy something else",false)),
                    false,
                    UserRole.Owner,
                    false
                )
            )
        )
    }

    suspend fun getItems(userId: String): DataHolder<List<Item>> =
        getItemDataSource.getItemsByUserId(userId)

}

