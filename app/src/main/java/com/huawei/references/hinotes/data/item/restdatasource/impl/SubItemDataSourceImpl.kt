package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.SubItemDataSource
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.data.item.restdatasource.model.TodoListSubItemRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.DBResult
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService

class SubItemDataSourceImpl(private val apiCallAdapter: ApiCallAdapter,
                            private val itemRestService: ItemRestService
) : SubItemDataSource {

    override suspend fun upsert(
        subItem: TodoListSubItem,
        itemId: Int,
        isNew: Boolean
    ): DataHolder<Int> =
        apiCallAdapter.adapt<TodoListSubItemRestDTO> {
            val query="insert into hinotesschema.todolistsubitem(\"itemId\",\"createdAt\",\"updatedAt\",title,\"isChecked\") values ($itemId,NOW(),NOW(),'${subItem.title}',${subItem.isChecked}) returning \"id\""
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.InsertResultId -> DataHolder.Success(it.id)
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }


    override suspend fun deleteSubItem(subItemId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<TodoListSubItemRestDTO> {
            val query="delete from hinotesschema.todolistsubitem where \"itemId\"=$subItemId"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.InsertResultId -> DataHolder.Success(it.id)
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }
    }

    override suspend fun checkUncheckSubItem(subItemId: Int, check: Boolean): DataHolder<Any> {
        return apiCallAdapter.adapt<TodoListSubItemRestDTO> {
            val query="UPDATE hinotesschema.todolistsubitem SET \"isChecked\" = ${check}, \"updatedAt\"=NOW() WHERE id = $subItemId"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.SuccessfulOperation -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }

    override suspend fun insertMultiple(subItemList: List<TodoListSubItem>, itemId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<TodoListSubItemRestDTO> {
            val query="BEGIN; ${subItemList.forEach { "insert into hinotesschema.todolistsubitem(\"itemId\",\"createdAt\",\"updatedAt\",title,\"isChecked\") values (${it.itemId},NOW(),NOW(),'${it.title}',${it.isChecked}); " }} COMMIT;"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.SuccessfulOperation -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }

    override suspend fun updateMultiple(subItemList: List<TodoListSubItem>, itemId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<TodoListSubItemRestDTO> {
            val query="BEGIN; ${subItemList.forEach { "UPDATE hinotesschema.todolistsubitem SET \"isChecked\" = ${it.isChecked},\"itemId\"=${it.itemId},\"title\"=${it.title}, \"updatedAt\"=NOW() WHERE id = ${it.id}; " }} COMMIT;"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.SuccessfulOperation -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }


}