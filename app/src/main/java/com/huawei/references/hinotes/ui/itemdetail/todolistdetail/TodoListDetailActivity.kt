package com.huawei.references.hinotes.ui.itemdetail.todolistdetail

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.huawei.references.hinotes.ui.itemdetail.todolistdetail.adapter.TodoListSubItemsAdapter
import kotlinx.android.synthetic.main.activity_detail_todo_list.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class TodoListDetailActivity : ItemDetailBaseActivity() {

    private var isNewNote = true
    private lateinit var todoItem: Item

    private val viewModel: TodoListDetailViewModel by viewModel()
    private val subItems = ArrayList<TodoListSubItem>()
    private val todoListSubItemsAdapter= TodoListSubItemsAdapter(subItems)

    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        todoItem = intent?.extras?.let {
            if (it.containsKey(ITEM_KEY)) {
                isNewNote=false
                (intent.extras?.getSerializable(ITEM_KEY) as Item).apply {
                    getSubItems(itemId)
                }
            } else {
                createItem()
            }
        } ?: createItem()
        super.onCreate(savedInstanceState)

        observeDataHolderLiveData(viewModel.todoSubItemsLiveData){
            subItems.addAll(it)
            todoListSubItemsAdapter.notifyDataSetChanged()
        }
    }

    private fun getSubItems(itemId:Int){
        viewModel.getTodoSubItems(itemId)
    }

    override fun setupUI() {
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.item_detail_toolbar)
        }

        setContentView(R.layout.activity_detail_todo_list)

        todo_item_title.setText(todoItem.title)
        todo_list_item_checkbox.isChecked = todoItem.isChecked ?: false
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
            createSubItemAndAdd(if (isNewNote) -1 else todoItem.itemId)
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        microphone_icon.setOnClickListener {
        }

        delete_icon.setOnClickListener {
            if (!isNewNote) {
                runWithAGConnectUserOrOpenLogin {
                    //TODO: show are you sure popup, then delete
                    viewModel.deleteItem(todoItem, it.uid)
                }
            }
        }

        saveFab.setOnClickListener {
            runWithAGConnectUserOrOpenLogin {
                viewModel.saveItem(todoItem.apply {
                    todoListSubItems?.let {
                        it.clear()
                        it.addAll(subItems)
                    }
                    title = todo_item_title.text.toString()
                    isChecked = todo_list_item_checkbox.isChecked
                }, it.uid, isNewNote)
            }
        }
    }

    override fun onBackPressed() {
        //TODO: show usnsaved data will be lost popup,then finish
        super.onBackPressed()
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
        ""
    )

    companion object {
        const val ITEM_KEY = "ITEM_KEY"
    }
}