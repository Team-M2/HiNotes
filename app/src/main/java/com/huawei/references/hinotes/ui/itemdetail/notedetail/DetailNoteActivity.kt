package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.ui.base.customPopup
import com.huawei.references.hinotes.ui.base.customToast
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.judemanutd.autostarter.AutoStartPermissionHelper
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailNoteActivity : ItemDetailBaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()
    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        itemData=intent?.extras?.let{
            if(it.containsKey(ITEM_KEY)){
                (intent.extras?.getSerializable(ITEM_KEY) as Item).apply {
                    isNewNote=false
                }
            }
            else{
                createItem(ItemType.Note)
            }
        }?: createItem(ItemType.Note)
        super.onCreate(savedInstanceState)
    }

    override fun setupUI() {
        if(!contentViewIsSet){
            contentViewIsSet=true
            setContentView(R.layout.activity_detail_note)
        }
        super.setupUI()

        back_button.setOnClickListener {
            onBackPressed()
        }

        saveFab.setOnClickListener {
            if(findViewById<TextView>(R.id.item_detail_title)?.text.toString() == "" ||
                findViewById<TextView>(R.id.item_detail_description)
            ?.text.toString() == ""){
                customToast(getString(R.string.note_detail_check_title_description),true)
            }
            else {
                runWithAGConnectUserOrOpenLogin {
                    val itemToSave = itemData.apply {
                        title = findViewById<TextView>(R.id.item_detail_title)?.text.toString()
                        description= findViewById<TextView>(R.id.item_detail_description)?.text.toString()
                    }
                    noteDetailChanged = false
                    saveChanges(itemToSave)
                }
            }
        }

        findViewById<TextView>(R.id.item_detail_title)?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                noteDetailChanged=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        findViewById<TextView>(R.id.item_detail_description)
            ?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                noteDetailChanged=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    private fun saveChanges(item:Item){
        runWithAGConnectUserOrOpenLogin {
            viewModel.saveItem(item, it.uid, isNewNote, listOf(), listOf())
        }
    }

    override fun onBackPressed() {
        if(noteDetailChanged) {
            runWithAGConnectUserOrOpenLogin {
                customPopup(this.getString(R.string.delete_item_changes_popup_warning),
                    this.getString(R.string.delete_item_changes_popup_accept),
                    this.getString(R.string.delete_item_changes_popup_reject)
                ) {finish()}
            }
        }
        else{
            super.onBackPressed()
        }
    }

    companion object{
        const val ITEM_KEY="ITEM_KEY"
    }
}