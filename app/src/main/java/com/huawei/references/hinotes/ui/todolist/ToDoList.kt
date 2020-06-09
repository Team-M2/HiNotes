package com.huawei.references.hinotes.ui.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R

class ToDoList : Fragment() {

    private lateinit var toDoListViewModel: ToDoListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        toDoListViewModel =
                ViewModelProviders.of(this).get(ToDoListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo_list, container, false)
        val templateTextView: TextView = root.findViewById(R.id.text_todolist) //we will delete this textView in next step.
        toDoListViewModel.text.observe(viewLifecycleOwner, Observer {
            templateTextView.text = it
        })
        return root
    }
}