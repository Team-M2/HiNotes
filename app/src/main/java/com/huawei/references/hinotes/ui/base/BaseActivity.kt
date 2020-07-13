package com.huawei.references.hinotes.ui.base

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.itemdetail.notedetail.LocationBottomSheetFragment
import com.huawei.references.hinotes.ui.login.LoginActivity

abstract class BaseActivity: AppCompatActivity() {

    protected val agConnectAuth :AGConnectAuth? = AGConnectAuth.getInstance()

    protected var contentViewIsSet=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AGConnectAuth.getInstance().currentUser == null) {
            openLoginActivity()
        }
        else{
            setupUI()
        }
    }

    protected open fun setupUI() = Unit

    fun openLoginActivity() {
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
                customToast(this,this.getString(R.string.failed_to_login),true)
                finish()
            }
        }
    }

    companion object {
        const val LOGIN_ACT_REQUEST_CODE = 333
    }
}