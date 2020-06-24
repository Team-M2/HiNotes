package com.huawei.references.hinotes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.references.hinotes.ui.base.BaseActivity
import com.huawei.references.hinotes.ui.notes.NotesFragment
import com.huawei.references.hinotes.ui.notes.adapter.NoteSectionAdapter.Companion.longClickedItemsList
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.note_item_list.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.main_toolbar)
        val auth = AGConnectAuth.getInstance().currentUser

        /*
        if(auth == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
         */

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_notes, R.id.navigation_todo_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        toolbar_delete_icon.setOnClickListener {
            longClickedItemsList.forEach {
                Toast.makeText(this,"delete item index is -> $it",Toast.LENGTH_SHORT).show()
            }
        }

        toolbar_cancel_icon.setOnClickListener{

        }

        toolbar_sign_out_icon.setOnClickListener {

        }
    }
}