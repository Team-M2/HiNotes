package com.huawei.references.hinotes.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.api.entity.hwid.HwIDConstant
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_sheet.*


open class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        btn_to_login.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                val bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag)
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        btn_to_register.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                val bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag)
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        hwid_signin.setOnClickListener {
            val huaweiIdAuthParamsHelper =
                HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            val scopeList: MutableList<Scope> = ArrayList()
            scopeList.add(Scope(HwIDConstant.SCOPE.ACCOUNT_BASEPROFILE))
            huaweiIdAuthParamsHelper.setScopeList(scopeList)
            val authParams = huaweiIdAuthParamsHelper.setAccessToken().createParams()
            HuaweiIdAuthManager.getService(this@LoginActivity, authParams)?.apply {
                startActivityForResult(signInIntent, SIGN_CODE)
            }
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
                    LOGIN_TAG, "accessToken:" + huaweiAccount.accessToken
                )
                val credential =
                    HwIdAuthProvider.credentialWithToken(huaweiAccount.accessToken)
                AGConnectAuth.getInstance().signIn(credential)
                    .addOnSuccessListener { loginSuccess() }
                    .addOnFailureListener {
                        loginFail()
                    }
            } else {
                Log.e(
                    LOGIN_TAG,
                    "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode
                )
            }
        }
    }

    private fun loginSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun loginFail() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object{
        const val LOGIN_TAG = "Loginn"
        const val SIGN_CODE = 9901
    }
}