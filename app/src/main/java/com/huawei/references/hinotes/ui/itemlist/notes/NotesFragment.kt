package com.huawei.references.hinotes.ui.itemlist.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.ui.itemdetail.notedetail.DetailNoteActivity
import com.huawei.references.hinotes.ui.itemlist.ItemListBaseFragment
import com.huawei.references.hinotes.ui.itemlist.ItemListViewModel
import com.huawei.references.hinotes.ui.itemlist.SectionAdapter
import com.huawei.references.hinotes.ui.itemlist.notes.adapter.NoteSectionAdapter
import kotlinx.android.synthetic.main.fragment_notes.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class NotesFragment : ItemListBaseFragment() {

    override fun getRecyclerView(): RecyclerView = notes_recycler_view

    override val myItemsSectionAdapter: SectionAdapter = NoteSectionAdapter(
        "My Notes",
        this)


    override val sharedItemsSectionAdapter: SectionAdapter = NoteSectionAdapter(
        "Shared Notes",
        this
    )
    override val pageTitle: Int = R.string.title_toolbar_notes

    override fun getItemListViewModel(): ItemListViewModel = getViewModel()
    override val itemType: ItemType = ItemType.Note

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        floatingActionNoteButton.setOnClickListener {
            startActivity(Intent(activity, DetailNoteActivity::class.java))
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun setOnLongClickListener() {
        sectionedAdapter.notifyDataSetChanged()
        super.setOnLongClickListener()
    }

    override fun getData() {
        runWithAGConnectUserOrOpenLogin {
            getItemListViewModel().getItems(it.uid,ItemType.Note)
        }
    }
}