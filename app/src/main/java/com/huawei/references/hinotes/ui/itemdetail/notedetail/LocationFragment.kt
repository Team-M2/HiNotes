package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import com.huawei.hms.maps.HuaweiMap
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.BaseMapFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.MapType
import kotlinx.android.synthetic.main.note_detail_location_bottom_sheet.view.*

class LocationFragment(var item: Item) : BaseMapFragment(item) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.note_detail_location_bottom_sheet, container, false)

    override val mapType: MapType = MapType.ITEM_LOCATION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.save_text.setOnClickListener {
            item.lat = selectedPoi?.location?.lat
            item.lng = selectedPoi?.location?.lng
            item.poiDescription = selectedPoi?.name
        }
        view.delete_text.setOnClickListener {
            if(item.lat == null && item.lng == null){
                //todo:delete location
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
    }

}
