package com.huawei.references.hinotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
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

    @SuppressLint("BatteryLife")
    override fun setupUI(){
        if(!contentViewIsSet){
            contentViewIsSet=true
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
        }

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_notes, R.id.navigation_todo_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.apply {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener {  }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm =
                getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }

}

