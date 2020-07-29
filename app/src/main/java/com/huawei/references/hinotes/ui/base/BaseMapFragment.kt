package com.huawei.references.hinotes.ui.base

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.location.InfoWindowData
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.huawei.references.hinotes.ui.itemdetail.reminder.MapType
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.IPoiClickListener
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.PoiItemsAdapter
import kotlinx.android.synthetic.main.custom_info_window.view.*
import kotlinx.android.synthetic.main.reminder_by_location_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


abstract class BaseMapFragment(private var item: Item): ItemDetailBottomSheetFragment(), OnMapReadyCallback,
    IPoiClickListener, HuaweiMap.OnMarkerClickListener {
    protected var hMap:HuaweiMap?=null
    var mapViewBundle: Bundle? = null
    var fusedLocationProviderClient : FusedLocationProviderClient ?= null
    var settingsClient:SettingsClient ?= null
    private var encodedApiKey:String?=null
    private var searchService: SearchService? = null
    private var markerList : ArrayList<Marker> = arrayListOf()
    protected var currentRadius=100.0
    var selectedPoi:Site=Site()

    abstract val mapType:MapType

    private var isFragmentActive=false


    protected val parentViewModel : ItemDetailViewModel by lazy {
        requireActivity().getViewModel<ItemDetailViewModel>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentActive=true
        try {
            encodedApiKey = URLEncoder.encode(API_KEY, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        searchService = SearchServiceFactory.create(context, encodedApiKey)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity)
        settingsClient = LocationServices.getSettingsClient(this.activity)

        savedInstanceState?.let { mapViewBundle=it }

        view.findViewById<MapView>(R.id.detailBottomSheetMapView)?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@BaseMapFragment)
        }

        view.findViewById<TextView>(R.id.save_text).setOnClickListener {
            (activity as? ItemDetailBaseActivity)?.poiSelected(selectedPoi,mapType,currentRadius)
            dismiss()
        }

    }

    fun handleSearchResult(results: NearbySearchResponse){
        if(isFragmentActive){
            if(results.sites != null) {
                addMarker(results.sites)
                poi_recycler_view?.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter =
                        PoiItemsAdapter(
                            results.sites,
                            this@BaseMapFragment
                        )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isFragmentActive=false
    }

    override fun onMapReady(huaweiMap: HuaweiMap?) {
        hMap=huaweiMap?.apply {
            isMyLocationEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            getLocation(settingsClient,fusedLocationProviderClient,this)
        }
        hMap?.setOnMarkerClickListener(this@BaseMapFragment)
    }

    private fun addMarker(placeList : List<Site>){
        markerList.clear()
        placeList.forEach {
            val latlng = LatLng(it.location.lat, it.location.lng)
            val info = InfoWindowData(it.siteId,
                it.name,
                it.formatAddress,
                it.location.lat,
                it.location.lng
            )
            val options = MarkerOptions().apply {
                position(latlng)
                title(it.name)
                draggable(false)
                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                clusterable(false)
            }

            try {
                (requireActivity() as? ItemDetailBaseActivity)?.let {
                    val customInfoWindow = CustomInfoWindowAdapter(
                        it,this,mapType
                    )
                    hMap?.apply {
                        setInfoWindowAdapter(customInfoWindow)
                        markerList.add(addMarker(options).apply {
                            tag=info
                        })
                    }
                }
            }
            catch (e:Exception){

            }
        }
    }

    protected open fun onLocationGet(location: Location){}

    private fun getLocation(settingsClient: SettingsClient?, fusedLocationProviderClient: FusedLocationProviderClient?, hMap: HuaweiMap?) {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        val mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.apply {
                    hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude), 16.5F))
                    onLocationGet(lastLocation)
                    getPoiList(lastLocation.latitude, lastLocation.longitude)
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
                    handleSearchResult(results)
                }

                override fun onSearchError(status: SearchStatus) {
                }
            }
        searchService?.nearbySearch(request, resultListener)
    }

    override fun setOnPoiClickListener(site: Site, index: Int) {
        markerList.getOrNull(index)?.showInfoWindow()
        cameraUpdate(site.location.lat,site.location.lng)
        selectedPoi=site
    }

    internal class CustomInfoWindowAdapter(itemDetailBaseActivity: ItemDetailBaseActivity,
                                           private val baseMapFragment: BaseMapFragment,
                                           private val mapType: MapType
    ) :
        HuaweiMap.InfoWindowAdapter {
        private val mWindow: View = itemDetailBaseActivity.
        layoutInflater.inflate(R.layout.custom_info_window, null)

        override fun getInfoWindow(marker: Marker?): View {
            val mInfoWindow: InfoWindowData? = marker?.tag as InfoWindowData?
            mWindow.infoTile.text = mInfoWindow?.siteName
            mWindow.infoAddress.text = mInfoWindow?.siteAddress
            return mWindow
        }

        override fun getInfoContents(marker: Marker): View? {
            return null
        }
    }

    private fun cameraUpdate(lat:Double,lng:Double){
        val cameraBuild = CameraPosition.Builder().target(
            LatLng(lat, lng)
        ).zoom(17f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraBuild)
        hMap?.animateCamera(cameraUpdate)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.let {
            selectedPoi.apply {
                name=it.title
                location.lat=it.position.latitude
                location.lng=it.position.longitude
            }
            cameraUpdate(it.position?.latitude!!,it.position?.longitude!!)
        }
        return true
    }

    companion object{
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        const val API_KEY="CV8PceXOYopn/ngZDofcwgFkmqYCo+LbAqjSx+uqBmVckaFf0bNgunMln8bncm+K6LjavJR/r/8N1PVf8ZEn1aVLNDZf"
    }


}