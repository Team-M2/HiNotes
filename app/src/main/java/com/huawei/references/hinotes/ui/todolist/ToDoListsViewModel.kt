package com.huawei.references.hinotes.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.references.hinotes.data.item.ItemRepository

class ToDoListsViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is to-do list fragment"
    }
    val text: LiveData<String> = _text
}