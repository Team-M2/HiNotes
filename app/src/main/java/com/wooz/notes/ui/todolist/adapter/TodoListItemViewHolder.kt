package com.wooz.notes.ui.todolist.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wooz.notes.R

internal class TodoListItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
    val todoListSubCheckbox: CheckBox = rootView.findViewById(R.id.todo_list_sub_checkbox)
    val todoListSubText: TextView = rootView.findViewById(R.id.todo_list_sub_text)
}