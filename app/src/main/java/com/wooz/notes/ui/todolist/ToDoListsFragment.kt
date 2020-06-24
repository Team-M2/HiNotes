package com.huawei.references.hinotes.ui.todolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.wooz.notes.ui.todolist.adapter.TodoListSectionAdapter
import com.wooz.notes.R
import com.wooz.notes.data.base.DataHolder
import com.wooz.notes.ui.base.BaseFragment
import com.wooz.notes.ui.todolist.ToDoListsViewModel
import com.wooz.notes.ui.todolistdetail.TodoListDetailActivity
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_todo_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToDoListsFragment : BaseFragment() {

    private val toDoListsViewModel: ToDoListsViewModel by viewModel()
    private var todoListSectionedAdapter: SectionedRecyclerViewAdapter? = null

    @SuppressLint("NewApi")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarTextView = activity!!.findViewById<View>(R.id.toolbar_title) as TextView
        toolbarTextView.text = "My To-do Lists"
        toDoListsViewModel.getNotes(1)

        todoListSectionedAdapter = SectionedRecyclerViewAdapter()
        todo_list_recycler_view.layoutManager= LinearLayoutManager(context)
        todo_list_recycler_view.adapter=todoListSectionedAdapter

        toDoListsViewModel.todoListItemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    // populate list with data, hide loading indicator
                    it.data.forEach {
                        /*    Toast.makeText(requireContext(),
                                it.toString(),
                                Toast.LENGTH_SHORT).show()
                         */
                    }
                    todoListSectionedAdapter!!.addSection(
                        TodoListSectionAdapter(
                            "My Todo Lists",
                            it.data
                        )
                    )
                    todoListSectionedAdapter!!.addSection(
                        TodoListSectionAdapter(
                            "Shared Todo Lists",
                            it.data
                        )
                    )
                    //notesAdapter.updateNotesList(it.data)
                    todoListSectionedAdapter?.notifyDataSetChanged()
                }

                is DataHolder.Fail ->{
                    // show error indicator
                }

                is DataHolder.Loading ->{
                    // show loading indicator
                }
            }
        })

        floatingActionTodoButton.setOnClickListener {
            val intent = Intent(activity, TodoListDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }
}