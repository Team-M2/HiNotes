package com.huawei.references.hinotes.ui.todolists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.base.BaseFragment

class ToDoLists : BaseFragment() {

    private lateinit var toDoListsViewModel: ToDoListsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        toDoListsViewModel =
                ViewModelProviders.of(this).get(ToDoListsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo_list, container, false)
        val templateTextView: TextView = root.findViewById(R.id.text_todolist) //we will delete this textView in next step.
        toDoListsViewModel.text.observe(viewLifecycleOwner, Observer {
            templateTextView.text = it
        })
        return root
    }
}