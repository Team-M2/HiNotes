package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.Circle
import com.huawei.hms.maps.model.CircleOptions
import com.huawei.hms.maps.model.LatLng
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.BaseMapFragment


class ReminderFragment(var item: Item) : BaseMapFragment(item) {

    var circle: Circle?=null
    override val mapType: MapType = MapType.GEOFENCE


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progress = view.findViewById(R.id.seekBar) as SeekBar
        progress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                currentRadius= 100+progress.toDouble()*2
                circle?.radius =currentRadius
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
         inflater.inflate(R.layout.reminder_fragment, container, false)


    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
        hMap=huaweiMap
    }

    override fun onLocationGet(location: Location) {
        super.onLocationGet(location)
        addCircle(location.latitude, location.longitude)
    }

    private fun addCircle(lat:Double, lng:Double){
        circle?.remove()
        circle=hMap?.addCircle(
            CircleOptions().center(LatLng(lat, lng)).radius(100.0).fillColor(Color.TRANSPARENT)
        )
    }

}