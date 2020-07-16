package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresPermission
import com.facebook.AccessToken
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.BaseMapFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.push.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReminderFragment(var item: Item) : BaseMapFragment(item) {


    var hMap:HuaweiMap?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity)
        settingsClient = LocationServices.getSettingsClient(this.activity)

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }

        val mapView=view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        val progress = view.findViewById(R.id.seekBar) as SeekBar
        progress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                circle?.radius = 100+progress.toDouble()*2
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reminder_fragment, container, false)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
        hMap=huaweiMap
    }

    override fun setOnPoiClickListener(site: Site, index: Int) {
        super.setOnPoiClickListener(site,index)

        //item.isPinned=true
        //(activity as DetailNoteActivity?)?.saveChanges(item)
    }


}