package com.huawei.references.hinotes.ui.base

import android.view.View
import com.huawei.references.hinotes.data.item.model.Item

fun View.show(){
    this.visibility=View.VISIBLE
}

fun View.hide(){
    this.visibility=View.GONE
}

fun HashMap<Item,Boolean>.isAllFalse() : Boolean =
    this.filter { it.value }.isEmpty()
