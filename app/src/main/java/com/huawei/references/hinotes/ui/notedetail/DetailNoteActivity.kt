package com.huawei.references.hinotes.ui.notedetail

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.base.BaseActivity
import com.huawei.references.hinotes.ui.notes.NotesFragment
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*





class DetailNoteActivity : BaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()
    private var isNewNote=true
    private var noteItemData :Item ?= null
    private var noteItemIndex :Int ?= null
    private var noteItemSectionIndex :Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        val auth = AGConnectAuth.getInstance()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)
        noteItemData = intent.extras?.getSerializable("clickedItemData") as Item?
        noteItemSectionIndex = intent.extras?.getSerializable("clickedItemSection") as Int?
        noteItemIndex = intent.extras?.getSerializable("clickedIndex") as Int?

        if(noteItemData!=null) {
            isNewNote=false
            note_detail_title.setText(noteItemData?.title)
            note_detail_description.setText(noteItemData?.poiDescription.toString())
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if(noteItemSectionIndex == 0) {
                noteItemIndex?.let { it1 -> NotesFragment.userMyNotesList.removeAt(it1) }
            }
            else{
                noteItemIndex?.let { it1 -> NotesFragment.userSharedList.removeAt(it1) }
            }
            finish()
        }

        saveFab.setOnClickListener {
            //TODO: empty note title and description check
            //TODO: if current user is null navigate to login activity
            val itemToSave=noteItemData?.apply {
                title=note_detail_title.text.toString()
                poiDescription=note_detail_description.text.toString()
            } ?: Item(type = ItemType.Note,title = note_detail_title.text.toString())
            viewModel.saveItem(itemToSave,"user1")
        }
    }

    override fun onBackPressed() {
        if(isNewNote){
            createNote()
        }
        else{
            saveNoteChanges()
        }
        super.onBackPressed()
    }

    private fun saveNoteChanges(){
        if(noteItemSectionIndex==0) {
                NotesFragment.userMyNotesList[noteItemIndex!!].title = note_detail_title.text.toString()
                NotesFragment.userMyNotesList[noteItemIndex!!].poiDescription = note_detail_description.text.toString()
            }
            else{
                NotesFragment.userSharedList[noteItemIndex!!].title = note_detail_title.text.toString()
                NotesFragment.userSharedList[noteItemIndex!!].poiDescription = note_detail_description.text.toString()
        }
    }

    private fun createNote(){
        NotesFragment.userMyNotesList.add(0,Item(11, Date(),Date(),ItemType.Note,false,0.0,0.0,note_detail_description.text.toString(),note_detail_title.text.toString(),
            arrayListOf(),false,UserRole.Owner,false))
    }
}