package com.huawei.references.hinotes.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoListsViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _todoListItemsLiveData = MutableLiveData<DataHolder<List<Item>>>()
    val todoListItemsLiveData : LiveData<DataHolder<List<Item>>>
        get() = _todoListItemsLiveData

    fun getNotes(userId: Int){
        _todoListItemsLiveData.value= DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _todoListItemsLiveData.postValue(itemRepository.getNotesDummy())
        }
    }
}