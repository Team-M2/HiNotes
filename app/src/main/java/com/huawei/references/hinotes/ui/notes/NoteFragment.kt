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

class NoteFragment : Fragment() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        noteViewModel =
                ViewModelProviders.of(this).get(NoteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notes, container, false)
        val templateTextView: TextView = root.findViewById(R.id.text_notes) //we will delete this textView in next step.
        noteViewModel.text.observe(viewLifecycleOwner, Observer {
            templateTextView.text = it
        })
        return root
    }
}