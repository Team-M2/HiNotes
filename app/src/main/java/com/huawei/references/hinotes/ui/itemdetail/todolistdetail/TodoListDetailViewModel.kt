package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.ReminderRepository
import com.huawei.references.hinotes.data.item.SubItemRepository
import com.huawei.references.hinotes.data.item.model.Reminder
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListDetailViewModel(itemRepository: ItemRepository,
                              private val subItemRepository: SubItemRepository,
                              private val reminderRepository: ReminderRepository
                              )
    : ItemDetailViewModel(itemRepository) {

    private val _todoSubItemsLiveData = MutableLiveData<DataHolder<List<TodoListSubItem>>>()
    val todoSubItemsLiveData : LiveData<DataHolder<List<TodoListSubItem>>>
        get() = _todoSubItemsLiveData

    private val _reminderLiveData = MutableLiveData<DataHolder<List<Reminder>>>()
    val reminderLiveData : LiveData<DataHolder<List<Reminder>>>
        get() = _reminderLiveData

    fun getTodoSubItems(itemId:Int){
        _todoSubItemsLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _todoSubItemsLiveData.postValue(subItemRepository.getSubItemsByItemId(itemId))
        }
    }

    fun getReminders(itemId:Int){
        _reminderLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _reminderLiveData.postValue(reminderRepository.getRemindersByItemId(itemId))
        }
    }
}