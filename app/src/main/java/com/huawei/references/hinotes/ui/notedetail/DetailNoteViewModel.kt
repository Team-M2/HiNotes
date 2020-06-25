package com.huawei.references.hinotes.ui.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailNoteViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    fun saveItem(item: Item, userId:String){
        viewModelScope.launch(Dispatchers.IO) {
            val res=itemRepository.addItemToDb(item,userId)
            val s=""
        }
    }
}