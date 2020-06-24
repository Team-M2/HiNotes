package com.wooz.notes.data.item.restdatasource

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.abstractions.DeleteItemDataSource
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.model.Item

class DeleteItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                   private val itemRestService: ItemRestService,
                                   private val getItemDataSource: GetItemDataSource
                     ) :
    DeleteItemDataSource {

    override suspend fun deleteItem(item:Item,userId: String) : DataHolder<Any>{
        return DataHolder.Success(Any())
    }

}
