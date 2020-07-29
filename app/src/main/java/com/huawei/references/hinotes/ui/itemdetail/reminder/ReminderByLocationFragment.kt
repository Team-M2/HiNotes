package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.huawei.hms.location.Geofence
import com.huawei.hms.location.GeofenceRequest
import com.huawei.hms.location.GeofenceService
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.Circle
import com.huawei.hms.maps.model.CircleOptions
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.BaseMapFragment
import com.huawei.references.hinotes.ui.base.ItemDetailBottomSheetType
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import kotlinx.android.synthetic.main.reminder_by_location_fragment.view.*


class ReminderByLocationFragment(var item: Item) : BaseMapFragment(item) {
    var circle: Circle?=null
    var userLocation:Location?=null
    override val mapType: MapType = MapType.GEOFENCE

    override val itemDetailBottomSheetType: ItemDetailBottomSheetType =
        ItemDetailBottomSheetType.REMINDER

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progress = view.findViewById(R.id.seekBar) as SeekBar
        progress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                currentRadius = 100+progress.toDouble()*2
                circle?.radius =currentRadius
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        view.save_text.setOnClickListener {
            selectedPoi.location?.lat?.let { it1 -> getLocationInBackground(it1,
                selectedPoi.location?.lng!!, currentRadius.toFloat()
            ) }
            (activity as? ItemDetailBaseActivity)?.poiSelected(selectedPoi!!,MapType.GEOFENCE,currentRadius)
            this.dismiss()
        }

        view.delete_text.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
         inflater.inflate(R.layout.reminder_by_location_fragment, container, false)


    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
        hMap=huaweiMap
        /*
        if(item.reminder != null && item.reminder?.itemId != -1){
            addSavedMarker(item.reminder?.location?.latitude!!,item.reminder?.location?.longitude!!)
            addCircle(item.reminder?.location?.latitude!!,item.reminder?.location?.longitude!!,
                item.reminder?.radius!!
            )
            cameraUpdate(item.reminder?.location?.latitude!!,item.reminder?.location?.longitude!!)
        }
        
         */
    }

    override fun onLocationGet(location: Location) {
        super.onLocationGet(location)
        addCircle(location.latitude, location.longitude,100.0)
        userLocation=location
        selectedPoi.location = Coordinate(userLocation!!.latitude,userLocation!!.longitude)
        selectedPoi.name = "Your Location"
        selectedPoi.address
    }

    private fun addCircle(lat:Double, lng:Double,radius: Double){
        circle?.remove()
        circle=hMap?.addCircle(
            CircleOptions().center(LatLng(lat, lng)).radius(radius).fillColor(Color.TRANSPARENT)
        )
    }

    private fun getLocationInBackground(lat:Double,lng:Double,radius:Float) {
        var geofenceService: GeofenceService?=null
        val idList: ArrayList<String?>?= arrayListOf()
        val geofenceList: ArrayList<Geofence?>?= arrayListOf()
        var TAG: String?=null
        var pendingIntent: PendingIntent?=null
        pendingIntent=getPendingIntent()
        geofenceService = LocationServices.getGeofenceService(this.activity)

        TAG = "geoFence"

        geofenceList?.add(
            Geofence.Builder()
                .setUniqueId("mGeofence")
                .setValidContinueTime(Geofence.GEOFENCE_NEVER_EXPIRE)
                .setNotificationInterval(10000)
                .setRoundArea(lat, lng, radius)
                .setConversions(Geofence.ENTER_GEOFENCE_CONVERSION or Geofence.EXIT_GEOFENCE_CONVERSION)
                .build()
        )
        idList?.add("mGeofence")

        getAddGeofenceRequest(geofenceList!!)
        geofenceService.createGeofenceList(getAddGeofenceRequest(geofenceList), pendingIntent)
            .addOnCompleteListener { task -> }
    }

    private fun getAddGeofenceRequest(geofenceList:ArrayList<Geofence?>): GeofenceRequest? {
        val builder = GeofenceRequest.Builder()
        builder.setInitConversions(GeofenceRequest.ENTER_INIT_CONVERSION)
        builder.createGeofenceList(geofenceList)
        return builder.build()
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this.activity, BroadcastReceiver::class.java)
        intent.putExtra("reminderType",0)
        intent.putExtra("reminderTitle",item.title)
        intent.action = BroadcastReceiver.ACTION_PROCESS_LOCATION
        return PendingIntent.getBroadcast(this.activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun setOnPoiClickListener(site: Site, index: Int) {
        selectedPoi=site
        selectedPoi.location?.lat?.let { addCircle(it, selectedPoi.location?.lng!!,100.0) }
        super.setOnPoiClickListener(site, index)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.position?.latitude?.let { addCircle(it,p0.position.longitude,100.0) }
        return super.onMarkerClick(p0)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}