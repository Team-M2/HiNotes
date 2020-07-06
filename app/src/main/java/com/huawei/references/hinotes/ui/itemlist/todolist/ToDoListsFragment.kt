package com.huawei.references.hinotes.ui.itemlist.todolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.TodoListDetailActivity
import com.huawei.references.hinotes.ui.itemlist.ItemListBaseFragment
import com.huawei.references.hinotes.ui.itemlist.SectionAdapter
import com.huawei.references.hinotes.ui.itemlist.todolist.adapter.TodoListSectionAdapter
import kotlinx.android.synthetic.main.fragment_todo_list.*

class ToDoListsFragment : ItemListBaseFragment() {

    override fun getRecyclerView(): RecyclerView = todo_list_recycler_view

    override val myItemsSectionAdapter: SectionAdapter=
        TodoListSectionAdapter("My Todo Lists",
            this)

    override val sharedItemsSectionAdapter: SectionAdapter=
        TodoListSectionAdapter("My Shared Todo Lists",
            this)

    override val pageTitle: Int =  R.string.title_toolbar_todo_list

    @SuppressLint("NewApi")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        floatingActionTodoButton.setOnClickListener {
            startActivity(Intent(activity, TodoListDetailActivity::class.java))
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun setOnLongClickListener() {
        sectionedAdapter.notifyDataSetChanged()
        super.setOnLongClickListener()
    }

    override fun getData() {
        runWithAGConnectUserOrOpenLogin {
            itemListViewModel.getItems(it.uid,ItemType.TodoList)
        }
    }
}