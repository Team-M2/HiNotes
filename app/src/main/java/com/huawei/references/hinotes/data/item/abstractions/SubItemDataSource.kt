package com.huawei.references.hinotes.data.item.abstractions

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.TodoListSubItem

interface SubItemDataSource {

    suspend fun getSubItemsByItemId(itemId:Int) : DataHolder<List<TodoListSubItem>>

    suspend fun upsert(subItem: TodoListSubItem,itemId:Int,isNew: Boolean) : DataHolder<Int>

    suspend fun deleteSubItem(subItemId: Int) : DataHolder<Any>

    suspend fun deleteSubItems(subItemIds: List<Int>) : DataHolder<Any>

    suspend fun checkUncheckSubItem(subItemId: Int, check: Boolean) : DataHolder<Any>

    suspend fun checkUncheckSubItemByItemId(itemId: Int, check: Boolean) : DataHolder<Any>

    suspend fun insertMultiple(subItemList:List<TodoListSubItem>, itemId:Int): DataHolder<List<Int>>

    suspend fun updateMultiple(subItemList: List<TodoListSubItem>, itemId: Int): DataHolder<List<Int>>
}