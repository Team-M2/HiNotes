package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DBInsertError
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.ItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.SubItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.restdatasource.model.ItemRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToItem
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.DBResult
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService

class ItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                             private val itemRestService: ItemRestService,
                             private val permissionsDataSource: PermissionsDataSource,
                             private val subItemDataSource: SubItemDataSource
) : ItemDataSource {

    override suspend fun getItemById(itemId: Int): DataHolder<Item> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query=""
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.ResultList ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.first().mapToItem())
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }


    override suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query=""
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.ResultList ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToItem() })
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    override suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query= "select json_agg(json_build_object('itemId',\"itemId\",'createdAt',\"createdAt\",'updatedAt',\"updatedAt\",'type',\"type\",'isOpen',\"isOpen\",'lat',\"lat\",'lng',\"lng\",'poiDescription',\"poiDescription\",'title',\"title\",'isChecked',\"isChecked\",'isPinned',\"isPinned\")) from hinotesschema.item"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.ResultList ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToItem() })
                }
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    override suspend fun deleteItem(item:Item,userId: String) : DataHolder<Any>{
        return DataHolder.Success(Any())
    }

    override suspend fun upsertItem(item:Item,userId: String,isNew:Boolean) : DataHolder<Any>{
        return if(isNew){
            upsertCore(item,isNew)
        }
        else{
//            when(val res=permissionsDataSource.getPermissions(userId)){
//                is DataHolder.Success ->{
//                    res.data.takeIf { it.isNotEmpty() }?.let {
//                        var permReturn: DataHolder<Any> = DataHolder.Fail(baseError = NoPermissionError())
//                        it.forEach {
//                            if(it.userRole== UserRole.Owner || it.userRole== UserRole.Write){
//                                permReturn=DataHolder.Success(Any())
//                            }
//                        }
//                        permReturn
//                    } ?: DataHolder.Fail(baseError = NoPermissionError())
//                }
//                is DataHolder.Fail -> res
//                is DataHolder.Loading -> res
//            }
            upsertCore(item,isNew)
        }
    }

    private suspend fun upsertCore(item:Item,isNew: Boolean) : DataHolder<Int> =
        if(isNew){
            apiCallAdapter.adapt<Any> {
                val query="insert into hinotesschema.item(\"createdAt\",\"updatedAt\",\"type\",\"isOpen\",lat,lng,\"poiDescription\",\"title\",\"isChecked\",\"isPinned\") values (${if(isNew) "NOW()," else ""}NOW(),${item.type.type},${item.isOpen},${item.lat?:"NULL"},${item.lng?:"NULL"},${item.poiDescription?.let{ "'$it'" }?:"NULL"},'${item.title}',${item.isChecked},${item.isPinned}) returning \"itemId\""
                itemRestService.executeQuery(query)
            }.let {
                when(it){
                    is DBResult.InsertResultId ->{
                        when(item.type){
                            ItemType.Note->{
                                DataHolder.Success(it.id)
                            }
                            ItemType.TodoList->{
                                subItemDataSource.insertMultiple(item.todoListSubItems ?: listOf(),it.id).let {
                                    when(it){
                                        is DataHolder.Success -> DataHolder.Success(item.itemId)
                                        is DataHolder.Fail -> it
                                        is DataHolder.Loading -> it
                                    }
                                }
                            }
                        }
                    }
                    else -> DataHolder.Fail(baseError = DBInsertError("Insert error"))
                }
            }
        }
        else{
            when(item.type){
                ItemType.Note->{
                    updateItemCore(item)
                }
                ItemType.TodoList->{
                    subItemDataSource.updateMultiple(item.todoListSubItems?:listOf(),item.itemId).let {
                        when(it){
                            is DataHolder.Success ->{
                                updateItemCore(item)
                            }
                            is DataHolder.Loading ->it
                            is DataHolder.Fail -> it
                        }
                    }
                }
            }
        }


    private suspend fun updateItemCore(item: Item) : DataHolder<Int> =
        apiCallAdapter.adapt<Any> {
            val query="UPDATE hinotesschema.item SET \"updatedAt\" = NOW(),\"type\"=${item.type.type},\"isOpen\"=${item.isOpen},lat=${item.lat},lng=${item.lng},\"poiDescription\"=${item.poiDescription},\"title\"=${item.title},\"isChecked\"=${item.isChecked},\"isPinned\"=${item.isPinned} where \"itemId\"=${item.itemId}"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.SuccessfulOperation -> DataHolder.Success(item.itemId)
                else -> DataHolder.Fail(baseError = DBInsertError("Insert error"))
            }
        }

}