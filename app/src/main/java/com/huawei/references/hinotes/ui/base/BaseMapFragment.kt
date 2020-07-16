package com.huawei.references.hinotes.ui.base

import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.location.InfoWindowData
import com.huawei.references.hinotes.ui.itemdetail.notedetail.LocationFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.IPoiClickListener
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.PoiItemsAdapter
import kotlinx.android.synthetic.main.reminder_fragment.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


open class BaseMapFragment(private var item: Item): BottomSheetDialogFragment(), OnMapReadyCallback,
    IPoiClickListener {
    private var hMap:HuaweiMap?=null
    var mapViewBundle: Bundle? = null
    var fusedLocationProviderClient : FusedLocationProviderClient ?= null
    var settingsClient:SettingsClient ?= null
    var circle:Circle?=null
    private var encodedApiKey:String?=null
    private var searchService: SearchService? = null
    var markerList : ArrayList<Marker>?= arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            encodedApiKey = URLEncoder.encode(LocationFragment.API_KEY, "utf-8")
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

            val marker = hMap!!.addMarker(options)
            markerList?.add(marker)
            marker.tag = info

            val customInfoWindow = context?.let { ctx ->
                LocationFragment.CustomInfoWindowAdapter(
                    ctx, item
                )
            }
            hMap!!.setInfoWindowAdapter(customInfoWindow)
        }
    }

    private fun getLocation(settingsClient: SettingsClient?, fusedLocationProviderClient: FusedLocationProviderClient?, hMap: HuaweiMap?) {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        val mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 17F))
                    getPoiList(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                    addCircle(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
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
        request.radius = 100
        request.language = "en"
        request.pageIndex = 1
        request.pageSize = 10
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(results: NearbySearchResponse) {
                    setMarkersToMap(results.sites)
                    poi_recycler_view?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    poi_recycler_view?.adapter =
                        PoiItemsAdapter(
                            results.sites,
                            this@BaseMapFragment
                        )
                }

                override fun onSearchError(status: SearchStatus) {
                }
            }
        searchService!!.nearbySearch(request, resultListener)
    }

    fun addCircle(lat:Double,lng:Double){
        circle?.remove()
        circle=hMap?.addCircle(
            CircleOptions().center(LatLng(lat, lng)).radius(100.0).fillColor(Color.TRANSPARENT)
        )
    }

    override fun setOnPoiClickListener(site: Site, index: Int) {
        markerList?.get(index)?.showInfoWindow()
        val cameraBuild = CameraPosition.Builder().target(
            LatLng(site.location.lat, site.location.lng)
        ).zoom(18f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraBuild)
        hMap?.animateCamera(cameraUpdate)
    }


}