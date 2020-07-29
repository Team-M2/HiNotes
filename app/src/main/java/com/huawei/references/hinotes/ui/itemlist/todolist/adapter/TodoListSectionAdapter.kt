package com.huawei.references.hinotes.ui.itemlist.todolist.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.isAllFalse
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.TodoListDetailActivity
import com.huawei.references.hinotes.ui.itemlist.SectionAdapter
import com.huawei.references.hinotes.ui.itemlist.notes.adapter.IOnLongClickListener
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


class TodoListSectionAdapter(
    private val todoListTitleList: String,
    private var onLongClickListener: IOnLongClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.todo_list_item_list)
        .headerResourceId(R.layout.todo_list_header_list)
        .build()
) , SectionAdapter {

    private val selectedItems:HashMap<Item,Boolean> = hashMapOf()

    var checkCallback : (item:Item) -> Unit = {}

    val list: ArrayList<Item> = arrayListOf()

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return TodoListItemViewHolder(
            view
        )
    }

    override fun onBindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val todoListItemHolder: TodoListItemViewHolder = holder as TodoListItemViewHolder
        val todoListItem: Item = list[position]
        todoListItemHolder.todoListSubText.text = todoListItem.title
        todoListItemHolder.todoListSubCheckbox.apply {
            isChecked = todoListItem.isChecked!!
            setOnCheckedChangeListener { _, isCheckedParam ->
                isChecked=isCheckedParam
                checkCallback.invoke(todoListItem.apply {
                    isChecked=isCheckedParam
                })
            }
            //setOnCheckedChangeListener(checkListener)
        }

        if(selectedItems[todoListItem]!!){
            holder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground)
        }
        else{
            holder.rootView.setBackgroundResource(R.color.colorViewsBackground)
        }

        todoListItemHolder.rootView.setOnClickListener { v ->
            if(selectedItems.isAllFalse()) {
                val intent = Intent(v.context, TodoListDetailActivity::class.java)
                intent.putExtra(TodoListDetailActivity.ITEM_KEY, todoListItem)
                ContextCompat.startActivity(v.context, intent, null)
            }
            else{
                if(selectedItems[todoListItem]!!)
                    performOnLongClickItem(v,position,false)
                else
                    performOnLongClickItem(v,position,true)
            }
        }

        todoListItemHolder.rootView.setOnLongClickListener {
            if(selectedItems[todoListItem]!!)
                performOnLongClickItem(it,position,false)
            else
                performOnLongClickItem(it,position,true)
            onLongClickListener.setOnLongClickListener()
            true
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return TodoListSectionHeaderViewHolder(
            view
        )
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val todoListSectionHeaderHolder: TodoListSectionHeaderViewHolder = holder as TodoListSectionHeaderViewHolder
        todoListSectionHeaderHolder.todoListSectionHeader.text = todoListTitleList
    }

    private fun performOnLongClickItem(view:View, position: Int, selected:Boolean){
        if(selected) {
            view.setBackgroundResource(R.color.colorSelectedViewBackground)
        }
        else {
            view.setBackgroundResource(R.color.colorViewsBackground)
        }
        selectedItems[list[position]]=selected
    }

    override fun clearSelections() {
        list.forEach {
            selectedItems[it]=false
        }
    }

    override fun setItems(items: List<Item>) {
        list.apply {
            clear()
            addAll(items)
            forEach {
                selectedItems[it] = false
            }
        }
    }

    override fun getSelectedItems(): List<Item> = selectedItems.filter { it.value }.keys.toList()
}

