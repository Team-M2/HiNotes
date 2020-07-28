package com.huawei.references.hinotes.ui.itemdetail.notedetail

import com.huawei.references.hinotes.data.item.ItemRepository
import com.huawei.references.hinotes.data.item.ReminderRepository
import com.huawei.references.hinotes.data.ml.MLRepository
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel

class DetailNoteViewModel(itemRepository: ItemRepository,
                          reminderRepository: ReminderRepository,
                          mlRepository: MLRepository
                          ) :
    ItemDetailViewModel(itemRepository,reminderRepository,mlRepository) {

}