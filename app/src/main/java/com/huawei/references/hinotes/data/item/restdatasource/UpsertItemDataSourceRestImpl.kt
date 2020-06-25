package com.huawei.references.hinotes.data.item.restdatasource

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoPermissionError
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.GetItemDataSource
import com.huawei.references.hinotes.data.item.abstractions.PermissionsDataSource
import com.huawei.references.hinotes.data.item.abstractions.UpsertItemDataSource
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.data.item.restdatasource.model.ItemRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToItem

class UpsertItemDataSourceRestImpl(private val apiCallAdapter: ApiCallAdapter,
                                   private val itemRestService: ItemRestService,
                                   private val getItemDataSource: GetItemDataSource,
                                   private val permissionsDataSource: PermissionsDataSource
                     ) :
    UpsertItemDataSource {

    override suspend fun upsertItem(item:Item,userId: String,isNew:Boolean) : DataHolder<Any>{
        return if(isNew){
            upsertCore(item,isNew)
        }
        else{
            when(val res=permissionsDataSource.getPermissions(userId)){
                is DataHolder.Success ->{
                    res.data.takeIf { it.isNotEmpty() }?.let {
                        var permReturn: DataHolder<Any> = DataHolder.Fail(baseError = NoPermissionError())
                        it.forEach {
                            if(it.userRole==UserRole.Owner || it.userRole==UserRole.Write){
                                permReturn=DataHolder.Success(Any())
                            }
                        }
                        permReturn
                    } ?: DataHolder.Fail(baseError = NoPermissionError())
                }
                is DataHolder.Fail -> res
                is DataHolder.Loading -> res
            }
        }
    }

    private suspend fun upsertCore(item:Item,isNew: Boolean) : DataHolder<Any>{
        return apiCallAdapter.adapt<ItemRestDTO> {
            val query="insert into hinotesschema.item(\"createdAt\",\"updatedAt\",\"type\",\"isOpen\",lat,lng,\"poiDescription\",\"title\",\"isChecked\",\"isPinned\") values (${if(isNew) "NOW()," else ""}NOW(),${item.type.type},${item.isOpen},${item.lat?:"NULL"},${item.lng?:"NULL"},${item.poiDescription?.let{ "'$it'" }?:"NULL"},'${item.title}',${item.isChecked},${item.isPinned}) returning \"itemId\""
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
    }

}
