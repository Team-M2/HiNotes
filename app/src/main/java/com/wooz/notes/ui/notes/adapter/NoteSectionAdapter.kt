package com.wooz.notes.adapter

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.wooz.notes.MainActivity
import com.wooz.notes.R
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.ui.notedetail.DetailNoteActivity
import com.wooz.notes.ui.notes.adapter.NoteItemViewHolder
import com.wooz.notes.ui.notes.adapter.NoteSectionHeaderViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


class NoteSectionAdapter(
    private val noteTitleList: String, var list: List<Item>,var clickLongListener:IOnLongClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.note_item_list)
        .headerResourceId(R.layout.note_item_header_list)
        .build()
) {

    companion object{
        var longClickedItems:ArrayList<Int> = arrayListOf()
    }

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

        itemHolder.noteDescription.setOnLongClickListener {
            holder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground);
            longClickedItems?.add(position)
            clickLongListener.setOnLongClickListener()
            true
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

    interface IOnLongClickListener{
        fun setOnLongClickListener()
    }
}
