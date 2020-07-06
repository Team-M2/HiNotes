package com.huawei.references.hinotes

import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.references.hinotes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun setupUI(){

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.main_toolbar)
            findViewById<ImageView>(R.id.toolbar_sign_out_icon).setOnClickListener {
                AGConnectAuth.getInstance().signOut()
                openLoginActivity()
            }
        }

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_notes, R.id.navigation_todo_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

    }

}

