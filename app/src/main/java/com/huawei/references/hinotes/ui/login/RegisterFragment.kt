package com.huawei.references.hinotes.ui.login

import android.os.Bundle
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
import com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hmf.tasks.TaskExecutors
import com.wooz.hinotes.R
import java.util.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val registerView = inflater.inflate(R.layout.fragment_register, container, false)
        val edtEmail = registerView.findViewById<EditText>(R.id.edt_email)
        val edtPass = registerView.findViewById<EditText>(R.id.edt_pass)
        val edtCode = registerView.findViewById<EditText>(R.id.edt_code)
        val registerBtn = registerView.findViewById<Button>(R.id.hwid_register)
        val codeBtn = registerView.findViewById<Button>(R.id.hwid_code)
        val txtForgot = registerView.findViewById<TextView>(R.id.hwid_repass)

        registerBtn.setOnClickListener {
            val emailUser: EmailUser = EmailUser.Builder()
                .setEmail(edtEmail.text.toString())
                .setVerifyCode(edtCode.text.toString())
                .setPassword(edtPass.text.toString())
                .build()
            AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener {
                    Toast.makeText(context, "Account created successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {e -> Log.e("register",e.toString())}
        }

        codeBtn.setOnClickListener {
            if(edtEmail.text != null){
                val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN) //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                    .sendInterval(360) // Minimum sending interval, ranging from 30s to 120s.
                    .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                    .build()

                val task: Task<VerifyCodeResult> =
                    EmailAuthProvider.requestVerifyCode(edtEmail.text.toString(), settings)
                task.addOnSuccessListener(
                    TaskExecutors.uiThread(),
                    OnSuccessListener { Toast.makeText(context, "Check your email for code", Toast.LENGTH_LONG).show()}).addOnFailureListener(
                    TaskExecutors.uiThread(),
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

        return registerView
    }

}