package com.huawei.references.hinotes.ui.itemdetail

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.ReminderRepository
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemSaveResult
import com.huawei.references.hinotes.data.item.model.Reminder
import com.huawei.references.hinotes.data.ml.MLRepository
import com.huawei.references.hinotes.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class ItemDetailViewModel(private val itemRepository: ItemRepository,
                                   private val reminderRepository: ReminderRepository,
                                   private val mlRepository: MLRepository
) : BaseViewModel(){

    private val _deleteItemLiveData = MutableLiveData<DataHolder<Any>>()
    val deleteItemLiveData : LiveData<DataHolder<Any>>
        get() = _deleteItemLiveData

    private val _saveItemLiveData = MutableLiveData<DataHolder<ItemSaveResult>>()
    val saveItemLiveData : LiveData<DataHolder<ItemSaveResult>>
        get() = _saveItemLiveData

    private val _reminderLiveData = MutableLiveData<DataHolder<List<Reminder>>>()
    val reminderLiveData : LiveData<DataHolder<List<Reminder>>>
        get() = _reminderLiveData

    private val _textRecognitionLiveData = MutableLiveData<DataHolder<String>>()
    val textRecognitionLiveData : LiveData<DataHolder<String>>
        get() = _textRecognitionLiveData

    fun getReminders(itemId:Int){
        _reminderLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _reminderLiveData.postValue(reminderRepository.getRemindersByItemId(itemId))
        }
    }

    fun saveItem(item: Item,
                 userId:String,
                 isNew:Boolean,
                 subItemIdsToDelete: List<Int>,
                 reminderIdsToDelete: List<Int>
                ){
        _saveItemLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("delete","size: "+subItemIdsToDelete.size.toString())
            _saveItemLiveData.postValue(itemRepository.upsertItem(item,
                userId,
                isNew,
                subItemIdsToDelete,
                reminderIdsToDelete
                ))
        }
    }

    fun deleteItem(item: Item, userId:String){
        _deleteItemLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _deleteItemLiveData.postValue(itemRepository.deleteItem(item,userId))
        }
    }

    fun performTextRecognition(bitmap: Bitmap){
        _textRecognitionLiveData.value=DataHolder.Loading()
        viewModelScope.launch(Dispatchers.Main) {
            _textRecognitionLiveData.postValue(mlRepository.recognizeImage(bitmap))
        }
    }

}