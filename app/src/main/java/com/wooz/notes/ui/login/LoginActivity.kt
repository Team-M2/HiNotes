package com.wooz.notes.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.auth.*
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import com.wooz.notes.MainActivity
import com.wooz.notes.R


open class LoginActivity : AppCompatActivity(){

    private var auth = AGConnectAuth.getInstance()
    private val SIGN_CODE = 9901

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /*supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        */
        val navView: BottomNavigationView = findViewById(R.id.nav_view_login)
        val navController = findNavController(R.id.nav_host_login_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.loginFragmentMenu, R.id.registerFragmentMenu))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    private fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_CODE) {
            val authHuaweiIdTask =
                HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful) {
                val huaweiAccount = authHuaweiIdTask.result
                Log.i(
                    "Loginn", "accessToken:" + huaweiAccount.accessToken
                )
                val credential =
                    HwIdAuthProvider.credentialWithToken(huaweiAccount.accessToken)
                auth.signIn(credential)
                    .addOnSuccessListener { loginSuccess() }
                    .addOnFailureListener { e -> Log.e("Loginn",e.toString()) }
            } else {
                Log.e(
                    "Loginn",
                    "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode
                )
            }
        }
    }

    private fun loginSuccess() {
        Log.i("Loginn",auth.currentUser.displayName)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}