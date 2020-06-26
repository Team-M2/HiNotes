package com.huawei.references.hinotes.ui.notes

import android.content.Intent
import android.os.Bundle
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
import com.huawei.references.hinotes.ui.base.BaseFragment
import com.huawei.references.hinotes.ui.notedetail.DetailNoteActivity
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedMyNoteItemsList
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.selectedSharedNoteItemsList
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_notes.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class NotesFragment : BaseFragment(),SwipeRefreshLayout.OnRefreshListener {

    private val notesViewModel: NotesViewModel by viewModel()
    private var noteSectionedAdapter: SectionedRecyclerViewAdapter? = null

    companion object{
        var userMyNotesList:ArrayList<Item> = arrayListOf()
        var userSharedList:ArrayList<Item> = arrayListOf()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarTextView = activity!!.findViewById<View>(R.id.toolbar_title) as TextView
        val toolbarCancelIcon = activity!!.findViewById(R.id.toolbar_cancel_icon) as ImageView
        val toolbarDeleteIcon = activity!!.findViewById(R.id.toolbar_delete_icon) as ImageView
        setDefaultToolbar()
        toolbarTextView.text = resources.getText(R.string.title_toolbar_notes)
        notesViewModel.getNotes("1")
        noteSectionedAdapter = SectionedRecyclerViewAdapter()
        notes_recycler_view.layoutManager=LinearLayoutManager(context)
        notes_recycler_view.adapter=noteSectionedAdapter
        notes_swipe_refresh_layout.setOnRefreshListener(this)

        notesViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    if(userMyNotesList.size == 0 && userSharedList.size == 0) {
                        userMyNotesList.clear()
                        userSharedList.clear()
                        it.data.forEach {
                            userMyNotesList.add(it)
                            userSharedList.add(it)
                        }
                    }
                    notes_swipe_refresh_layout.isRefreshing=false
                    notes_progressbar.visibility=View.GONE
                    noteSectionedAdapter!!.addSection(
                        NoteSectionAdapter(
                            "My Notes",
                            userMyNotesList,this,0
                        )
                    )
                    noteSectionedAdapter!!.addSection(
                        NoteSectionAdapter(
                            "Shared Notes",
                            userSharedList,this,1
                        )
                    )
                    noteSectionedAdapter?.notifyDataSetChanged()
                    //notesAdapter.updateNotesList(it.data)
                }

                is DataHolder.Fail ->{
                    notes_swipe_refresh_layout.isRefreshing=false
                    notes_progressbar.visibility=View.GONE
                }

                is DataHolder.Loading ->{
                    notes_progressbar.visibility=View.VISIBLE
                }
            }
        })

        floatingActionNoteButton.setOnClickListener {
            val intent = Intent(activity, DetailNoteActivity::class.java)
            startActivity(intent)
        }

        toolbarCancelIcon.setOnClickListener {
            setDefaultToolbar()
            noteSectionedAdapter?.notifyDataSetChanged()
        }

        toolbarDeleteIcon.setOnClickListener {
            selectedMyNoteItemsList.sortDescending()
            selectedSharedNoteItemsList.sortDescending()
            selectedMyNoteItemsList.forEach{
                userMyNotesList.removeAt(it)
            }
            selectedSharedNoteItemsList.forEach{
                userSharedList.removeAt(it)
            }
            setDefaultToolbar()
            noteSectionedAdapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun setOnLongClickListener() {
        noteSectionedAdapter?.notifyDataSetChanged()
        super.setOnLongClickListener()
    }

    override fun onRefresh() {
        notesViewModel.getNotes("1")
        noteSectionedAdapter = SectionedRecyclerViewAdapter()
    }

    override fun onResume() {
        noteSectionedAdapter?.notifyDataSetChanged()
        super.onResume()
    }
}