package com.huawei.references.hinotes.ui.notedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.add_item_toolbar.*

class DetailNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.add_item_toolbar)

        val noteItemData = intent.extras?.getSerializable("clickedItemData") as Item?

        if(noteItemData!=null) {
            note_detail_title.setText(noteItemData.itemId.toString())
            note_detail_description.setText(noteItemData?.poiDescription.toString())
        }

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}