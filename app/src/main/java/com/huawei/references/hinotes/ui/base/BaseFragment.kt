package com.huawei.references.hinotes.ui.base

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.notes.adapter.IOnLongClickListener
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedMyNoteItemsList
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedSharedNoteItemsList
import com.huawei.references.hinotes.ui.todolist.adapter.TodoListSectionAdapter.Companion.selectedTodoMyItemsList
import com.huawei.references.hinotes.ui.todolist.adapter.TodoListSectionAdapter.Companion.selectedTodoSharedItemsList

open class BaseFragment: Fragment(), IOnLongClickListener {
    override fun setOnLongClickListener() {
        val deleteIcon: ImageView = activity!!.findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = activity!!.findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = activity!!.findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.VISIBLE
        cancelIcon.visibility = View.VISIBLE
        signOutIcon.visibility = View.GONE
    }

    fun setDefaultToolbar(){
        val deleteIcon: ImageView = activity!!.findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = activity!!.findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = activity!!.findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.GONE
        cancelIcon.visibility = View.GONE
        signOutIcon.visibility = View.VISIBLE
        selectedMyNoteItemsList.clear()
        selectedSharedNoteItemsList.clear()
        selectedTodoMyItemsList.clear()
        selectedTodoSharedItemsList.clear()
    }

}