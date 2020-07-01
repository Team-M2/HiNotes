package com.huawei.references.hinotes.ui.notedetail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.notes.NotesFragment
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.choose_image_direction.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import java.io.IOException
import java.util.*


class DetailNoteActivity : AppCompatActivity() {
    private var isNewNote=true
    private var noteItemData :Item ?= null
    private var noteItemIndex :Int ?= null
    private var noteItemSectionIndex :Int ?= null
    private val cameraRequestCode = 101
    private val takePictureResultCode = 201
    private val storageRequestCode = 102
    private val pickImageResultCode = 202

    private val recordAudioRequestCode = 103
    private val recordAudioResultCode = 203

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        val auth = AGConnectAuth.getInstance()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.item_detail_toolbar)
        noteItemData = intent.extras?.getSerializable("clickedItemData") as Item?
        noteItemSectionIndex = intent.extras?.getSerializable("clickedItemSection") as Int?
        noteItemIndex = intent.extras?.getSerializable("clickedIndex") as Int?

        if(noteItemData!=null) {
            isNewNote=false
            note_detail_title.setText(noteItemData?.title)
            note_detail_description.setText(noteItemData?.poiDescription.toString())
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if(noteItemSectionIndex == 0) {
                noteItemIndex?.let { it1 -> NotesFragment.userMyNotesList.removeAt(it1) }
            }
            else{
                noteItemIndex?.let { it1 -> NotesFragment.userSharedList.removeAt(it1) }
            }
            finish()
        }

        microphone_icon.setOnClickListener {
            performSpeechToText()
        }

        image_icon.setOnClickListener {
            hideKeyboard()
            include_choose_image.visibility= View.VISIBLE
        }

        choose_take_picture_text.setOnClickListener {
            performTakePicture()
        }

        choose_pick_image_text.setOnClickListener {
            performPickImage()
        }

        choose_image_cancel.setOnClickListener {
            include_choose_image.visibility= View.GONE
        }

        choose_event_background.setOnClickListener {
            include_choose_image.visibility= View.GONE
        }

        saveFab.setOnClickListener {
            //TODO: empty note title and description check
            //TODO: if current user is null navigate to login activity
            val itemToSave=noteItemData?.apply {
                title=note_detail_title.text.toString()
                poiDescription=note_detail_description.text.toString()
            } ?: Item(type = ItemType.Note,title = note_detail_title.text.toString())
            viewModel.saveItem(itemToSave,"user1")
        }
    }

    override fun onBackPressed() {
        if(isNewNote){
            createNote()
        }
        else{
            saveNoteChanges()
        }
        super.onBackPressed()
    }

    private fun saveNoteChanges(){
        if(noteItemSectionIndex==0) {
                NotesFragment.userMyNotesList[noteItemIndex!!].title = note_detail_title.text.toString()
                NotesFragment.userMyNotesList[noteItemIndex!!].poiDescription = note_detail_description.text.toString()
            }
            else{
                NotesFragment.userSharedList[noteItemIndex!!].title = note_detail_title.text.toString()
                NotesFragment.userSharedList[noteItemIndex!!].poiDescription = note_detail_description.text.toString()
        }
    }

    private fun createNote(){
        NotesFragment.userMyNotesList.add(0,Item(11, Date(),Date(),ItemType.Note,false,0.0,0.0,note_detail_description.text.toString(),note_detail_title.text.toString(),
            arrayListOf(),false,UserRole.Owner,false))
    }

    private fun performTakePicture(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, takePictureResultCode)
        }
    }

    private fun performPickImage(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storageRequestCode)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageResultCode)
        }
    }

    private fun performTextRecognition(selectedImageBitmap:Bitmap){
        val setting = MLLocalTextSetting.Factory()
            .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE) // Specify languages that can be recognized.
            .setLanguage("en")
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)
        val frame = MLFrame.fromBitmap(selectedImageBitmap)

        val task: Task<MLText> = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            if(it.stringValue == ""){
                Toast.makeText(this,this.getString(R.string.text_recognition_could_not_read),Toast.LENGTH_LONG).show()
            }
            val resultText=it.stringValue.replace("\n"," ")
            include_choose_image.visibility= View.GONE
            note_detail_description.text = note_detail_description.text.append(resultText)
        }.addOnFailureListener {
            Toast.makeText(this,this.getString(R.string.text_recognition_failed),Toast.LENGTH_SHORT).show()
            include_choose_image.visibility= View.GONE
        }
    }

    private fun performSpeechToText(){
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO), recordAudioRequestCode)
        } else {
            val intent = Intent(this, MLAsrCaptureActivity::class.java)
                .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
            startActivityForResult(intent, recordAudioResultCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performTakePicture()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == storageRequestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performPickImage()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == recordAudioRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                performSpeechToText()
            }
            else {
                Toast.makeText(this, "Record audio permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == takePictureResultCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.data == null) {
                    performTextRecognition(data?.extras!!["data"] as Bitmap)
                } else {
                    performTextRecognition(MediaStore.Images.Media.getBitmap( this.contentResolver,data.data))
                }
            }
        }
        if (requestCode == pickImageResultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val pickedImage: Uri? = data?.data
                try {
                    performTextRecognition(MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (requestCode == recordAudioResultCode) {
            when (resultCode) {
                MLAsrCaptureConstants.ASR_SUCCESS -> if (data != null) {
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                        val text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT).toString()
                        note_detail_description.append(text)
                    }
                }
                MLAsrCaptureConstants.ASR_FAILURE -> if (data != null) {
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                        val errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE)
                        Log.e("SpeechToTextCode",  errorCode.toString())
                    }
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)) {
                        val errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)
                        Log.e("SpeechToTextMessage", errorMsg.toString())
                    }
                    Toast.makeText(this, this.getString(R.string.speech_to_text_failed),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}