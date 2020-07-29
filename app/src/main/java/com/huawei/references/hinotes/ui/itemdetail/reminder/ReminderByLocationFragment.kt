package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
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

        view.findViewById<TextView>(R.id.save_text)

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

    }

    override fun onLocationGet(location: Location) {
        super.onLocationGet(location)
        addCircle(location.latitude, location.longitude)
        userLocation=location
        selectedPoi.location = Coordinate(userLocation!!.latitude,userLocation!!.longitude)
        selectedPoi.name = "Your Location"
    }

    private fun addCircle(lat:Double, lng:Double){
        circle?.remove()
        circle=hMap?.addCircle(
            CircleOptions().center(LatLng(lat, lng)).radius(100.0).fillColor(Color.TRANSPARENT)
        )
    }

    override fun setOnPoiClickListener(site: Site, index: Int) {
        selectedPoi=site
        selectedPoi?.location?.lat?.let { addCircle(it, selectedPoi?.location?.lng!!) }
        super.setOnPoiClickListener(site, index)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.position?.latitude?.let { addCircle(it,p0.position.longitude) }
        return super.onMarkerClick(p0)
    }


}