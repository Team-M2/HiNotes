package com.huawei.references.hinotes.ui.base

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.notes.adapter.IOnLongClickListener
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedMyNoteItemsList
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedSharedNoteItemsList
import com.huawei.references.hinotes.ui.todolist.adapter.TodoListSectionAdapter.Companion.selectedTodoMyItemsList
import com.huawei.references.hinotes.ui.todolist.adapter.TodoListSectionAdapter.Companion.selectedTodoSharedItemsList

open class BaseFragment: Fragment(), IOnLongClickListener {

    lateinit var toolbarTextView :TextView
    lateinit var toolbarCancelIcon :ImageView
    lateinit var toolbarDeleteIcon :ImageView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarTextView=requireActivity().findViewById(R.id.toolbar_title)
        toolbarCancelIcon=requireActivity().findViewById(R.id.toolbar_cancel_icon)
        toolbarDeleteIcon=requireActivity().findViewById(R.id.toolbar_delete_icon)
    }

    override fun setOnLongClickListener() {
        val deleteIcon: ImageView = requireActivity().findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = requireActivity().findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = requireActivity().findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.VISIBLE
        cancelIcon.visibility = View.VISIBLE
        signOutIcon.visibility = View.GONE
    }

    fun setDefaultToolbar(){
        val deleteIcon: ImageView = requireActivity().findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = requireActivity().findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = requireActivity().findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.GONE
        cancelIcon.visibility = View.GONE
        signOutIcon.visibility = View.VISIBLE
        selectedMyNoteItemsList.clear()
        selectedSharedNoteItemsList.clear()
        selectedTodoMyItemsList.clear()
        selectedTodoSharedItemsList.clear()
    }

}