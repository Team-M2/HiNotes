package com.huawei.references.hinotes.ui.itemdetail.todolistdetail.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import kotlinx.android.synthetic.main.todo_list_sub_item_list.view.*

class TodoListSubItemsAdapter(var todoListSubItems:List<TodoListSubItem>) : RecyclerView.Adapter<TodoListSubItemsAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.
        context).inflate(R.layout.todo_list_sub_item_list,parent,false)
        return FeedViewHolder(view)
    }
    override fun getItemCount(): Int {
        return todoListSubItems.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.sub_item_title.setText(todoListSubItems[position].title)
        holder.itemView.sub_item_checkbox.isChecked = todoListSubItems[position].isChecked

        holder.itemView.sub_item_title.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                todoListSubItems[position].title = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        holder.itemView.sub_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            todoListSubItems[position].isChecked=isChecked
        }
    }

    class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}