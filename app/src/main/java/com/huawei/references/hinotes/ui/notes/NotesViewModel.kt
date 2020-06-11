package com.huawei.references.hinotes.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.note.NoteRepository
import com.huawei.references.hinotes.data.note.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notes fragment"
    }
    val text: LiveData<String> = _text

    private val _itemsLiveData = MutableLiveData<DataHolder<List<Item>>>()
    val itemsLiveData : LiveData<DataHolder<List<Item>>>
        get() = _itemsLiveData

    fun getNotes(userId: Int){
        _itemsLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _itemsLiveData.postValue(noteRepository.getNotes(userId))
        }
    }
}