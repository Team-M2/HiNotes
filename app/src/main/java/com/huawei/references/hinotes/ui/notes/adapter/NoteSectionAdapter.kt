package com.huawei.references.hinotes.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.adapter.notes.NoteItemViewHolder
import com.huawei.references.hinotes.adapter.notes.NoteSectionHeaderViewHolder
import com.huawei.references.hinotes.data.item.model.Item
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


class NoteSectionAdapter(
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
        return NoteItemViewHolder(view)
    }

    override fun onBindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val itemHolder: NoteItemViewHolder = holder as NoteItemViewHolder
        val noteItem: Item = list[position]
        itemHolder.noteTitle.text = noteItem.itemId.toString()
        itemHolder.noteDescription.text = noteItem.poiDescription
        itemHolder.noteCreatedDate.text = "08:24 PM"

        itemHolder.rootView.setOnClickListener { v ->
            println("clicked item -> ")
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return NoteSectionHeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder: NoteSectionHeaderViewHolder = holder as NoteSectionHeaderViewHolder
        headerHolder.noteSectionHeader.text = noteTitleList
    }

}
