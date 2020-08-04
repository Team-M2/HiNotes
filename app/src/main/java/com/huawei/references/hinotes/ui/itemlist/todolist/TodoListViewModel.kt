package com.huawei.references.hinotes.ui.itemlist.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.itemlist.ItemListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel(private val itemRepository: ItemRepository) : ItemListViewModel(itemRepository) {

    private val _checkUncheckLiveData = MutableLiveData<DataHolder<Any>>()
    val checkUncheckLiveData : LiveData<DataHolder<Any>>
        get() = _checkUncheckLiveData

    fun checkUncheckTodoItem(userId:String, item: Item,isChecked:Boolean){
        _checkUncheckLiveData.value=DataHolder.Loading()
        viewModelScope.launch (Dispatchers.IO){
            _checkUncheckLiveData.postValue(itemRepository.checkUncheckTodoItem(userId,item,isChecked))
        }
    }


}