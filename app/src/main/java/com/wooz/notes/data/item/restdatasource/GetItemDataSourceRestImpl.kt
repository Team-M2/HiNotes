package com.wooz.notes.data.item.restdatasource

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.base.NoRecordFoundError
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.data.item.restdatasource.model.ItemRestDTO
import com.wooz.notes.data.item.restdatasource.model.mapToItem

class GetItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                private val itemRestService: ItemRestService
                                ) :
    GetItemDataSource {

    override suspend fun getItemById(itemId: Int): DataHolder<Item> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query=""
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DataHolder.Success ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.first().mapToItem())
                }
                is DataHolder.Fail -> it
                is DataHolder.Loading -> it
            }
        }


    override suspend fun getItemByIds(itemIds: List<Int>): DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query=""
        itemRestService.executeQuery(query)
    }.let {
        when(it){
            is DataHolder.Success ->{
                if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                else DataHolder.Success(it.data.map { it.mapToItem() })
            }
            is DataHolder.Fail -> it
            is DataHolder.Loading -> it
        }
    }
    override suspend fun getItemsByUserId(userId: String) : DataHolder<List<Item>> =
        apiCallAdapter.adapt<ItemRestDTO> {
            val query= "select json_agg(json_build_object('itemId',\"itemId\",'createdAt',created_at,'updatedAt',\"updatedAt\",'type',\"type\",'isOpen',\"isOpen\",'lat',\"lat\",'lng',\"lng\",'poiDescription',\"poiDescription\",'title',\"title\",'isChecked',\"isChecked\",'isPinned',\"isPinned\")) from hinotesschema.item"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DataHolder.Success ->{
                    if(it.data.isEmpty()) DataHolder.Fail(baseError = NoRecordFoundError())
                    else DataHolder.Success(it.data.map { it.mapToItem() })
                }
                is DataHolder.Fail -> it
                is DataHolder.Loading -> it
            }
        }

}
