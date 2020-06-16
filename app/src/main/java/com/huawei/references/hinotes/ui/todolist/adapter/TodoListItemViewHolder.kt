package com.huawei.references.hinotes.ui.todolist.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R

internal class TodoListItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
    val todoListTitle: TextView = rootView.findViewById(R.id.todo_list_sub)

}