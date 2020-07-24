package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.github.javiersantos.bottomdialogs.BottomDialog
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.base.customBottomDialogs
import com.huawei.references.hinotes.ui.base.customPopup
import com.huawei.references.hinotes.ui.base.customToast
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class DetailNoteActivity : ItemDetailBaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()
    private val takePictureResultCode = 201
    private val pickImageResultCode = 202

    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel
    private val recordAudioResultCode = 203

    override fun onCreate(savedInstanceState: Bundle?) {
        itemData=intent?.extras?.let{
            if(it.containsKey(ITEM_KEY)){
                (intent.extras?.getSerializable(ITEM_KEY) as Item).apply {
                    isNewNote=false
                }
            }
            else{
                createNote()
            }
        }?: createNote()
        super.onCreate(savedInstanceState)
    }

    override fun setupUI() {
        if(!contentViewIsSet){
            contentViewIsSet=true
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                setCustomView(R.layout.item_detail_toolbar)
            }
            setContentView(R.layout.activity_detail_note)
        }

        note_detail_title.setText(itemData.title)
        note_detail_description.setText(itemData.poiDescription.toString())

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if(!isNewNote){
                runWithAGConnectUserOrOpenLogin {
                    customPopup(this.getString(R.string.delete_note_popup_warning),
                        this.getString(R.string.delete_note_popup_accept),
                        this.getString(R.string.delete_note_popup_reject)
                    ) { viewModel.deleteItem(itemData,it.uid) }
                }
            }
        }

        microphone_icon.setOnClickListener {
            performSpeechToText()
        }

        image_icon.setOnClickListener {
            hideKeyboard()
            //include_choose_image.visibility= View.VISIBLE
            customBottomDialogs("Take Picture","Pick Image",getDrawable(R.drawable.camera_icon),getDrawable(R.drawable.gallery_icon),{performTakePicture()},{performPickImage()})
        }

        saveFab.setOnClickListener {
            if(note_detail_title.text.toString() == "" || note_detail_description.text.toString() == ""){
                customToast(this,this.getString(R.string.note_detail_check_title_description),true)
            }
            else {
                runWithAGConnectUserOrOpenLogin {
                    val itemToSave = itemData.apply {
                        title = note_detail_title.text.toString()
                        poiDescription = note_detail_description.text.toString()
                    }
                    noteDetailChanged = false
                    saveChanges(itemToSave)
                }
            }
        }

        note_detail_title.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                noteDetailChanged=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        note_detail_description.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                noteDetailChanged=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        super.setupUI()
    }

    private fun saveChanges(item:Item){
        runWithAGConnectUserOrOpenLogin {
            viewModel.saveItem(item, it.uid, isNewNote)
        }
    }

    override fun onBackPressed() {
        if(noteDetailChanged) {
            runWithAGConnectUserOrOpenLogin {
                customPopup(this.getString(R.string.delete_item_changes_popup_warning),
                    this.getString(R.string.delete_item_changes_popup_accept),
                    this.getString(R.string.delete_item_changes_popup_reject)
                ) {finish()}
            }
        }
        else{
            super.onBackPressed()
        }
    }

    private fun createNote()=Item(11, Date(),Date(),ItemType.Note,false,0.0,0.0,"","","","",
        arrayListOf(),false,UserRole.Owner, null,false)

    private fun performTakePicture() =
        runWithPermissions(Manifest.permission.CAMERA){
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), takePictureResultCode)
        }

    private fun performPickImage() =
        runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE){
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageResultCode)
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
            note_detail_description.text = note_detail_description.text.append(resultText)
        }.addOnFailureListener {
            Toast.makeText(this,this.getString(R.string.text_recognition_failed),Toast.LENGTH_SHORT).show()
        }
    }

    private fun performSpeechToText() = runWithPermissions(Manifest.permission.RECORD_AUDIO){
        val intent = Intent(this, MLAsrCaptureActivity::class.java)
            .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
            .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
        startActivityForResult(intent, recordAudioResultCode)
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

    companion object{
        const val ITEM_KEY="ITEM_KEY"
    }
}