package com.huawei.references.hinotes.ui.todolist.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wooz.hinotes.R

internal class TodoListSectionHeaderViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val todoListSectionHeader: TextView = view.findViewById(R.id.todo_list_header_title)
}