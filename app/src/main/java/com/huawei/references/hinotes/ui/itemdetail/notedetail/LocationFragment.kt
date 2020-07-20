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

class LocationFragment(item: Item) : BaseMapFragment(item) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.note_detail_location_bottom_sheet, container, false)

    override val mapType: MapType = MapType.ITEM_LOCATION


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    override fun onMapReady(huaweiMap: HuaweiMap?) {
        super.onMapReady(huaweiMap)
    }

    /*
    internal class CustomInfoWindowAdapter(private val name:String,private val address:String) : InfoWindowAdapter {
        private val mWindow: View

        override fun getInfoContents(marker: Marker): View? {
            return null
        }

        override fun getInfoWindow(marker: Marker): View {
            val txtvTitle = mWindow.findViewById<TextView>(R.id.infoTile)
            val txtvAddress = mWindow.findViewById<TextView>(R.id.infoAddress)
            val txtvdirection = mWindow.findViewById<TextView>(R.id.infoGetDirections)
            txtvTitle.text = name
            txtvAddress.text = address
            txtvdirection.setOnClickListener {

            }
            return mWindow
        }

        init {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null)
        }
    }
*/

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle =
            outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }
    */




    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle =
            outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }


    private fun setMarkersToMap(placeList : List<Site>){
        placeList.forEach {
            val latlng = LatLng(it.location.lat, it.location.lng)
            val info = InfoWindowData(it.siteId,
                it.name,
                it.formatAddress,
                it.location.lat,
                it.location.lng
            )
            val options = MarkerOptions()
            options.position(latlng)
            options.title(it.name)
            options.draggable(false)
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            options.clusterable(false)

            val customInfoWindow = context?.let { ctx -> CustomInfoWindowAdapter(ctx) }
            hMap!!.setInfoWindowAdapter(customInfoWindow)
            val marker = hMap!!.addMarker(options)
            marker.tag = info
        }
    }
*/



}
