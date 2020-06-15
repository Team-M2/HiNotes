package com.huawei.references.hinotes.ui.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToDoListsFragment : BaseFragment() {

    private val toDoListsViewModel: ToDoListsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_todo_list, container, false)
        val templateTextView: TextView = root.findViewById(R.id.text_todolist) //we will delete this textView in next step.
        toDoListsViewModel.text.observe(viewLifecycleOwner, Observer {
            templateTextView.text = it
        })
        return root
    }
}