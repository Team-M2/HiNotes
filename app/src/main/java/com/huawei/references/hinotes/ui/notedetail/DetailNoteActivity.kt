package com.huawei.references.hinotes.ui.notedetail

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailNoteActivity : BaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        val auth = AGConnectAuth.getInstance()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)

        val noteItemData = intent.extras?.getSerializable("clickedItemData") as Item?

        if(noteItemData!=null) {
            note_detail_title.setText(noteItemData.title)
            note_detail_description.setText(noteItemData.poiDescription.toString())
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        saveFab.setOnClickListener {
            //TODO: empty note title and description check
            //TODO: if current user is null navigate to login activity
            val itemToSave=noteItemData?.apply {
                title=note_detail_title.text.toString()
                poiDescription=note_detail_description.text.toString()
            } ?: Item(type = ItemType.Note,title = note_detail_title.text.toString())
            viewModel.saveItem(itemToSave,auth.currentUser.uid)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}