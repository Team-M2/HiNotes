package com.huawei.references.hinotes.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.agconnect.auth.*
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hmf.tasks.TaskExecutors
import com.huawei.references.hinotes.MainActivity
import com.huawei.references.hinotes.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.util.*

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginView = inflater.inflate(R.layout.bottom_sheet, container, false)
        val edtEmail = loginView.findViewById<EditText>(R.id.edt_email)
        val edtPass = loginView.findViewById<EditText>(R.id.edt_pass)
        val edtCode = loginView.findViewById<EditText>(R.id.edt_code)
        val loginBtn = loginView.findViewById<Button>(R.id.btn_login)
        val registerBtn = loginView.findViewById<Button>(R.id.btn_register)
        val codeBtn = loginView.findViewById<Button>(R.id.hwid_code)
        val txtForgot = loginView.findViewById<TextView>(R.id.hwid_repass)

        loginBtn.setOnClickListener {
            val credential: AGConnectAuthCredential = if (TextUtils.isEmpty(edt_code.text)) {
                EmailAuthProvider.credentialWithPassword(
                    edtEmail.text.toString(),
                    edtPass.text.toString()
                )
            } else {
                EmailAuthProvider.credentialWithVerifyCode(
                    edtEmail.text.toString(),
                    edtPass.text.toString(),
                    edtCode.text.toString()
                )
            }
            signIn(credential)
        }

        registerBtn.setOnClickListener {
            val emailUser: EmailUser = EmailUser.Builder()
                .setEmail(edtEmail.text.toString())
                .setVerifyCode(edtCode.text.toString())
                .setPassword(edtPass.text.toString())
                .build()
            AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener {
                    Toast.makeText(context, "Account created successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(context, MainActivity::class.java))
                    activity!!.finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
        }

        codeBtn.setOnClickListener {
            if (edt_email.text != null) {
                val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN) //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                    .sendInterval(360) // Minimum sending interval, ranging from 30s to 120s.
                    .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                    .build()

                val task: Task<VerifyCodeResult> =
                    EmailAuthProvider.requestVerifyCode(edtEmail.text.toString(), settings)
                task.addOnSuccessListener(
                    TaskExecutors.uiThread(),
                    OnSuccessListener {
                        Toast.makeText(
                            context,
                            "Check your email for code",
                            Toast.LENGTH_LONG
                        ).show()
                    }).addOnFailureListener(
                    TaskExecutors.uiThread(),
                    OnFailureListener { e -> Log.e("code", e.toString()) })
            }
        }

        txtForgot.setOnClickListener {
            val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_RESET_PASSWORD) //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                .sendInterval(30) // Minimum sending interval, ranging from 30s to 120s.
                .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                .build()

            val task: Task<VerifyCodeResult> =
                EmailAuthProvider.requestVerifyCode(edtEmail.text.toString(), settings)
            task.addOnSuccessListener(
                TaskExecutors.uiThread(),
                OnSuccessListener {
                    Toast.makeText(
                        context,
                        "Resend email password information",
                        Toast.LENGTH_LONG
                    ).show()
                }).addOnFailureListener(
                TaskExecutors.uiThread(),
                OnFailureListener { e -> Log.e("recode", e.toString()) })
        }
        return loginView
    }


    private fun signIn(credential: AGConnectAuthCredential) {
        AGConnectAuth.getInstance().signIn(credential)
            .addOnSuccessListener {
                startActivity(Intent(context, MainActivity::class.java))
                activity!!.finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "signIn fail:$e", Toast.LENGTH_SHORT).show()
            }
    }
}
