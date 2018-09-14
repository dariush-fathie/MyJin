package myjin.pro.ahoora.myjin.fragments

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.customClasses.Clusters
import myjin.pro.ahoora.myjin.customClasses.OwnIconRendered
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.events.NearestEvent
import myjin.pro.ahoora.myjin.models.events.NearestEvent2
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class NearestFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {


    private var mapType = 0
    lateinit var mMap: GoogleMap
    lateinit var realm: Realm

    private var g_url = ""
    private var g_name = ""


    private var groupId = 0


    lateinit var cv_level: CardView
    lateinit var fab_closeMap: FloatingActionButton

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    // send request to location service with @mLocationRequest
    private lateinit var mLocationRequest: LocationRequest
    // @mLocationRequest response
    private lateinit var mLocationCallback: LocationCallback

    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    private lateinit var currentPoint: LatLng

    private val updateInterval: Long = 16000
    private val fastestUpdateInterval: Long = updateInterval / 2
    private val requestPermission = 1054
    private val requestLocationSetting = 1055
    private var latitudeC: Double = 0.0
    private var longitudeC: Double = 0.0
    lateinit var idArray: ArrayList<Int>
    private var mClusterManager: ClusterManager<Clusters>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nearest, container, false)
        initViews(view)
        try {
            groupId = (activity as OfficeActivity).groupId
        } catch (e: Exception) {
            Log.e("ERR_GID", e.message + "")
        }
        idArray = (activity as OfficeActivity).idArray
        initMap()
        return view
    }

    private fun initViews(view: View) {

        cv_level = view.findViewById(R.id.cv_level)


        cv_level.setOnClickListener(this)
    }

    private fun initMap() {
        var nearestFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.nearestMap) as SupportMapFragment
        if (nearestFragment == null) {
            val fm = fragmentManager
            val ft: FragmentTransaction
            if (fm != null) {
                ft = fm.beginTransaction()
                nearestFragment = SupportMapFragment.newInstance()
                ft.replace(R.id.nearestMap, nearestFragment).commit()
            }
        }
        if (nearestFragment != null) {
            nearestFragment.getMapAsync(this)
        }

    }

    private fun selectModeLevel() {
        val popupMenu = PopupMenu((activity as OfficeActivity), cv_level, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu2, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.default_ -> {
                    mapType = 0
                    mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                R.id.satelite -> {
                    mapType = 1
                    mMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                R.id.terrain -> {
                    mapType = 2
                    mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
            }
            true
        }

        popupMenu.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        when (mapType) {
            0 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            1 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            2 -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }
        Handler().postDelayed({ EventBus.getDefault().post("loaded") }, 150)
        checkLocationPermissions()

        setUpClusterer()
    }


    private fun setUpClusterer() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.311339, 46.995957), 16f))
        mClusterManager = ClusterManager((activity as OfficeActivity), mMap)
        mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
        addMarkers()
    }


    private fun addMarkers() {

        realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val item = db.where(KotlinItemModel::class.java).`in`("centerId", idArray.toTypedArray()).findAll()
            item.forEach { mItem ->

                val offsetItem = Clusters((activity as OfficeActivity), mItem.addressList!![0]?.latitude?.toDouble()!!, mItem.addressList!![0]?.longitude?.toDouble()!!,
                        mItem.firstName + " " + mItem.lastName, "قطعه")
                mClusterManager!!.addItem(offsetItem)
                mClusterManager!!.setRenderer(OwnIconRendered((activity as OfficeActivity), mMap, mClusterManager))

            }
        }

    }

    // return true if app has location permission
    private fun hasLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.checkSelfPermission((activity as OfficeActivity), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationApi() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient((activity as OfficeActivity))
        mSettingsClient = LocationServices.getSettingsClient((activity as OfficeActivity))
        // get last saved location
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->

            try {
                currentPoint = LatLng(location.latitude, location.longitude)
            } catch (e: Exception) {
                Log.e("loc", "error")
            }
        }
    }

    private fun stopLocationUpdate() {
        if (hasLocationPermission()) {
            if (this::mFusedLocationClient.isInitialized && this::mLocationCallback.isInitialized) {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            }
        }
    }




    private fun createLocationRequest() {
        initLocationApi()
        mLocationRequest = LocationRequest.create()
        // update user location each 10000ms = 10s
        mLocationRequest.interval = updateInterval
        mLocationRequest.fastestInterval = fastestUpdateInterval
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        checkLocationSetting()
    }

    var first = true
    private fun createLocationCallback() {
        Log.e("loc", "1")
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                Log.e("loc", "2")
                locationResult ?: return
                val x = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                currentPoint = x

                if (first) {
                    first = false
                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(x, 16f))
                }
                Log.e("Location Update", "location updated!")
            }
        }

        startLocationUpdate()
        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        Log.e("loc", "3")
        if (hasLocationPermission()) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    @Subscribe
    fun chLoSet(e: NearestEvent) {
        checkLocationSetting()
    }

    @Subscribe
    fun crLoRes(e: NearestEvent2) {
        createLocationRequest()

    }


    private fun checkLocationSetting() {
        mLocationSettingsRequest = LocationSettingsRequest.Builder().setAlwaysShow(true).addLocationRequest(mLocationRequest).build()
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    createLocationCallback()
                }.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            exception.startResolutionForResult((activity as OfficeActivity),
                                    requestLocationSetting)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                    Log.e("Location", "failure ${exception.message}")
                    //locationSettingDialog()
                    // location setting not enabled
                }.addOnCanceledListener {
                    Log.e("Location", "onCanecl")
                }

    }

    private fun checkLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasLocationPermission()) {
                createLocationRequest()
            } else {
                ActivityCompat.requestPermissions((activity as OfficeActivity)
                        , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        , requestPermission)
            }
        } else {
            createLocationRequest()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cv_level -> selectModeLevel()

        }
    }




    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdate()
        EventBus.getDefault().unregister(this)
    }
}
