package com.huawei.references.hinotes.ui.base

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
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
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailViewModel
import com.huawei.references.hinotes.ui.itemdetail.notedetail.LocationFragment
import com.huawei.references.hinotes.ui.itemdetail.reminder.MapType
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.IPoiClickListener
import com.huawei.references.hinotes.ui.itemdetail.reminder.adapter.PoiItemsAdapter
import kotlinx.android.synthetic.main.custom_info_window.view.*
import kotlinx.android.synthetic.main.reminder_by_location_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


abstract class BaseMapFragment(private var item: Item): BottomSheetDialogFragment(), OnMapReadyCallback,
    IPoiClickListener, HuaweiMap.OnMarkerClickListener {
    protected var hMap:HuaweiMap?=null
    var mapViewBundle: Bundle? = null
    var fusedLocationProviderClient : FusedLocationProviderClient ?= null
    var settingsClient:SettingsClient ?= null
    private var encodedApiKey:String?=null
    private var searchService: SearchService? = null
    private var markerList : ArrayList<Marker> = arrayListOf()
    protected var currentRadius=100.0
    var selectedPoi:Site?=null

    abstract val mapType:MapType

    protected val parentViewModel : ItemDetailViewModel by lazy {
        requireActivity().getViewModel<ItemDetailViewModel>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            encodedApiKey = URLEncoder.encode(API_KEY, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        searchService = SearchServiceFactory.create(context, encodedApiKey)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity)
        settingsClient = LocationServices.getSettingsClient(this.activity)

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        val mapView=view.findViewById<MapView>(R.id.detailBottomSheetMapView)
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
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


            // crashed uncaughtException stacktrace is java.lang.IllegalStateException:
            // Fragment LocationFragment{32e271} (b53af7fb-8182-43e7-b823-7816b764545e)} not attached to an activity.

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
    }

    protected open fun onLocationGet(location: Location){}

    private fun getLocation(settingsClient: SettingsClient?, fusedLocationProviderClient: FusedLocationProviderClient?, hMap: HuaweiMap?) {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        val mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 16.5F))
                    onLocationGet(locationResult.lastLocation)
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
        request.radius = 100
        request.language = "en"
        request.pageIndex = 1
        request.pageSize = 10
        val resultListener: SearchResultListener<NearbySearchResponse> =
            object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(results: NearbySearchResponse) {
                    addMarker(results.sites)
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

    override fun setOnPoiClickListener(site: Site, index: Int) {
        markerList.getOrNull(index)?.showInfoWindow()
        (activity as? ItemDetailBaseActivity)?.let {
            it.poiSelected(site,mapType,currentRadius)
        }
        cameraUpdate(site.location.lat,site.location.lng)
        selectedPoi = site
    }

    internal class CustomInfoWindowAdapter(private val itemDetailBaseActivity: ItemDetailBaseActivity,
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
            //val directionText = mWindow.findViewById<TextView>(R.id.infoGetDirections)
            //directionText.setOnClickListener {
            //    marker?.position?.let {
            //        itemDetailBaseActivity.locationSelected(it.latitude,it.longitude,mapType,baseMapFragment.currentRadius)}
            //}
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
        selectedPoi?.name=p0?.title
        selectedPoi?.location?.lat = p0?.position?.latitude!!
        selectedPoi?.location?.lng = p0.position?.longitude!!
        cameraUpdate(p0.position?.latitude!!,p0.position?.longitude!!)
        return true
    }

    companion object{
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        const val API_KEY="CV8PceXOYopn/ngZDofcwgFkmqYCo+LbAqjSx+uqBmVckaFf0bNgunMln8bncm+K6LjavJR/r/8N1PVf8ZEn1aVLNDZf"
    }


}