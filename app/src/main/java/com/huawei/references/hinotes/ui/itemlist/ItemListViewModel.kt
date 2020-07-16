package com.huawei.references.hinotes.ui.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.SubscriptionParam
import com.huawei.references.hinotes.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class ItemListViewModel(private val itemRepository: ItemRepository) : BaseViewModel() {

    private val _itemsLiveData = MutableLiveData<DataHolder<List<Item>>>()
    val itemsLiveData : LiveData<DataHolder<List<Item>>>
        get() = _itemsLiveData

    private val _deleteItemsLiveData = MutableLiveData<DataHolder<Any>>()
    val deleteILiveData : LiveData<DataHolder<Any>>
        get() = _deleteItemsLiveData

    var isDataGet=false

    fun subscribeToLiveData(userId: String) =
        itemRepository.subscribe(SubscriptionParam(userId))

    fun getItems(userId: String,itemType: ItemType){
        _itemsLiveData.value= DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _itemsLiveData.postValue(itemRepository.getItems(userId,itemType))
        }
    }

    fun deleteItems(items:List<Item>, userId: String){
        _deleteItemsLiveData.value= DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _deleteItemsLiveData.postValue(itemRepository.deleteItems(items,userId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        itemRepository.unSubscribe()
    }
}

