package com.huawei.references.hinotes.ui.notedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.wooz.hinotes.R
import com.wooz.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*

class DetailNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)

        val noteItemData = intent.extras?.getSerializable("clickedItemData") as Item?

        if(noteItemData!=null) {
            note_detail_title.setText(noteItemData.title.toString())
            note_detail_description.setText(noteItemData.poiDescription.toString())
        }

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}