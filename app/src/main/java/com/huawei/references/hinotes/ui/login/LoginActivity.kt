package com.huawei.references.hinotes.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.FacebookAuthProvider
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.api.entity.hwid.HwIDConstant
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import com.huawei.references.hinotes.MainActivity
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.activity_login.*


open class LoginActivity : AppCompatActivity(){
    private val SIGN_CODE = 9901
    private val LINK_CODE = 9902
    var auth = AGConnectAuth.getInstance()
    var service: HuaweiIdAuthService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val huaweiIdAuthParamsHelper =
            HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
        val scopeList: MutableList<Scope> = ArrayList()
        scopeList.add(Scope(HwIDConstant.SCOPE.ACCOUNT_BASEPROFILE))
        huaweiIdAuthParamsHelper.setScopeList(scopeList)
        val authParams = huaweiIdAuthParamsHelper.setAccessToken().createParams()
        service = HuaweiIdAuthManager.getService(this@LoginActivity, authParams)

        hwid_signin.setOnClickListener {
            startActivityForResult(service!!.signInIntent, SIGN_CODE)
        }

        face_signin.setOnClickListener {
            val callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val token = loginResult.accessToken.token
                        val credential =
                            FacebookAuthProvider.credentialWithToken(token)
                        auth.signIn(credential)
                            .addOnSuccessListener { loginSuccess() }
                            .addOnFailureListener { e -> Log.e("facebookk",e.toString())  }
                    }

                    override fun onCancel() {
                      //  showToast("Cancel")
                    }

                    override fun onError(error: FacebookException) {
                     //   showToast(error.message)
                    }
                })
        }

        hwid_signout.setOnClickListener {
            signOut()
        }
    }

    private fun faceSignIn() {

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