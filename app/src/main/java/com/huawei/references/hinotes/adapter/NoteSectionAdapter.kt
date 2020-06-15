package com.huawei.references.hinotes.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.note.model.Item
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


class ContactsSection(
    private val noteTitleList: String, var list: List<Item>
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.note_item_list)
        .headerResourceId(R.layout.note_item_header_list)
        .build()
) {


    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val itemHolder: ItemViewHolder = holder as ItemViewHolder
        val noteItem: Item = list[position]
        itemHolder.noteTitle.text = noteItem.itemId
        itemHolder.noteDescription.text = noteItem.poiDescription
        itemHolder.noteCreatedDate.text = "08:24 PM"

        itemHolder.rootView.setOnClickListener { v ->
            println("clicked item -> ")
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder: HeaderViewHolder = holder as HeaderViewHolder
        headerHolder.noteSectionHeader.text = noteTitleList
    }

}
