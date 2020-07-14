package com.huawei.references.hinotes.ui.base

import android.os.Bundle
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.location.InfoWindowData
import com.huawei.references.hinotes.ui.itemdetail.notedetail.LocationBottomSheetFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.IPoiClickListener
import com.huawei.references.hinotes.ui.itemdetail.reminder.PoiItemsAdapter
import kotlinx.android.synthetic.main.reminder_fragment.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

open class BaseMapFragment: BottomSheetDialogFragment(), OnMapReadyCallback, IPoiClickListener {
    private var hMap:HuaweiMap?=null
    var mapViewBundle: Bundle? = null
    var fusedLocationProviderClient : FusedLocationProviderClient ?= null
    var settingsClient:SettingsClient ?= null
    private var encodedApiKey:String?=null
    private var searchService: SearchService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            encodedApiKey = URLEncoder.encode(LocationBottomSheetFragment.API_KEY, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        searchService = SearchServiceFactory.create(context, encodedApiKey)
    }


    override fun onMapReady(huaweiMap: HuaweiMap?) {
        hMap=huaweiMap
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isCompassEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
        getLocation(settingsClient,fusedLocationProviderClient,hMap)
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

            val customInfoWindow = context?.let { ctx ->
                LocationBottomSheetFragment.CustomInfoWindowAdapter(
                    ctx
                )
            }
            hMap!!.setInfoWindowAdapter(customInfoWindow)
            val marker = hMap!!.addMarker(options)
            marker.tag = info
        }
    }

    /*
    fun addMarker(lat:Double,lng:Double,hMap: HuaweiMap){
        val options = MarkerOptions().position(LatLng(lat, lng))
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        hMap.addMarker(options)
    }
*/
    private fun getLocation(settingsClient: SettingsClient?, fusedLocationProviderClient: FusedLocationProviderClient?, hMap: HuaweiMap?) {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        val mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 15F))
                    getPoiList(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                }
            }
        }

        settingsClient?.checkLocationSettings(locationSettingsRequest)
            ?.addOnSuccessListener {
                fusedLocationProviderClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
                )
                    .addOnSuccessListener { }
            }
            ?.addOnFailureListener { e -> }
    }

    fun getPoiList(lat:Double, lng:Double) {
        val request = NearbySearchRequest()
        val location = Coordinate(lat, lng)
        request.location = location
        request.radius = 1000
        request.language = "en"
        request.pageIndex = 1
        request.pageSize = 10
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(results: NearbySearchResponse) {
                    setMarkersToMap(results.sites)
                    poi_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    poi_recycler_view.adapter = PoiItemsAdapter(results.sites,this@BaseMapFragment)
                }

                override fun onSearchError(status: SearchStatus) {
                }
            }
        searchService!!.nearbySearch(request, resultListener)
    }

    protected open fun setupUI() = Unit

    override fun setOnPoiClickListener(site: Site) {

    }

}