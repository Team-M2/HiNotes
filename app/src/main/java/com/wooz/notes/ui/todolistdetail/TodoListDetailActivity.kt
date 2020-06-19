package com.wooz.notes.ui.todolistdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.wooz.notes.R
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.ui.todolistdetail.adapter.TodoListSubItemsAdapter
import kotlinx.android.synthetic.main.activity_detail_todo_list.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import kotlinx.android.synthetic.main.todo_list_item_list.*

class TodoListDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_todo_list)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)

        val todoListItemData = intent.extras?.getSerializable("clickedItemData") as Item?
        val todoListSubItemsAdapter =
            TodoListSubItemsAdapter(todoListItemData?.todoListSubItems)
        if(todoListItemData != null) {
            todo_item_title.setText(todoListItemData.title)
            todo_list_item_checkbox.isChecked= todoListItemData.isChecked!!
            todo_list_sub_recycler_view.layoutManager = LinearLayoutManager(this)
            todo_list_sub_recycler_view.adapter = todoListSubItemsAdapter
        }

        todo_list_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            todoListSubItemsAdapter.updateCheckBox(isChecked)
        }

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}