package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.*
import com.huawei.references.hinotes.ui.base.customPopup
import com.huawei.references.hinotes.ui.base.formattedToString
import com.huawei.references.hinotes.ui.base.hide
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.adapter.TodoListSubItemsAdapter
import kotlinx.android.synthetic.main.activity_detail_todo_list.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class TodoListDetailActivity : ItemDetailBaseActivity() {

    private val viewModel: TodoListDetailViewModel by viewModel()
    private val subItems = ArrayList<TodoListSubItem>()
    private val subItemIdsToDelete = ArrayList<Int>()
    private val reminderIdsToDelete = ArrayList<Int>()
    private val todoListSubItemsAdapter = TodoListSubItemsAdapter(subItems,subItemIdsToDelete)

    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        itemData = intent?.extras?.let {
            if (it.containsKey(ITEM_KEY)) {
                isNewNote=false
                (intent.extras?.getSerializable(ITEM_KEY) as Item)
            } else {
                createItem()
            }
        } ?: createItem()
        super.onCreate(savedInstanceState)

        observeDataHolderLiveData(viewModel.todoSubItemsLiveData){
            subItems.clear()
            subItems.addAll(it)
            itemData.todoListSubItems?.apply {
                clear()
                addAll(it)
            }
            todoListSubItemsAdapter.notifyDataSetChanged()
        }
        if (isNewNote)
            detail_progress_bar.hide()
    }

    override fun onSaveSuccessful() {
        viewModel.getReminders(itemData.itemId)
    }

    override fun onGetRemindersCompleted() {
        super.onGetRemindersCompleted()
        viewModel.getTodoSubItems(itemData.itemId)
    }

    override fun setupUI() {
        if(!contentViewIsSet){
            contentViewIsSet=true
            setContentView(R.layout.activity_detail_todo_list)
        }
        super.setupUI()
        todo_list_item_checkbox.isChecked = itemData.isChecked ?: false
        todo_list_sub_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@TodoListDetailActivity)
            adapter = todoListSubItemsAdapter
        }

        todo_list_item_checkbox.setOnCheckedChangeListener { _, isChecked ->
            subItems.forEach {
                it.isChecked = isChecked
            }
            todoListSubItemsAdapter.notifyDataSetChanged()
        }

        add_todo_item.setOnClickListener {
            createSubItemAndAdd(if (isNewNote) -1 else itemData.itemId)
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if (!isNewNote) {
                runWithAGConnectUserOrOpenLogin {
                    customPopup(this.getString(R.string.delete_todo_list_popup_warning),
                        this.getString(R.string.delete_todo_list_popup_accept),
                        this.getString(R.string.delete_todo_list_popup_reject)
                    ) { viewModel.deleteItem(itemData, it.uid) }
                }
            }
        }

        saveFab.setOnClickListener {
            runWithAGConnectUserOrOpenLogin {
                viewModel.saveItem(itemData.apply {
                    todoListSubItems?.let {
                        it.clear()
                        it.addAll(subItems)
                    }
                    title = findViewById<TextView>(R.id.item_detail_title)?.text.toString()
                    description= findViewById<TextView>(R.id.item_detail_description)?.text.toString()
                    isChecked = todo_list_item_checkbox.isChecked
                }, it.uid, isNewNote, subItemIdsToDelete.map { it },reminderIdsToDelete)
                subItemIdsToDelete.clear()
            }
        }
    }

    private fun createSubItemAndAdd(itemId: Int) {
        subItems.add(
            TodoListSubItem(
                -1,
                itemId,
                Date(),
                Date(),
                "",
                false
            )
        )
        todoListSubItemsAdapter.notifyDataSetChanged()
    }

    private fun createItem() = Item(
        -1,
        Date(),
        Date(),
        ItemType.TodoList,
        false,
        null,
        null,
        "",
        "",
        "",
        "",
        reminder = Reminder(-1,
            -1,
            "reminderTitle",
            null,
            null,
            Date().formattedToString(),
            ReminderType.ByTime)
    )

    companion object {
        const val ITEM_KEY = "ITEM_KEY"
    }
}