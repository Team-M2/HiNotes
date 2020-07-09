package com.huawei.references.hinotes.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.custom_warning_toast.*
import kotlinx.android.synthetic.main.note_detail_popup.view.*

private var positiveButtonClickEvent :  () -> Unit = {}
private var negativeButtonClickEvent :  () -> Unit = {}
private var positiveButtonText: String?=null
private var negativeButtonText: String?=null

fun View.show(){
    this.visibility=View.VISIBLE
}

fun View.hide(){
    this.visibility=View.GONE
}

fun customToast(context:Activity, toastMessage:String, isWarningToast:Boolean){
    val inflater = context.layoutInflater
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    val layout = inflater.inflate(
        R.layout.custom_warning_toast,
        context.custom_toast_constraint)
    val toastTextView: TextView = layout.findViewById<View>(R.id.custom_toast_text) as TextView
    val toastImageView: ImageView = layout.findViewById<View>(R.id.custom_toast_image) as ImageView
    if(!isWarningToast){
        toastImageView.setImageDrawable(context.getDrawable(R.drawable.info_icon))
        toast.duration = Toast.LENGTH_SHORT
    }
    toastTextView.text = toastMessage
    toast.setGravity(Gravity.CENTER_VERTICAL,0,0)
    toast.view = layout
    toast.show()
}

fun customPopup(popupText:String,positiveButtonText : String, negativeButtonText : String, clickEvent :() -> Unit = {}, context: Activity){
    val mDialogView = LayoutInflater.from(context).inflate(R.layout.note_detail_popup, null)

    mDialogView.popup_title.text = popupText
    mDialogView.accept_popup.text = positiveButtonText
    mDialogView.reject_popup.text = negativeButtonText

    val mBuilder = AlertDialog
        .Builder(context)
        .setView(mDialogView)
    val mAlertDialog = mBuilder.show()

    mAlertDialog.window?.setLayout(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    mDialogView.reject_popup.setOnClickListener {
        mAlertDialog.dismiss()
    }

    mDialogView.accept_popup.setOnClickListener {
        clickEvent.invoke()
    }
}

fun HashMap<Item,Boolean>.isAllFalse() : Boolean =
    this.filter { it.value }.isEmpty()
