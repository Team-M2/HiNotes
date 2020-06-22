package com.huawei.references.hinotes.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.references.hinotes.MainActivity
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.ui.base.BaseFragment
import com.huawei.references.hinotes.ui.notedetail.DetailNoteActivity
import com.huawei.references.hinotes.ui.notes.adapter.IOnLongClickListener
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_notes.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotesFragment : BaseFragment(), IOnLongClickListener {

    private val notesViewModel: NotesViewModel by viewModel()
    private var noteSectionedAdapter: SectionedRecyclerViewAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarTextView = activity!!.findViewById<View>(R.id.toolbar_title) as TextView
        toolbarTextView.text = "My Notes"
        notesViewModel.getNotes("1")
        noteSectionedAdapter = SectionedRecyclerViewAdapter()
        notes_recycler_view.layoutManager=LinearLayoutManager(context)
        notes_recycler_view.adapter=noteSectionedAdapter

        notesViewModel.text.observe(viewLifecycleOwner, Observer {
            textNotes.text = it
        })

        notesViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    // populate list with data, hide loading indicator
                    it.data.forEach {
                    /*    Toast.makeText(requireContext(),
                            it.toString(),
                            Toast.LENGTH_SHORT).show()
                     */
                }
                    noteSectionedAdapter!!.addSection(
                        NoteSectionAdapter(
                            "My Notes",
                            it.data,this
                        )
                    )
                    noteSectionedAdapter!!.addSection(
                        NoteSectionAdapter(
                            "Shared Notes",
                            it.data,this
                        )
                    )
                    noteSectionedAdapter?.notifyDataSetChanged()
                    //notesAdapter.updateNotesList(it.data)
                }

                is DataHolder.Fail ->{
                    // show error indicator
                }

                is DataHolder.Loading ->{
                    // show loading indicator
                }
            }
        })

        floatingActionNoteButton.setOnClickListener {
            val intent = Intent(activity, DetailNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun setOnLongClickListener() {
        noteSectionedAdapter?.notifyDataSetChanged()
        val deleteIcon: ImageView = activity!!.findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = activity!!.findViewById(R.id.toolbar_cancel_icon)
        deleteIcon.visibility = View.VISIBLE
        cancelIcon.visibility = View.VISIBLE
    }
}