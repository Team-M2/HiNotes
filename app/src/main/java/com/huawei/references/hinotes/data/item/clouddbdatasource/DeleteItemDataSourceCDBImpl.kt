package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.DeleteItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.mapToItemDTO

class DeleteItemDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?,
                                  private val permissionsDataSource: PermissionsDataSource,
                                  private val getItemDataSource: GetItemDataSource
                     ) :
    DeleteItemDataSource {

    override suspend fun deleteItem(item:Item,userId: String) : DataHolder<Any>{
        try {
            var itemSize=0
            // getting lastItemId and itemSize
            when(val res=getItemDataSource.getItemsByUserId(userId)){
                is DataHolder.Success -> {
                    if(res.data.isNotEmpty()){
                        itemSize=res.data.size
                    }
                }
                is DataHolder.Fail -> {
                    (res.baseError as? NoRecordFoundError)?.let {
                        // no record found. Still success with zero items
                        0
                    } ?: let {
                        return res
                    }
                }
                is DataHolder.Loading -> res
            }

            cloudDBZone?.executeUpsert(item.mapToItemDTO())?.apply {
                await()
            }?.result?.let {updateResultItemSize->
                if(itemSize-updateResultItemSize==1){
                    return DataHolder.Success(Any())
                }
            }?: kotlin.run {
                return DataHolder.Fail()
            }
        }
        catch (e:Exception){
            return DataHolder.Fail(errStr = e.message ?: DataConstants.DEFAULT_ERROR_STR)
        }
        return DataHolder.Fail()
    }

}
