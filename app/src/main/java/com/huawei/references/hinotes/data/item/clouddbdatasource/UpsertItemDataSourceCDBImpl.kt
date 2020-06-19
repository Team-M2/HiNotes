package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.references.hinotes.data.DataConstants
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.UpsertItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.mapToItemDTO

class UpsertItemDataSourceCDBImpl(private val cloudDBZone: CloudDBZone?,
                                  private val permissionsDataSource: PermissionsDataSource,
                                  private val getItemDataSource: GetItemDataSource
                     ) :
    UpsertItemDataSource {

    override suspend fun upsertItem(item:Item,userId: String,isNew:Boolean) : DataHolder<Any>{
        try {
            var itemSize=0
            var lastItemId=0
            // getting lastItemId and itemSize
            when(val res=getItemDataSource.getItemsByUserId(userId)){
                is DataHolder.Success -> {
                    if(res.data.isNotEmpty()){
                        itemSize=res.data.size
                        lastItemId=res.data.last().itemId
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
            val itemToInsert=if(isNew){
                item.mapToItemDTO().apply {
                    itemId=lastItemId+1
                }
            }
            else item.mapToItemDTO()

            cloudDBZone?.executeUpsert(itemToInsert)?.apply {
                await()
            }?.result?.let {updateResultItemSize->
                if(isNew){
                    if(updateResultItemSize-itemSize==1){
                        return DataHolder.Success(Any())
                    }
                    //item size not increased
                    else DataHolder.Fail()
                }
                else{
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
