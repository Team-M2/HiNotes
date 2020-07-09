package com.huawei.references.hinotes.ui.itemlist.notes.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.hide
import com.huawei.references.hinotes.ui.base.isAllFalse
import com.huawei.references.hinotes.ui.base.show
import com.huawei.references.hinotes.ui.itemdetail.notedetail.DetailNoteActivity
import com.huawei.references.hinotes.ui.itemlist.SectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import kotlinx.android.synthetic.main.note_item_list.view.*


class NoteSectionAdapter(
    private val noteTitleList: String,
    private val onLongClickListener:IOnLongClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.note_item_list)
        .headerResourceId(R.layout.note_item_header_list)
        .build()
), SectionAdapter {

    val list: ArrayList<Item> = arrayListOf()

    private val selectedItems:HashMap<Item,Boolean> = HashMap()

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
        itemHolder.noteCreatedDate.text = noteItem.updatedAt.toString().subSequence(0,20)

        itemHolder.rootView.setOnClickListener { v ->
            if(selectedItems.isAllFalse()) {
                val intent = Intent(v.context, DetailNoteActivity::class.java)
                intent.putExtra(DetailNoteActivity.ITEM_KEY, noteItem)
                startActivity(v.context, intent, null)
            }
            else{
                performOnLongClickItem(holder,position,!selectedItems[noteItem]!!)
            }
        }

        itemHolder.rootView.setOnLongClickListener {
            performOnLongClickItem(holder,position,!selectedItems[noteItem]!!)
            onLongClickListener.setOnLongClickListener()
            true
        }

        itemHolder.selectedItemCheckBox.setOnClickListener {
            performOnLongClickItem(holder,position,!selectedItems[noteItem]!!)
        }

        setItemUI(itemHolder,noteItem)

    }

    private fun setItemUI(itemHolder: NoteItemViewHolder,noteItem:Item){
        if(!selectedItems[noteItem]!!){
            itemHolder.selectedItemCheckBox.hide()
            itemHolder.selectedItemCheckBox.isChecked=false
        }
        else{
            itemHolder.selectedItemCheckBox.show()
            itemHolder.selectedItemCheckBox.isChecked=true
        }

        if(selectedItems[noteItem]!!){
            itemHolder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground)
        }
        else{
            itemHolder.rootView.setBackgroundResource(R.color.colorViewsBackground)
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

    private fun performOnLongClickItem(itemHolder: NoteItemViewHolder, position: Int, selected:Boolean){
        selectedItems[list[position]]=selected
        if(selected) {
            itemHolder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground)
            itemHolder.rootView.select_item_checkbox.isChecked=true
        }
        else {
            itemHolder.rootView.setBackgroundResource(R.color.colorViewsBackground)
            itemHolder.rootView.select_item_checkbox.isChecked=false
        }

        setItemUI(itemHolder,list[position])

    }

    override fun clearSelections() {
        list.forEach {
            selectedItems[it]=false
        }
    }

    override fun getSelectedItems(): List<Item> = selectedItems.filter { it.value }.keys.toList()

    override fun setItems(items: List<Item>) {
        list.apply {
            clear()
            addAll(items)
            forEach {
                selectedItems[it] = false
            }
        }
    }
}
