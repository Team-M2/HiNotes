package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.model.*
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.location.InfoWindowData
import com.huawei.references.hinotes.ui.base.BaseMapFragment
import kotlinx.android.synthetic.main.custom_info_window.view.*

class LocationBottomSheetFragment(lat:Double, lon:Double) : BaseMapFragment() {
    private var hMap: HuaweiMap? = null
    private var mMarker: Marker? = null
    private var lLat:Double = lat
    private var lLon:Double = lon
    private var searchService: SearchService? = null
    private var request: NearbySearchRequest? = null
    private var location: Coordinate? = null
    private var listSite: MutableList<Site>? = null
    private var reSite: RecyclerView? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val locationView =
            inflater.inflate(R.layout.note_detail_location_bottom_sheet, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity)
        settingsClient = LocationServices.getSettingsClient(this.activity)

        val mapView = locationView.findViewById <MapView> (R.id.note_detail_location_mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        return locationView
    }


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

    internal class CustomInfoWindowAdapter(mcontext: Context) :
        HuaweiMap.InfoWindowAdapter {
        private val mWindow: View = (mcontext as Activity).
        layoutInflater.inflate(R.layout.custom_info_window, null)

        override fun getInfoWindow(marker: Marker?): View {
            val mInfoWindow: InfoWindowData? = marker?.tag as InfoWindowData?

            mWindow.infoTile.text = mInfoWindow?.siteName
            mWindow.infoAddress.text = mInfoWindow?.siteAddress

            val txtvdirection = mWindow.findViewById<TextView>(R.id.infoGetDirections)
            txtvdirection.setOnClickListener {

            }

            return mWindow
        }

        override fun getInfoContents(marker: Marker): View? {
            return null
        }

    }


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
    companion object{
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        const val API_KEY="CV8PceXOYopn/ngZDofcwgFkmqYCo+LbAqjSx+uqBmVckaFf0bNgunMln8bncm+K6LjavJR/r/8N1PVf8ZEn1aVLNDZf"
    }

}
