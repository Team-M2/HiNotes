package com.huawei.references.hinotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.references.hinotes.ui.base.BaseActivity
import com.huawei.references.hinotes.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (AGConnectAuth.getInstance().currentUser == null) {
            openLoginActivity()
        }
        else{
            setupUI()
        }
    }

    private fun setupUI(){

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.main_toolbar)
            findViewById<ImageView>(R.id.toolbar_sign_out_icon).setOnClickListener {
                AGConnectAuth.getInstance().signOut()
                openLoginActivity()
            }
//            toolbar_sign_out_icon.setOnClickListener {
//                AGConnectAuth.getInstance().signOut()
//                openLoginActivity()
//            }
        }

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_notes, R.id.navigation_todo_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)



        toolbar_delete_icon.setOnClickListener {
            longClickedItemsList.forEach {
                Toast.makeText(this,"delete item index is -> $it",Toast.LENGTH_SHORT).show()
            }
        }

        toolbar_cancel_icon.setOnClickListener {

        }

//        toolbar_sign_out_icon.setOnClickListener {
//            AGConnectAuth.getInstance().signOut()
//            openLoginActivity()
//        }


    }

    private fun openLoginActivity() {
        startActivityForResult(
            Intent(this, LoginActivity::class.java),
            LOGIN_ACT_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_ACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setupUI()
            }
            else{
                //TODO: show login error after finish
                finish()
            }
        }
    }

    companion object {
        const val LOGIN_ACT_REQUEST_CODE = 333
    }
}

