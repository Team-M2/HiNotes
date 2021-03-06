package com.huawei.references.hinotes.ui.itemlist.notes.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R

internal class NoteItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
    val noteTitle: TextView = rootView.findViewById(R.id.note_title)
    val noteDescription: TextView = rootView.findViewById(R.id.note_description)
    val noteCreatedDate: TextView = rootView.findViewById(R.id.note_created_date)
    val pinIcon: ImageView = rootView.findViewById(R.id.pin_icon)
    val selectedItemCheckBox: CheckBox = rootView.findViewById(R.id.select_item_checkbox)
}