package com.huawei.references.hinotes.ui.itemdetail

import android.Manifest
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.Reminder
import com.huawei.references.hinotes.data.item.model.ReminderType
import com.huawei.references.hinotes.ui.base.*
import com.huawei.references.hinotes.ui.itemdetail.notedetail.LocationFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.MapType
import com.huawei.references.hinotes.ui.itemdetail.reminder.ReminderFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_detail_todo_list.*
import kotlinx.android.synthetic.main.item_detail_toolbar.*
import kotlinx.android.synthetic.main.note_detail_location_bottom_sheet.*

abstract class ItemDetailBaseActivity : BaseActivity() {

    abstract fun getItemDetailViewModel() : ItemDetailViewModel

    protected var noteDetailChanged = false

    protected var isNewNote = true

    protected lateinit var itemData : Item

    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null

    override fun onStart() {
        super.onStart()
        observeDataHolderLiveData(getItemDetailViewModel().saveItemLiveData){
            noteDetailChanged = false
            isNewNote=false
            itemData.itemId=it.itemId
//            it.subItemIds?.let {serverSubItemList->
//                val newIds=serverSubItemList.filter {
//                        serverSubItemId->
//                    itemData.todoListSubItems?.find { it.id==serverSubItemId }==null
//                }
//                newIds.forEach {
//                    itemData.todoListSubItems?.forEach {subItem->
//                        if(subItem.id==-1) subItem.id=it
//                    }
//                }
//            }
//            it.reminderId?.let {
//                 itemData.reminder?.id=it
//            }
            onSaveSuccessful()
            customToast(this,this.getString(R.string.note_successfully_saved),false)
        }

        observeDataHolderLiveData(getItemDetailViewModel().deleteItemLiveData){
            customToast(this,this.getString(R.string.note_successfully_deleted),false)
            finish()
        }

        if(itemData.itemId!=-1)
            getItemDetailViewModel().getReminders(itemData.itemId)

        observeDataHolderLiveData(getItemDetailViewModel().reminderLiveData,{
            onGetRemindersCompleted()
            //TODO: fill no reminder ui
        }){
            itemData.reminder=it.first()
            onGetRemindersCompleted()
            //TODO: fill reminder ui
        }

    }

    override fun setupUI() {
        super.setupUI()
        add_reminder?.setOnClickListener {
            performAddReminder()
        }

        location_icon?.setOnClickListener {
            performAddLocation()
        }

        bottomSheetBehavior = BottomSheetBehavior.
        from(note_detail_location_bottom_sheet_layout as View).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun performAddLocation(){
        runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET){
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                val bottomSheetFragment = LocationFragment(itemData)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            else {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    protected open fun onGetRemindersCompleted(){}

    private fun performAddReminder() =
        runWithPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET){
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                val bottomSheetFragment = ReminderFragment(itemData)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            else {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
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

    open fun onSaveSuccessful(){}

    protected fun runWithAGConnectUserOrOpenLogin(runBlock: (agConnectUser: AGConnectUser) -> Unit){
        agConnectAuth?.currentUser?.let {
            runBlock.invoke(it)
        } ?: kotlin.run {
            openLoginActivity()
        }
    }

    fun locationSelected(lat:Double,lng:Double,mapType: MapType,radius: Double){
        when(mapType){
            MapType.ITEM_LOCATION ->{
                itemData.lat=lat
                itemData.lng=lng
                //TODO: set location ui
            }
            MapType.GEOFENCE->{
                val reminder=itemData.reminder ?: Reminder(-1,reminderType = ReminderType.ByGeofence).apply {
                    itemData.reminder=this
                }
                reminder.apply {
                    location= LatLng(lat,lng)
                    this.radius=radius
                }
                //TODO: set location reminder ui
            }
        }
    }

    fun poiSelected(site: Site, mapType: MapType,radius: Double){
        when(mapType){
            MapType.ITEM_LOCATION ->{
                itemData.lat=site.location.lat
                itemData.lng=site.location.lng
                //TODO: set poi location ui
            }
            MapType.GEOFENCE->{
                val reminder=itemData.reminder ?: Reminder(-1,reminderType = ReminderType.ByGeofence).apply {
                    itemData.reminder=this
                }
                reminder.apply {
                    location= LatLng(site.location.lat,site.location.lng)
                    reminderType=ReminderType.ByGeofence
                    title=site.name
                    this.radius=radius
                }
                //TODO: set location reminder ui
            }
        }
    }

    protected fun <T : Any>observeDataHolderLiveData(liveData: LiveData<DataHolder<T>>,
                                                     noResultBlock: () -> Unit= {},
                                                     runBlock: (data:T) -> Unit){
        liveData.observe(this, Observer {
            when(it){
                is DataHolder.Success->{
                    detail_progress_bar.hide()
                    runBlock.invoke(it.data)
                }
                is DataHolder.Fail->{
                    detail_progress_bar.hide()
                    if(it.baseError is NoRecordFoundError){
                        noResultBlock.invoke()
                    }
                    else {
                        customToast(this,it.errStr,true)
                    }
                }
                is DataHolder.Loading->{
                    detail_progress_bar.show()
                }
            }
        })
    }
}