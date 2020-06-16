package com.huawei.references.hinotes.adapter.todolist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


class TodoListSectionAdapter(
    private val todoListTitleList: String, var list: List<Item>
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.todo_list_item_list)
        .headerResourceId(R.layout.todo_list_header_list)
        .build()
) {


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
        val noteItem: Item = list[position]
        todoListItemHolder.todoListTitle.text = noteItem.itemId.toString()

        todoListItemHolder.rootView.setOnClickListener { v ->
            println("clicked item -> ")
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

}
