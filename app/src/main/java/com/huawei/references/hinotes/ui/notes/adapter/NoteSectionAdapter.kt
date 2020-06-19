package com.huawei.references.hinotes.ui.notes.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.wooz.hinotes.R
import com.wooz.hinotes.data.item.model.Item
import com.wooz.hinotes.ui.notedetail.DetailNoteActivity
import com.wooz.hinotes.ui.notes.adapter.NoteItemViewHolder
import com.wooz.hinotes.ui.notes.adapter.NoteSectionHeaderViewHolder
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
        return NoteItemViewHolder(
            view
        )
    }

    override fun onBindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val itemHolder: NoteItemViewHolder = holder as NoteItemViewHolder
        val noteItem: Item = list[position]
        itemHolder.noteTitle.text = noteItem.title
        itemHolder.noteDescription.text = noteItem.poiDescription
        itemHolder.noteCreatedDate.text = "08:24 PM"

        itemHolder.rootView.setOnClickListener { v ->
            val intent = Intent(v.context, DetailNoteActivity::class.java)
            intent.putExtra("clickedItemData", noteItem)
            startActivity(v.context,intent,null)
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return NoteSectionHeaderViewHolder(
            view
        )
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder: NoteSectionHeaderViewHolder = holder as NoteSectionHeaderViewHolder
        headerHolder.noteSectionHeader.text = noteTitleList
    }

}
