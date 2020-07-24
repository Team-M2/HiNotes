package com.huawei.references.hinotes.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.javiersantos.bottomdialogs.BottomDialog
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import kotlinx.android.synthetic.main.choose_reminder_direction.view.*
import kotlinx.android.synthetic.main.custom_warning_toast.*
import kotlinx.android.synthetic.main.note_detail_popup.view.*
import java.text.SimpleDateFormat
import java.util.*


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

fun BaseActivity.customPopup(popupText:String,positiveButtonText : String, negativeButtonText : String, clickEvent :() -> Unit = {}){
    val mDialogView = LayoutInflater.from(this).inflate(R.layout.note_detail_popup,
        null).apply {
        popup_title.text = popupText
        accept_popup.text = positiveButtonText
        reject_popup.text = negativeButtonText
    }

    val mAlertDialog = AlertDialog
        .Builder(this)
        .setView(mDialogView).show()

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


fun BaseActivity.customBottomDialogs(firstButtonText : String, secondButtonText : String, firstIcon:Drawable?, secondIcon:Drawable?,  firstClickEvent :() -> Unit = {} , secondClickEvent :() -> Unit = {}){
    val mDialogView = LayoutInflater.from(this).inflate(R.layout.choose_reminder_direction,
        null).apply {
        this.choose_first_text.text = firstButtonText
        this.choose_second_text.text = secondButtonText
        this.choose_first_icon.setImageDrawable(firstIcon)
        this.choose_second_icon.setImageDrawable(secondIcon)
    }

    val bottomDialog = BottomDialog.Builder(this)
        .setTitle("Options")
        .setContent("Which method do you want to add reminders for?")
        .setCustomView(mDialogView)
        .show()

    mDialogView.choose_first_text.setOnClickListener {
        firstClickEvent.invoke()
        bottomDialog.dismiss()
    }

    mDialogView.choose_second_text.setOnClickListener {
        secondClickEvent.invoke()
        bottomDialog.dismiss()
    }

    mDialogView.choose_dialog_cancel.setOnClickListener {
        bottomDialog.dismiss()
    }
}



fun HashMap<Item,Boolean>.isAllFalse() : Boolean =
    this.filter { it.value }.isEmpty()

fun Date.formattedToString() : Date = object : Date(){
    override fun toString(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
}
