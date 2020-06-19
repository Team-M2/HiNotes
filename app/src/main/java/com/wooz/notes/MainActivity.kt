package com.wooz.notes

import android.content.Intent
import android.os.Bundle
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.auth.AGConnectAuth
import com.wooz.notes.adapter.NoteSectionAdapter
import com.wooz.notes.ui.base.BaseActivity
import com.wooz.notes.ui.login.LoginActivity
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.main_toolbar)
     /*
     val auth = AGConnectAuth.getInstance().currentUser
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
            NoteSectionAdapter.longClickedItems?.forEach {
                Toast.makeText(this,"deleted item index -> $it",Toast.LENGTH_SHORT).show()
            }
        }

        toolbar_sign_out_icon.setOnClickListener {

        }
    }
}