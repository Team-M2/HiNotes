package com.huawei.references.hinotes.ui.todolistdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wooz.hinotes.R
import com.wooz.hinotes.data.item.model.TodoListSubItem
import kotlinx.android.synthetic.main.todo_list_sub_item_list.view.*

class TodoListSubItemsAdapter(var todoListSubItems:List<TodoListSubItem>?) : RecyclerView.Adapter<TodoListSubItemsAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.
        context).inflate(R.layout.todo_list_sub_item_list,parent,false)
        return FeedViewHolder(view)
    }
    override fun getItemCount(): Int {
        return todoListSubItems?.size!!
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.sub_item_title.text= todoListSubItems?.get(position)?.title ?: ""
        holder.itemView.sub_item_checkbox.isChecked = todoListSubItems?.get(position)?.isChecked!!
    }

    fun updateCheckBox(isCheckedAll:Boolean){
        for (index in todoListSubItems!!.indices){
            todoListSubItems!![index].isChecked=isCheckedAll
        }
        notifyDataSetChanged()
    }

    class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}