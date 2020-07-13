package com.huawei.references.hinotes.ui.itemdetail.notedetail

import android.Manifest.permission
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.location.SiteAdapter
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class LocationBottomSheetFragment(lat:Double, lon:Double) : BottomSheetDialogFragment(), OnMapReadyCallback {
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
        var encodedApiKey: String? = null
        try {
            encodedApiKey = URLEncoder.encode(API_KEY, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        searchService = SearchServiceFactory.create(activity, encodedApiKey)

        val mapView = locationView.findViewById <MapView> (R.id.note_detail_location_mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        val siteList = locationView.findViewById <RecyclerView> (R.id.site_list)
        reSite = siteList
        getFirstPlace()
        return locationView
    }


    @RequiresPermission(allOf = [permission.ACCESS_FINE_LOCATION, permission.ACCESS_WIFI_STATE])
    override fun onMapReady(huaweiMap: HuaweiMap) {
        Log.d("onMapReady", huaweiMap.toString())
        hMap = huaweiMap
        hMap!!.isMyLocationEnabled = true // Enable the my-location overlay.
        hMap!!.uiSettings.isCompassEnabled = true
        hMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lLat, lLon), 15F))
        hMap!!.uiSettings.isMyLocationButtonEnabled = true // Enable the my-location icon.
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


    private fun addMarker(lLat: Double, lLon: Double) {
        //if (mMarker != null) mMarker!!.remove()
        val options = MarkerOptions().position(LatLng(lLat, lLon))
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        mMarker = hMap!!.addMarker(options)
    }

    private fun getFirstPlace() {
        request = NearbySearchRequest()
        location = Coordinate(lLat, lLon)
        request!!.location = location
        request!!.radius = 6000
        request!!.poiType = LocationType.GROCERY_OR_SUPERMARKET
        request!!.language = "en"
        request!!.pageIndex = 1
        request!!.pageSize = 10
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(results: NearbySearchResponse) {
                    listSite = results.sites
                    reSite!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                    reSite!!.adapter = SiteAdapter(listSite!!)
                    if (results.totalCount <= 0 || listSite.isNullOrEmpty()) {
                        return
                    }
                    for (site in listSite!!) {
                        addMarker(site.location.lat,site.location.lng)
                        //hMap!!.setInfoWindowAdapter(CustomInfoWindowAdapter(site.name,site.formatAddress.toString()))
                        Log.i("siteid",
                            String.format(
                                "siteId: '%s', name: %s\r\n distance:%f",
                                site.siteId,
                                site.name,
                                site.distance
                            )
                        )
                    }
                }

                override fun onSearchError(status: SearchStatus) {
                    Log.i(
                        "siteid",
                        "Error : " + status.errorCode + " " + status.errorMessage
                    )
                }
            }
        searchService!!.nearbySearch(request, resultListener)
    }

    companion object{
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        const val API_KEY="CV8PceXOYopn/ngZDofcwgFkmqYCo+LbAqjSx+uqBmVckaFf0bNgunMln8bncm+K6LjavJR/r/8N1PVf8ZEn1aVLNDZf"
    }

}
