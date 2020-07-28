package com.huawei.references.hinotes.ui.itemdetail

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.huawei.hms.maps.HuaweiMap
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.base.BaseMapFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.MapType
import kotlinx.android.synthetic.main.note_detail_location_bottom_sheet.view.*

class LocationFragment(var item: Item,var bottomSheetBehavior: BottomSheetBehavior<View>) : BaseMapFragment(item) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.note_detail_location_bottom_sheet, container, false)

    override val mapType: MapType = MapType.ITEM_LOCATION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("mcmc $item")
        view.save_text.setOnClickListener {
            (activity as? ItemDetailBaseActivity)?.poiSelected(selectedPoi!!,MapType.ITEM_LOCATION,0.0)
        }
        view.delete_text.setOnClickListener {

        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
    }

    override fun onDestroy() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        super.onDestroy()
    }
}
