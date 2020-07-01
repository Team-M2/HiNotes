package com.huawei.references.hinotes.ui.notes.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.notedetail.DetailNoteActivity
import com.huawei.references.hinotes.ui.notes.adapter.NoteItemViewHolder
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionHeaderViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import kotlinx.android.synthetic.main.note_item_list.view.*


class NoteSectionAdapter(
    private val noteTitleList: String, var list: List<Item>, private var onLongClickListener:IOnLongClickListener, var sectionIndex:Int
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.note_item_list)
        .headerResourceId(R.layout.note_item_header_list)
        .build()
) {
    companion object{
       // var selectedNoteItemsList:ArrayList<Int> = arrayListOf()
        var selectedMyNoteItemsList:ArrayList<Int> = arrayListOf()
        var selectedSharedNoteItemsList:ArrayList<Int> = arrayListOf()
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

        if((selectedMyNoteItemsList.contains(position) && sectionIndex == 0) || (selectedSharedNoteItemsList.contains(position) && sectionIndex == 1) ){
            holder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground)
        }
        else{
            holder.rootView.setBackgroundResource(R.color.colorViewsBackground)
        }

        itemHolder.rootView.setOnClickListener { v ->
            if(selectedMyNoteItemsList.size == 0 && selectedSharedNoteItemsList.size == 0 && itemHolder.selectedItemCheckBox.visibility != View.VISIBLE) {
                val intent = Intent(v.context, DetailNoteActivity::class.java)
                intent.putExtra("clickedItemData", noteItem)
                intent.putExtra("clickedItemSection", sectionIndex)
                intent.putExtra("clickedIndex", position)
                startActivity(v.context, intent, null)
            }
            else{
                if((selectedMyNoteItemsList.contains(position) && sectionIndex == 0) || (selectedSharedNoteItemsList.contains(position) && sectionIndex == 1) ){
                    performOnLongClickItem(holder.rootView,position,false)
                    println()
                }
                else{
                    performOnLongClickItem(holder.rootView,position,true)
                }
            }
        }

        itemHolder.rootView.setOnLongClickListener {
            if((selectedMyNoteItemsList.contains(position) && sectionIndex == 0) || (selectedSharedNoteItemsList.contains(position) && sectionIndex == 1) ){
                performOnLongClickItem(holder.rootView,position,false)
                println()
            }
            else{
                performOnLongClickItem(holder.rootView,position,true)
            }
            onLongClickListener.setOnLongClickListener()
            true
        }

        itemHolder.selectedItemCheckBox.setOnClickListener {
            if((selectedMyNoteItemsList.contains(position) && sectionIndex == 0) || (selectedSharedNoteItemsList.contains(position) && sectionIndex == 1) ){
                performOnLongClickItem(holder.rootView,position,false)
            }
            else{
                performOnLongClickItem(holder.rootView,position,true)
            }
        }

        if(selectedMyNoteItemsList.size == 0 && selectedSharedNoteItemsList.size == 0){
            itemHolder.selectedItemCheckBox.visibility=View.GONE
            itemHolder.selectedItemCheckBox.isChecked=false
        }
        else{
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

    private fun performOnLongClickItem(view:View, position: Int, selected:Boolean){
        if(selected) {
            view.setBackgroundResource(R.color.colorSelectedViewBackground)
            view.select_item_checkbox.isChecked=true
            if(sectionIndex == 0 && !selectedMyNoteItemsList.contains(position)){
                selectedMyNoteItemsList.add(position)
            }
            else{
                selectedSharedNoteItemsList.add(position)
            }
        }
        else {
            view.setBackgroundResource(R.color.colorViewsBackground)
            view.select_item_checkbox.isChecked=false
            if(sectionIndex == 0 && !selectedSharedNoteItemsList.contains(position)){
                selectedMyNoteItemsList.remove(position)
            }
            else{
                selectedSharedNoteItemsList.remove(position)
            }
        }
    }
}
