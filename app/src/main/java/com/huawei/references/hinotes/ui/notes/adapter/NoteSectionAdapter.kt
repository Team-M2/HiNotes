package com.huawei.references.hinotes.ui.notes.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.notedetail.DetailNoteActivity
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import kotlinx.android.synthetic.main.note_item_list.view.*


class NoteSectionAdapter(
    private val noteTitleList: String, var list: List<Item>, var onLongClickListener:IOnLongClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.note_item_list)
        .headerResourceId(R.layout.note_item_header_list)
        .build()
) {

    companion object{
        var longClickedItemsList:ArrayList<Int> = arrayListOf()
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
            if(longClickedItemsList.size == 0) {
                val intent = Intent(v.context, DetailNoteActivity::class.java)
                intent.putExtra("clickedItemData", noteItem)
                startActivity(v.context, intent, null)
            }
            else{
                performOnLongClickItem(v,position)
            }
        }

        itemHolder.rootView.setOnLongClickListener {
            performOnLongClickItem(it,position)
            onLongClickListener.setOnLongClickListener()
            true
        }

        itemHolder.selectedItemCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            performOnLongClickItem(holder.rootView,position)
        }

        if(longClickedItemsList.size != 0){
            itemHolder.selectedItemCheckBox.visibility=View.VISIBLE
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

    private fun performOnLongClickItem(view:View, position: Int){
        longClickedItemsList.add(position)
        view.setBackgroundResource(R.color.colorSelectedViewBackground)
        view.select_item_checkbox.isChecked=true
    }
}
