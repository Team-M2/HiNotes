package com.huawei.references.hinotes.ui.todolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.todolistdetail.TodoListDetailActivity
import com.huawei.references.hinotes.ui.base.BaseFragment
import com.huawei.references.hinotes.ui.todolist.adapter.TodoListSectionAdapter
import com.huawei.references.hinotes.ui.todolistdetail.TodoListDetailActivity
import com.huawei.references.hinotes.ui.notes.NotesFragment
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_todo_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToDoListsFragment : BaseFragment(),SwipeRefreshLayout.OnRefreshListener {

    private val toDoListsViewModel: ToDoListsViewModel by viewModel()
    private var todoListSectionedAdapter: SectionedRecyclerViewAdapter? = null

    companion object{
        var userMyTodoList:ArrayList<Item> = arrayListOf()
        var userSharedTodoList:ArrayList<Item> = arrayListOf()
    }

    @SuppressLint("NewApi")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarTextView = activity!!.findViewById<View>(R.id.toolbar_title) as TextView
        val toolbarCancelIcon = activity!!.findViewById(R.id.toolbar_cancel_icon) as ImageView
        val toolbarDeleteIcon = activity!!.findViewById(R.id.toolbar_delete_icon) as ImageView
        setDefaultToolbar()
        toolbarTextView.text = resources.getString(R.string.title_toolbar_todo_list)
        toDoListsViewModel.getNotes(1)
        todoListSectionedAdapter = SectionedRecyclerViewAdapter()
        todo_list_recycler_view.layoutManager= LinearLayoutManager(context)
        todo_list_recycler_view.adapter=todoListSectionedAdapter
        todo_swipe_refresh_layout.setOnRefreshListener(this)

        toDoListsViewModel.todoListItemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    if(userMyTodoList.size == 0 && userSharedTodoList.size == 0) {
                        userMyTodoList.clear()
                        userSharedTodoList.clear()
                        it.data.forEach {
                            userMyTodoList.add(it)
                            userSharedTodoList.add(it)
                        }
                    }
                    todo_progressbar.visibility=View.GONE
                    todo_swipe_refresh_layout.isRefreshing=false

                    todoListSectionedAdapter!!.addSection(
                        TodoListSectionAdapter(
                            "My Todo Lists",
                            userMyTodoList,this,0
                        )
                    )
                    todoListSectionedAdapter!!.addSection(
                        TodoListSectionAdapter(
                            "Shared Todo Lists",
                            userSharedTodoList,this,1
                        )
                    )
                        //notesAdapter.updateNotesList(it.data)
                    todoListSectionedAdapter?.notifyDataSetChanged()
                }

                is DataHolder.Fail ->{
                    todo_progressbar.visibility=View.GONE
                    todo_swipe_refresh_layout.isRefreshing=false
                }

                is DataHolder.Loading ->{
                    todo_progressbar.visibility=View.VISIBLE
                }
            }
        })

        floatingActionTodoButton.setOnClickListener {
            val intent = Intent(activity, TodoListDetailActivity::class.java)
            startActivity(intent)
        }

        toolbarCancelIcon.setOnClickListener {
            setDefaultToolbar()
            todoListSectionedAdapter?.notifyDataSetChanged()
        }

        toolbarDeleteIcon.setOnClickListener {
            TodoListSectionAdapter.selectedTodoMyItemsList.sortDescending()
            TodoListSectionAdapter.selectedTodoSharedItemsList.sortDescending()
            TodoListSectionAdapter.selectedTodoMyItemsList.forEach{
                userMyTodoList.removeAt(it)
            }
            TodoListSectionAdapter.selectedTodoSharedItemsList.forEach{
                userSharedTodoList.removeAt(it)
            }
            setDefaultToolbar()
            todoListSectionedAdapter?.notifyDataSetChanged()
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<TextView>(R.id.toolbar_title)?.text="My To-do Lists"
    }

    override fun setOnLongClickListener() {
        todoListSectionedAdapter?.notifyDataSetChanged()
        super.setOnLongClickListener()
    }

    override fun onRefresh() {
        toDoListsViewModel.getNotes(1)
        todoListSectionedAdapter = SectionedRecyclerViewAdapter()
    }

    override fun onResume() {
        todoListSectionedAdapter?.notifyDataSetChanged()
        super.onResume()
    }
}