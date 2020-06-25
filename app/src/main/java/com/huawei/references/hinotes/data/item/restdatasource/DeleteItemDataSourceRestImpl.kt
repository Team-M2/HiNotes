package com.huawei.references.hinotes.data.item.restdatasource

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.abstractions.DeleteItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.model.Item

class DeleteItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                   private val itemRestService: ItemRestService,
                                   private val getItemDataSource: GetItemDataSource
                     ) :
    DeleteItemDataSource {

    override suspend fun deleteItem(item:Item,userId: String) : DataHolder<Any>{
        return DataHolder.Success(Any())
    }

}
