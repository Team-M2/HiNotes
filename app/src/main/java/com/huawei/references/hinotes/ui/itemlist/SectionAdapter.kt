package com.huawei.references.hinotes.ui.itemlist

import com.huawei.references.hinotes.data.item.model.Item

interface SectionAdapter {

    fun clearSelections()

    fun getSelectedItems() : List<Item>

    fun setItems(items: List<Item>)
}