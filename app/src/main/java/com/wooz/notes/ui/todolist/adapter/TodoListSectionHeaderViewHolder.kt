package com.wooz.notes.ui.todolist.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wooz.notes.R

internal class TodoListSectionHeaderViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val todoListSectionHeader: TextView = view.findViewById(R.id.todo_list_header_title)
}