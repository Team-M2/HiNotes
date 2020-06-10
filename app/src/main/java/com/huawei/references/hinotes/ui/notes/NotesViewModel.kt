package com.huawei.references.hinotes.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.references.hinotes.data.note.NoteRepository

class NotesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notes fragment"
    }
    val text: LiveData<String> = _text
}