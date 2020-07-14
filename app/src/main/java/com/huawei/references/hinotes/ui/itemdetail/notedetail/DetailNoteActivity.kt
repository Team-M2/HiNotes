package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.base.customPopup
import com.huawei.references.hinotes.ui.base.customToast
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.huawei.references.hinotes.ui.itemdetail.reminder.IPoiClickListener
import com.huawei.references.hinotes.ui.itemdetail.reminder.ReminderFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.choose_image_direction.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import kotlinx.android.synthetic.main.note_detail_location_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class DetailNoteActivity : ItemDetailBaseActivity() {

    private val viewModel: DetailNoteViewModel by viewModel()
    private var isNewNote = true
    private lateinit var noteItemData :Item
    private val takePictureResultCode = 201
    private val pickImageResultCode = 202
    private var noteDetailChanged = false
    private var mLocationRequest: LocationRequest? = null
    private var settingsClient: SettingsClient? = null
    private var mLocationCallback: LocationCallback? = null
    var lLat: Double? = null
    var lLon: Double? = null
    var mName: String? = null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun getItemDetailViewModel(): ItemDetailViewModel =viewModel
    private val recordAudioResultCode = 203

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsClient = LocationServices.getSettingsClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
        if(!contentViewIsSet){
            contentViewIsSet=true
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                setCustomView(R.layout.item_detail_toolbar)
            }
            setContentView(R.layout.activity_detail_note)
        }
        bottomSheetBehavior = BottomSheetBehavior.from(note_detail_location_bottom_sheet_layout)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        note_detail_title.setText(noteItemData.title)
        note_detail_description.setText(noteItemData.poiDescription.toString())

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_icon.setOnClickListener {
            if(!isNewNote){
                runWithAGConnectUserOrOpenLogin {
                    customPopup(this.getString(R.string.delete_note_popup_warning),
                        this.getString(R.string.delete_note_popup_accept),
                        this.getString(R.string.delete_note_popup_reject),
                        { viewModel.deleteItem(noteItemData,it.uid) },
                        this)
                }
            }
        }

        location_icon.setOnClickListener {
            checkLocationPermission()
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

        add_reminder.setOnClickListener {
            performAddReminder()
        }

        saveFab.setOnClickListener {
            if(note_detail_title.text.toString() == "" || note_detail_description.text.toString() == ""){
                customToast(this,this.getString(R.string.note_detail_check_title_description),true)
            }
            else {
                runWithAGConnectUserOrOpenLogin {
                    val itemToSave = noteItemData.apply {
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
    }

    fun saveChanges(item:Item){
        runWithAGConnectUserOrOpenLogin {
            viewModel.saveItem(item, it.uid, isNewNote)
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            } else
                getLocation()
        } else {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                ActivityCompat.requestPermissions(this, strings, 2)
            } else
                getLocation()
        }
    }

    private fun getLocation() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    lLat = locationResult.lastLocation.latitude
                    lLon = locationResult.lastLocation.longitude
                    if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                        val bottomSheetFragment = LocationBottomSheetFragment(lLat!!, lLon!!)
                        bottomSheetFragment.show(this@DetailNoteActivity.supportFragmentManager, bottomSheetFragment.tag)
                    } else {
                        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
        }

        settingsClient!!.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                fusedLocationProviderClient!!.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.getMainLooper()
                    )
                    .addOnSuccessListener { }
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        Log.i("bla", e.toString())
                    } catch (sie: SendIntentException) {
                        Log.i("bla", sie.toString())
                    }
                }
            }
    }

    override fun onBackPressed() {
        if(noteDetailChanged) {
            runWithAGConnectUserOrOpenLogin {
                customPopup(this.getString(R.string.delete_item_changes_popup_warning),
                    this.getString(R.string.delete_item_changes_popup_accept),
                    this.getString(R.string.delete_item_changes_popup_reject),
                    {finish()},
                    this
                )
            }
        }
        else{
            super.onBackPressed()
        }
    }

    private fun createNote()=Item(11, Date(),Date(),ItemType.Note,false,0.0,0.0,"","","",
        arrayListOf(),false,UserRole.Owner,false)

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
            include_choose_image.visibility= View.GONE
            note_detail_description.text = note_detail_description.text.append(resultText)
        }.addOnFailureListener {
            Toast.makeText(this,this.getString(R.string.text_recognition_failed),Toast.LENGTH_SHORT).show()
            include_choose_image.visibility= View.GONE
        }
    }

    private fun performSpeechToText() = runWithPermissions(Manifest.permission.RECORD_AUDIO){
        val intent = Intent(this, MLAsrCaptureActivity::class.java)
            .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
            .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
        startActivityForResult(intent, recordAudioResultCode)
    }

    private fun performAddReminder() =
        runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET){
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                val bottomSheetFragment = ReminderFragment(noteItemData)
                bottomSheetFragment.show(this@DetailNoteActivity.supportFragmentManager, bottomSheetFragment.tag)
            }
            else {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
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

    companion object{
        const val ITEM_KEY="ITEM_KEY"
    }
}