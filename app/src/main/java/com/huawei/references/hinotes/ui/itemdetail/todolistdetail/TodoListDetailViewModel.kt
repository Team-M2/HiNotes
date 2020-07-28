package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.ReminderRepository
import com.huawei.references.hinotes.data.item.SubItemRepository
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.data.ml.MLRepository
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListDetailViewModel(itemRepository: ItemRepository,
                              private val subItemRepository: SubItemRepository,
                              reminderRepository: ReminderRepository,
                              mlRepository: MLRepository
                              )
    : ItemDetailViewModel(itemRepository,reminderRepository,mlRepository) {

    private val _todoSubItemsLiveData = MutableLiveData<DataHolder<List<TodoListSubItem>>>()
    val todoSubItemsLiveData : LiveData<DataHolder<List<TodoListSubItem>>>
        get() = _todoSubItemsLiveData


    fun getTodoSubItems(itemId:Int){
        _todoSubItemsLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _todoSubItemsLiveData.postValue(subItemRepository.getSubItemsByItemId(itemId))
        }
    }
}