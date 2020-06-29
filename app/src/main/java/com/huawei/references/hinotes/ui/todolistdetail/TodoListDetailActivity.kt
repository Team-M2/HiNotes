package com.huawei.references.hinotes.ui.todolistdetail

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.references.hinotes.ui.todolistdetail.adapter.TodoListSubItemsAdapter
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.notes.NotesFragment
import com.huawei.references.hinotes.ui.todolist.ToDoListsFragment
import com.huawei.references.hinotes.ui.todolistdetail.adapter.TodoListSubItemsAdapter
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.activity_detail_todo_list.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class TodoListDetailActivity : AppCompatActivity() {
    private var isNewNote=true
    private var todoItemData :Item ?= null
    private var todoItemIndex :Int ?= null
    private var todoItemSectionIndex :Int ?= null
    private var todoListSubItemsAdapter:TodoListSubItemsAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_todo_list)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)

        todoItemData = intent.extras?.getSerializable("clickedItemData") as Item?
        todoItemSectionIndex = intent.extras?.getSerializable("clickedItemSection") as Int?
        todoItemIndex = intent.extras?.getSerializable("clickedIndex") as Int?

        if(todoItemData != null) {
            isNewNote=false
            if(todoItemSectionIndex==0) {
                todoListSubItemsAdapter =
                    TodoListSubItemsAdapter(ToDoListsFragment.userMyTodoList[todoItemIndex!!].todoListSubItems)
            }
            else{
                todoListSubItemsAdapter =
                    TodoListSubItemsAdapter(ToDoListsFragment.userSharedTodoList[todoItemIndex!!].todoListSubItems)
            }
            todo_item_title.setText(todoItemData?.title)
            todo_list_item_checkbox.isChecked= todoItemData?.isChecked!!
            todo_list_sub_recycler_view.layoutManager = LinearLayoutManager(this)
            todo_list_sub_recycler_view.adapter = todoListSubItemsAdapter
        }
        else{
            todoItemIndex=0
            todoItemSectionIndex=0
            createTodoList()
            todoListSubItemsAdapter =
                TodoListSubItemsAdapter(ToDoListsFragment.userMyTodoList[0].todoListSubItems)

            todo_list_sub_recycler_view.layoutManager = LinearLayoutManager(this)
            todo_list_sub_recycler_view.adapter = todoListSubItemsAdapter
        }

        todo_list_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            todoListSubItemsAdapter?.updateCheckBox(isChecked)
        }

        add_todo_item.setOnClickListener {
            if(isNewNote){
                todoItemIndex=0
                createSubItem(ToDoListsFragment.userMyTodoList)
            }
            else if(todoItemSectionIndex == 0) {
                createSubItem(ToDoListsFragment.userMyTodoList)
            }
            else{
                createSubItem(ToDoListsFragment.userSharedTodoList)
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        microphone_icon.setOnClickListener {

        }

        delete_icon.setOnClickListener {
            if(todoItemSectionIndex == 0) {
                todoItemIndex?.let { it1 -> ToDoListsFragment.userMyTodoList.removeAt(it1) }
            }
            else{
                todoItemIndex?.let { it1 -> ToDoListsFragment.userSharedTodoList.removeAt(it1) }
            }
            finish()
        }
    }

    override fun onBackPressed() {
        saveTodoListChanges()
        super.onBackPressed()
    }

    private fun createTodoList(){
        ToDoListsFragment.userMyTodoList.add(0,Item(111, Date(),Date(),
            ItemType.TodoList,false,0.0,0.0,"",todo_item_title.text.toString(), arrayListOf(),false,
            UserRole.Owner,false))
    }

    private fun saveTodoListChanges(){
        if (todoItemSectionIndex == 0){
            ToDoListsFragment.userMyTodoList[todoItemIndex!!].title=todo_item_title.text.toString()
            ToDoListsFragment.userMyTodoList[todoItemIndex!!].isChecked=todo_list_item_checkbox.isChecked
        }
        else{
            ToDoListsFragment.userSharedTodoList[todoItemIndex!!].title=todo_item_title.text.toString()
            ToDoListsFragment.userSharedTodoList[todoItemIndex!!].isChecked=todo_list_item_checkbox.isChecked
        }
    }

    private fun createSubItem(todoItemList:ArrayList<Item>){
        todoItemList[todoItemIndex!!].todoListSubItems.add(
            TodoListSubItem(
                11,
                Date(),
                Date(),
                "",
                false
            )
        )
        todoListSubItemsAdapter?.notifyDataSetChanged()
    }
}