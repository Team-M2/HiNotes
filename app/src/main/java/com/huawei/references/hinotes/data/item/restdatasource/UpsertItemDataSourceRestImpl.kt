package com.wooz.notes.data.item.restdatasource

import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.abstractions.UpsertItemDataSource
import com.wooz.notes.data.item.model.Item

class UpsertItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                   private val itemRestService: ItemRestService,
                                   private val getItemDataSource: GetItemDataSource
                     ) :
    UpsertItemDataSource {

    override suspend fun upsertItem(item:Item,userId: String,isNew:Boolean) : DataHolder<Any>{
        return DataHolder.Success(Any())
    }

}
