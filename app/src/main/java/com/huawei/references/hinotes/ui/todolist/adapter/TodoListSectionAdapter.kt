package com.huawei.references.hinotes.ui.todolist.adapter

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.notes.adapter.IOnLongClickListener
import com.huawei.references.hinotes.ui.todolist.ToDoListsFragment
import com.huawei.references.hinotes.ui.todolistdetail.TodoListDetailActivity
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import kotlinx.android.synthetic.main.activity_detail_todo_list.view.*
import kotlinx.android.synthetic.main.todo_list_item_list.view.*


class TodoListSectionAdapter(
    private val todoListTitleList: String, var list: List<Item>,private var onLongClickListener: IOnLongClickListener,var sectionIndex:Int
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.todo_list_item_list)
        .headerResourceId(R.layout.todo_list_header_list)
        .build()
) {

    companion object{
        var selectedTodoMyItemsList:ArrayList<Int> = arrayListOf()
        var selectedTodoSharedItemsList:ArrayList<Int> = arrayListOf()
    }

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
        todoListItemHolder.todoListSubCheckbox.isChecked = todoListItem.isChecked!!

        if((selectedTodoMyItemsList.contains(position) && sectionIndex == 0) || (selectedTodoSharedItemsList.contains(position) && sectionIndex == 1) ){
            holder.rootView.setBackgroundResource(R.color.colorSelectedViewBackground)
        }
        else{
            holder.rootView.setBackgroundResource(R.color.colorViewsBackground)
        }

        todoListItemHolder.rootView.setOnClickListener { v ->
            if(selectedTodoMyItemsList.size == 0 && selectedTodoSharedItemsList.size == 0) {
                val intent = Intent(v.context, TodoListDetailActivity::class.java)
                intent.putExtra("clickedItemData", todoListItem)
                intent.putExtra("clickedItemSection", sectionIndex)
                intent.putExtra("clickedIndex", position)
                ContextCompat.startActivity(v.context, intent, null)
            }
            else{
                if((selectedTodoMyItemsList.contains(position) && sectionIndex == 0) || (selectedTodoSharedItemsList.contains(0) && sectionIndex == 1))
                    performOnLongClickItem(v,position,false)
                else
                    performOnLongClickItem(v,position,true)
            }
        }

        todoListItemHolder.rootView.setOnLongClickListener {
            if((selectedTodoMyItemsList.contains(position) && sectionIndex == 0) || (selectedTodoSharedItemsList.contains(0) && sectionIndex == 1))
                performOnLongClickItem(it,position,false)
            else
                performOnLongClickItem(it,position,true)
            onLongClickListener.setOnLongClickListener()
            true
        }


        holder.itemView.todo_list_sub_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(sectionIndex == 0){
                ToDoListsFragment.userMyTodoList[position].isChecked=isChecked
            }
            else{
                ToDoListsFragment.userSharedTodoList[position].isChecked=isChecked
            }
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
            if(sectionIndex == 0 && !selectedTodoMyItemsList.contains(position)){
                selectedTodoMyItemsList.add(position)
            }
            else{
                selectedTodoSharedItemsList.add(position)
            }
        }
        else {
            view.setBackgroundResource(R.color.colorViewsBackground)
            if(sectionIndex == 0 && !selectedTodoMyItemsList.contains(position)){
                selectedTodoMyItemsList.remove(position)
            }
            else{
                selectedTodoSharedItemsList.remove(position)
            }
        }
    }
}