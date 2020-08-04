package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DBError
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.ReminderDataSource
import com.huawei.references.hinotes.data.item.abstractions.SubItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemSaveResult
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.data.item.restdatasource.model.ItemRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToItem
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.DBResult
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService

class ItemDataSourceRestImpl(
    private val apiCallAdapter: ApiCallAdapter,
    private val itemRestService: ItemRestService,
    private val permissionsDataSource: PermissionsDataSource,
    private val subItemDataSource: SubItemDataSource,
    private val reminderDataSource: ReminderDataSource
) : ItemDataSource {

    override suspend fun getItemById(itemId: Int): DataHolder<Item> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query = ""
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DBResult.ResultList -> {
                    if (it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.first().mapToItem())
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }


    override suspend fun getItemByIds(
        itemIds: List<Int>,
        itemType: ItemType
    ): DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query = ""
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DBResult.ResultList -> {
                    if (it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToItem() })
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    override suspend fun getItemsByUserId(
        userId: String,
        itemType: ItemType
    ): DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query =
                "select json_agg(json_build_object('itemId',item.\"itemId\",'createdAt',\"createdAt\",'updatedAt',\"updatedAt\",'type',\"type\",'isOpen',\"isOpen\",'lat',\"lat\",'lng',\"lng\",'poiDescription',\"poiDescription\",'description',\"description\",'title',\"title\",'isChecked',\"isChecked\",'isPinned',\"isPinned\",'role',\"role\")) from hinotesschema.item INNER JOIN hinotesschema.permission ON permission.\"itemId\"=item.\"itemId\" WHERE \"type\"=${itemType.type} AND permission.\"userId\"='$userId'"
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DBResult.ResultList -> {
                    if (it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToItem() })
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    override suspend fun deleteItem(item: Item, userId: String): DataHolder<Any> =
        when (item.type) {
            ItemType.TodoList -> {
                when (val res =
                    subItemDataSource.deleteSubItems(item.todoListSubItems?.map { it.id }
                        ?: listOf())) {
                    is DataHolder.Success -> {
                        deleteItemCore(item, userId)
                    }
                    is DataHolder.Fail -> res
                    is DataHolder.Loading -> res
                }
            }
            ItemType.Note -> {
                deleteItemCore(item, userId)
            }
        }

    private suspend fun deleteItemCore(item: Item, userId: String): DataHolder<Any> =
        when (val deletePermissionResult =
            permissionsDataSource.deletePermission(userId, item.itemId)) {
            is DataHolder.Success -> {
                apiCallAdapter.adapt<ItemRestDTO> {
                    val query = "DELETE FROM hinotesschema.item WHERE \"itemId\"=${item.itemId}"
                    itemRestService.executeQuery(query)
                }.let {
                    when (it) {
                        is DBResult.EmptyQueryResult -> DataHolder.Success(Any())
                        else -> DataHolder.Fail(baseError = DBError("delete error"))
                    }
                }
            }
            is DataHolder.Fail -> deletePermissionResult
            is DataHolder.Loading -> deletePermissionResult
        }


    override suspend fun deleteItems(items: List<Item>, userId: String): DataHolder<Any> {
        val resultList = mutableListOf<DataHolder<Any>>()
        items.forEach { item ->
            when (item.type) {
                ItemType.TodoList -> {
                    when (val res =
                        subItemDataSource.deleteSubItems(item.todoListSubItems?.map { it.id }
                            ?: listOf())) {
                        is DataHolder.Success -> {
                            deleteItemCore(item, userId)
                        }
                        is DataHolder.Fail -> res
                        is DataHolder.Loading -> res
                    }
                }
                ItemType.Note -> {
                    deleteItemCore(item, userId)
                }
            }.let {
                resultList.add(it)
            }
        }
        return if (resultList.count { it is DataHolder.Fail } > 0) DataHolder.Fail(
            baseError = DBError(
                "delete error"
            )
        )
        else DataHolder.Success(Any())
    }

    override suspend fun upsertItem(
        item: Item,
        userId: String,
        isNew: Boolean,
        subItemIdsToDelete: List<Int>,
        reminderIdsToDelete: List<Int>
    ): DataHolder<ItemSaveResult> {
        return upsertCore(userId, item, isNew,subItemIdsToDelete,reminderIdsToDelete)
    }

    private suspend fun upsertCore(userId: String,
                                   item: Item,
                                   isNew: Boolean,
                                   subItemIdsToDelete: List<Int>,
                                   reminderIdsToDelete: List<Int>): DataHolder<ItemSaveResult> =
        if (isNew) {
            apiCallAdapter.adapt<Any> {
                val query =
                    "insert into hinotesschema.item(\"createdAt\",\"updatedAt\",type,\"isOpen\",lat,lng,\"poiDescription\",description,title,\"isChecked\",\"isPinned\") values (${if (isNew) "NOW()," else ""}NOW(),${item.type.type},${item.isOpen},${item.lat ?: "NULL"},${item.lng ?: "NULL"},${item.poiDescription?.let { "'$it'" } ?: "NULL"},${item.description?.let { "'$it'" } ?: "NULL"},'${item.title}',${item.isChecked},${item.isPinned}) returning \"itemId\""
                itemRestService.executeQuery(query)
            }.let { itemDbResult ->
                when (itemDbResult) {
                    is DBResult.InsertResultId -> {
                        val permResult=permissionsDataSource.upsertPermission(
                            userId,
                            itemDbResult.id,
                            UserRole.Owner,
                            true
                        )

                        val insertSubItemResult=subItemDataSource.insertMultiple(
                            item.todoListSubItems ?: listOf(), itemDbResult.id
                        )

                        val addReminderResult=item.reminder?.let {
                            reminderDataSource.upsert(it,itemDbResult.id,isNew)
                        } ?:  DataHolder.Success(-1)

                        when(DataHolder.checkAllSuccess(permResult,insertSubItemResult,addReminderResult)){
                            is DataHolder.Success ->
                                DataHolder.Success(ItemSaveResult(itemDbResult.id,
                                    (insertSubItemResult as DataHolder.Success).data,
                                    (addReminderResult as DataHolder.Success).data))
                            else -> DataHolder.Fail()
                        }
                    }
                    else -> DataHolder.Fail(baseError = DBError("Insert error"))
                }
            }
        } else {
            when (item.type) {
                ItemType.Note -> {
                    updateItemCore(item,reminderIdsToDelete)
                }
                ItemType.TodoList -> {
                    // inserts sub items with id -1 , updates others
                    val itemResult=item.todoListSubItems?.takeIf { it.isNotEmpty() }?.partition { it.id == -1 }
                        ?.let {
                            val insertResult = if (it.first.isEmpty()) {
                                DataHolder.Success(Any())
                            } else subItemDataSource.insertMultiple(it.first, item.itemId)

                            val updateResult = if (it.second.isEmpty()) {
                                DataHolder.Success(Any())
                            } else subItemDataSource.updateMultiple(it.second, item.itemId)

                            DataHolder.checkAllSuccess(insertResult,updateResult)
                        } ?: DataHolder.Success(Any())


                    val deleteSubItemResult=subItemIdsToDelete.takeIf { it.isNotEmpty() }?.let{
                        subItemDataSource.deleteSubItems(subItemIdsToDelete)
                    } ?: DataHolder.Success(-1)

                    when(DataHolder.checkAllSuccess(itemResult,
                            deleteSubItemResult
                        )){
                        is DataHolder.Success -> updateItemCore(item,reminderIdsToDelete)
                        else -> DataHolder.Fail(errStr = "update error")
                    }
                }
            }
        }

    private suspend fun updateItemCore(item: Item,reminderIdsToDelete: List<Int>): DataHolder<ItemSaveResult> =
        apiCallAdapter.adapt<Any> {
            val query =
                "UPDATE hinotesschema.item SET \"updatedAt\" = NOW(),\"type\"=${item.type.type},\"isOpen\"=${item.isOpen},lat=${item.lat},lng=${item.lng},\"poiDescription\"=${item.poiDescription.takeIf { (it ?: "").isNotBlank() }
                    ?.let { "'$it'" } ?: "NULL"},description=${item.description?.let { "'$it'" } ?: "NULL"},\"title\"='${item.title}',\"isChecked\"=${item.isChecked},\"isPinned\"=${item.isPinned} where \"itemId\"=${item.itemId}"
            itemRestService.executeQuery(query)
        }.let {
            when (it) {
                is DBResult.EmptyQueryResult -> {
                    val reminderResult=item.reminder?.let {
                        reminderDataSource.upsert(it,item.itemId,it.id==-1)
                    } ?: DataHolder.Success(Any())

                    val reminderDeleteResult=reminderIdsToDelete.takeIf { it.isNotEmpty() }?.let {
                        reminderDataSource.deleteReminders(it)
                    } ?: DataHolder.Success(-1)

                    when(DataHolder.checkAllSuccess(
                        reminderResult,reminderDeleteResult
                    )){
                        is DataHolder.Success ->  DataHolder.Success(ItemSaveResult(item.itemId))
                        else -> DataHolder.Fail(errStr = "update error")
                    }
                }
                else -> DataHolder.Fail(baseError = DBError("Update error"))
            }
        }

    override suspend fun checkUncheckTodoItem(
        userId: String,
        item: Item,
        isChecked: Boolean
    ): DataHolder<Any> =
        when (val updateSubRes =
            subItemDataSource.checkUncheckSubItemByItemId(item.itemId, isChecked)) {
            is DataHolder.Success -> {
                updateItemCore(item.apply { this.isChecked = isChecked }, listOf())
            }
            is DataHolder.Fail -> updateSubRes
            is DataHolder.Loading -> updateSubRes
        }
}