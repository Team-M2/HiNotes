package com.huawei.references.hinotes.data.item

import androidx.lifecycle.LiveData
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.ItemsLiveDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.data.item.model.UserRole
import java.util.*

class ItemRepository(private val itemDataSource: ItemDataSource,
                     private val itemsLiveDataSource: ItemsLiveDataSource
                     ) {

    suspend fun getNotesDummy(): DataHolder<List<Item>> {
        return DataHolder.Success(
            mutableListOf(
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
                    mutableListOf(TodoListSubItem(1,11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(2,12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(3,12,Date(),Date(),"Buy something else",false)),
                    false,UserRole.Owner,
                    true
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
                    mutableListOf(TodoListSubItem(4,11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(5,12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(6,12,Date(),Date(),"Buy something else",false)),
                    false,UserRole.Owner,
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
                    mutableListOf(TodoListSubItem(7,11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(8,12,Date(),Date(),"Buy drink",true),
                        TodoListSubItem(9,12,Date(),Date(),"Buy something else",true)),
                    false,UserRole.Owner,
                    true
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
                    mutableListOf(TodoListSubItem(11,11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(12,12,Date(),Date(),"Buy drink",true),
                        TodoListSubItem(13,12,Date(),Date(),"Buy something else",true)),
                    false,UserRole.Owner,
                    true
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
                    mutableListOf(TodoListSubItem(14,11,Date(),Date(),"Buy food",true),
                        TodoListSubItem(15,12,Date(),Date(),"Buy drink",false),
                        TodoListSubItem(16,12,Date(),Date(),"Buy something else",false)),
                    false,UserRole.Owner,
                    true
                )
            )
        )
    }

    suspend fun getItems(userId: String,itemType: ItemType): DataHolder<List<Item>> =
        itemDataSource.getItemsByUserId(userId,itemType)

    suspend fun getItemsLiveData() : LiveData<DataHolder<List<Item>>> =
        itemsLiveDataSource.getItemsLiveData()

    suspend fun upsertItem(item: Item,userId: String,isNew:Boolean) : DataHolder<Any> =
        itemDataSource.upsertItem(item,userId,isNew)


    suspend fun deleteItem(item:Item,userId:String) : DataHolder<Any> =
        itemDataSource.deleteItem(item,userId)


    suspend fun deleteItems(items:List<Item>,userId:String) : DataHolder<Any> =
        itemDataSource.deleteItems(items,userId)

    suspend fun checkUncheckTodoItem(userId:String,item:Item,isChecked:Boolean) : DataHolder<Any> =
        itemDataSource.checkUncheckTodoItem(userId,item,isChecked)

}

