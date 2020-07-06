package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.choose_image_direction.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class DetailNoteActivity : ItemDetailBaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()
    private var isNewNote=true
    private lateinit var noteItemData :Item
    private val cameraRequestCode = 101
    private val takePictureResultCode = 201
    private val storageRequestCode = 102
    private val pickImageResultCode = 202

    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        noteItemData=intent?.extras?.let{
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
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.item_detail_toolbar)
        }

        setContentView(R.layout.activity_detail_note)

        note_detail_title.setText(noteItemData.title)
        note_detail_description.setText(noteItemData.poiDescription.toString())

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if(!isNewNote){
                runWithAGConnectUserOrOpenLogin {
                    viewModel.deleteItem(noteItemData,it.uid)
                }
            }
        }

        microphone_icon.setOnClickListener {

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
            runWithAGConnectUserOrOpenLogin {
                val itemToSave=noteItemData.apply {
                    title=note_detail_title.text.toString()
                    poiDescription=note_detail_description.text.toString()
                }
                viewModel.saveItem(itemToSave,it.uid,isNewNote)
            }
        }
    }

    override fun onBackPressed() {
        //TODO: show usnsaved data will be lost popup,then finish
        super.onBackPressed()
    }

    private fun createNote()=Item(11, Date(),Date(),ItemType.Note,false,0.0,0.0,"","",
        arrayListOf(),false,UserRole.Owner,false)

    @SuppressLint("NewApi")
    private fun performTakePicture(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, takePictureResultCode)
        }
    }
    @SuppressLint("NewApi")
    private fun performPickImage(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storageRequestCode)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageResultCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, takePictureResultCode)
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == storageRequestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, pickImageResultCode)
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == takePictureResultCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.data == null) {
                    textRecFunctions(data?.extras!!["data"] as Bitmap)
                } else {
                    textRecFunctions(MediaStore.Images.Media.getBitmap( this.contentResolver,data.data))
                }
            }
        }
        if (requestCode == pickImageResultCode) {
            if (resultCode == Activity.RESULT_OK) {
                val pickedImage: Uri? = data?.data
                try {
                    textRecFunctions(MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun textRecFunctions(selectedImageBitmap:Bitmap){
        val setting = MLLocalTextSetting.Factory()
            .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE) // Specify languages that can be recognized.
            .setLanguage("en")
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)
        val frame = MLFrame.fromBitmap(selectedImageBitmap)

        val task: Task<MLText> = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            val resultText=it.stringValue.replace("\n"," ")
            include_choose_image.visibility= View.GONE
            performTextRecognitionResult(resultText)
        }.addOnFailureListener {
            include_choose_image.visibility= View.GONE
        }
    }

    private fun performTextRecognitionResult(result:String){
        note_detail_description.text = note_detail_description.text.append(result)
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