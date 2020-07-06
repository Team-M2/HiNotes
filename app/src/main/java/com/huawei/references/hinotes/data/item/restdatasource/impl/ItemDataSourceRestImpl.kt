package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DBError
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


    override suspend fun getItemByIds(itemIds: List<Int>,itemType: ItemType): DataHolder<List<Item>> =
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

    override suspend fun getItemsByUserId(userId: String,itemType: ItemType) : DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query= "select json_agg(json_build_object('itemId',\"itemId\",'createdAt',\"createdAt\",'updatedAt',\"updatedAt\",'type',\"type\",'isOpen',\"isOpen\",'lat',\"lat\",'lng',\"lng\",'poiDescription',\"poiDescription\",'title',\"title\",'isChecked',\"isChecked\",'isPinned',\"isPinned\")) from hinotesschema.item WHERE \"type\"=${itemType.type} "
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

    override suspend fun deleteItem(item:Item,userId: String) : DataHolder<Any> =
        when(item.type){
            ItemType.TodoList->{
                when(val res=subItemDataSource.deleteSubItems(item.todoListSubItems?.map { it.id } ?: listOf())){
                    is DataHolder.Success ->{
                        deleteItemCore(item, userId)
                    }
                    is DataHolder.Fail -> res
                    is DataHolder.Loading -> res
                }
            }
            ItemType.Note->{
                deleteItemCore(item,userId)
            }
        }


    private suspend fun deleteItemCore(item: Item,userId:String) : DataHolder<Any> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query= "DELETE FROM hinotesschema.item WHERE \"itemId\"=${item.itemId}"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult ->DataHolder.Success(Any())
                else -> DataHolder.Fail(baseError = DBError("delete error"))
            }
        }


    override suspend fun deleteItems(items: List<Item>, userId: String): DataHolder<Any> {
        val resultList= mutableListOf<DataHolder<Any>>()
        items.forEach {item->
            when(item.type){
                ItemType.TodoList->{
                    when(val res=subItemDataSource.deleteSubItems(item.todoListSubItems?.map { it.id } ?: listOf())){
                        is DataHolder.Success ->{
                            deleteItemCore(item,userId)
                        }
                        is DataHolder.Fail -> res
                        is DataHolder.Loading -> res
                    }
                }
                ItemType.Note->{
                    deleteItemCore(item,userId)
                }
            }.let {
               resultList.add(it)
            }
        }
       return if(resultList.count { it is DataHolder.Fail } >0 ) DataHolder.Fail(baseError = DBError("delete error"))
        else DataHolder.Success(Any())
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
            }.let {itemDbResult->
                when(itemDbResult){
                    is DBResult.InsertResultId ->{
                        when(item.type){
                            ItemType.Note->{
                                DataHolder.Success(itemDbResult.id)
                            }
                            ItemType.TodoList->{
                                subItemDataSource.insertMultiple(item.todoListSubItems ?: listOf(),itemDbResult.id).let {
                                    when(it){
                                        is DataHolder.Success -> DataHolder.Success(itemDbResult.id)
                                        is DataHolder.Fail -> it
                                        is DataHolder.Loading -> it
                                    }
                                }
                            }
                        }
                    }
                    else -> DataHolder.Fail(baseError = DBError("Insert error"))
                }
            }
        }
        else{
            when(item.type){
                ItemType.Note->{
                    updateItemCore(item)
                }
                ItemType.TodoList->{
                    // inserts sub items with id -1 , updates others
                    item.todoListSubItems?.takeIf{ it.isNotEmpty() }?.partition { it.id==-1 }?.let {
                        val insertResult= if(it.first.isEmpty()){
                            DataHolder.Success(Any())
                        } else subItemDataSource.insertMultiple(it.first,item.itemId)

                        val updateResult= if(it.second.isEmpty()){
                            DataHolder.Success(Any())
                        } else subItemDataSource.updateMultiple(it.second,item.itemId)

                        if(updateResult is DataHolder.Success && insertResult is DataHolder.Success){
                            updateItemCore(item)
                        }
                        else  DataHolder.Fail()
                    } ?: updateItemCore(item)
                }
            }
        }

    private suspend fun updateItemCore(item: Item) : DataHolder<Int> =
        apiCallAdapter.adapt<Any> {
            val query="UPDATE hinotesschema.item SET \"updatedAt\" = NOW(),\"type\"=${item.type.type},\"isOpen\"=${item.isOpen},lat=${item.lat},lng=${item.lng},\"poiDescription\"=${item.poiDescription.takeIf { (it?:"").isNotBlank() }?:"null"},\"title\"=${item.title},\"isChecked\"=${item.isChecked},\"isPinned\"=${item.isPinned} where \"itemId\"=${item.itemId}"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult -> DataHolder.Success(item.itemId)
                else -> DataHolder.Fail(baseError = DBError("Insert error"))
            }
        }

}