package com.huawei.references.hinotes.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.huawei.agconnect.auth.*
import com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN
import com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hmf.tasks.TaskExecutors
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.api.entity.hwid.HwIDConstant
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton
import com.wooz.hinotes.MainActivity
import com.wooz.hinotes.R
import java.util.*
import kotlin.collections.ArrayList

private var auth = AGConnectAuth.getInstance()
var service: HuaweiIdAuthService? = null
private val SIGN_CODE = 9901

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginView = inflater.inflate(R.layout.fragment_login, container, false)
        val hwidBtn = loginView.findViewById<HuaweiIdAuthButton>(R.id.hwid_signin)
        val edtEmail = loginView.findViewById<EditText>(R.id.edt_email)
        val edtPass = loginView.findViewById<EditText>(R.id.edt_pass)
        val edtCode = loginView.findViewById<EditText>(R.id.edt_code)
        val loginBtn = loginView.findViewById<Button>(R.id.btn_login)
        val codeBtn = loginView.findViewById<Button>(R.id.hwid_code)
        val txtForgot = loginView.findViewById<TextView>(R.id.hwid_repass)

        hwidBtn.setOnClickListener {
            val huaweiIdAuthParamsHelper =
                HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            val scopeList: MutableList<Scope> = ArrayList()
            scopeList.add(Scope(HwIDConstant.SCOPE.ACCOUNT_BASEPROFILE))
            huaweiIdAuthParamsHelper.setScopeList(scopeList)
            val authParams = huaweiIdAuthParamsHelper.setAccessToken().createParams()
            service = HuaweiIdAuthManager.getService(activity, authParams)
            startActivityForResult(service!!.signInIntent, SIGN_CODE)
        }

        loginBtn.setOnClickListener {
            val credential: AGConnectAuthCredential = if (TextUtils.isEmpty(edtCode.text)) {
                EmailAuthProvider.credentialWithPassword(edtEmail.text.toString(), edtPass.text.toString())
            } else {
                EmailAuthProvider.credentialWithVerifyCode(edtEmail.text.toString(), edtPass.text.toString(), edtCode.text.toString())
            }
            signIn(credential)
        }

        codeBtn.setOnClickListener {
            if(edtEmail.text != null){
                val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                    .action(ACTION_REGISTER_LOGIN) //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                    .sendInterval(360) // Minimum sending interval, ranging from 30s to 120s.
                    .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                    .build()

                val task: Task<VerifyCodeResult> =
                    EmailAuthProvider.requestVerifyCode(edtEmail.text.toString(), settings)
                task.addOnSuccessListener(
                    TaskExecutors.uiThread(),
                    OnSuccessListener { Toast.makeText(context, "Check your email for code", Toast.LENGTH_LONG).show()}).addOnFailureListener(TaskExecutors.uiThread(),
                    OnFailureListener { e -> Log.e("code",e.toString()) })
            }
        }

        txtForgot.setOnClickListener {
            val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                .action(ACTION_RESET_PASSWORD) //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                .sendInterval(30) // Minimum sending interval, ranging from 30s to 120s.
                .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                .build()

            val task: Task<VerifyCodeResult> =
                EmailAuthProvider.requestVerifyCode(edtEmail.text.toString(), settings)
            task.addOnSuccessListener(
                TaskExecutors.uiThread(),
                OnSuccessListener { Toast.makeText(context, "Resend email password information", Toast.LENGTH_LONG).show() }).addOnFailureListener(TaskExecutors.uiThread(),
                OnFailureListener { e -> Log.e("recode",e.toString()) })
        }

        return loginView
    }

    private fun signIn(credential: AGConnectAuthCredential) {
        AGConnectAuth.getInstance().signIn(credential)
            .addOnSuccessListener {
                startActivity(Intent(activity, MainActivity::class.java))
                activity!!.finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "signIn fail:$e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginSuccess() {
        Log.i("loginsuccess",auth.currentUser.displayName)
        startActivity(Intent(activity, MainActivity::class.java))
        activity!!.finish()
    }
}