package com.huawei.references.hinotes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.base.BaseFragment

class NotesFragment : BaseFragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notes, container, false)
        val templateTextView: TextView = root.findViewById(R.id.text_notes) //we will delete this textView in next step.
        notesViewModel.text.observe(viewLifecycleOwner, Observer {
            templateTextView.text = it
        })
        return root
    }
}