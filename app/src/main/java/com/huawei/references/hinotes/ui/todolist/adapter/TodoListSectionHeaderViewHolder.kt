package com.huawei.references.hinotes.adapter.todolist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R

internal class TodoListSectionHeaderViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val todoListSectionHeader: TextView = view.findViewById(R.id.todo_list_header_title)
}