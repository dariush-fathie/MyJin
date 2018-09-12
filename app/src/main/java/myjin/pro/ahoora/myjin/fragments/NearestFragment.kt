package myjin.pro.ahoora.myjin.fragments

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.events.NearestEvent
import myjin.pro.ahoora.myjin.models.events.NearestEvent2
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class NearestFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {


    private var mapType = 0
    var mMap: GoogleMap? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.nearest_map, container, false)
        initViews(view)
        try {
            groupId = (activity as OfficeActivity).groupId
        } catch (e: Exception) {
            Log.e("ERR_GID", e.message + "")
        }
        initMap()
        return view
    }

    private fun initViews(view: View) {

        cv_level = view.findViewById(R.id.cv_level)
        fab_closeMap = view.findViewById(R.id.fab_closeMap)

        cv_level.setOnClickListener(this)
        fab_closeMap.setOnClickListener(this)
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

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        when (mapType) {
            0 -> {
                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            1 -> {
                googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            2 -> {
                googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }

        checkLocationPermissions()

        val p = LatLng(35.27029,46.999)
        mMap?.addMarker(MarkerOptions().title("sanandaj").position(p))?.showInfoWindow()
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(p, 16f))
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
            //mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17f))
            try {
                currentPoint = LatLng(location.latitude, location.longitude)
            } catch (e: Exception) {
                Log.e("loc", "error")
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

    private fun createLocationCallback() {
        Log.e("loc", "1")
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                Log.e("loc", "2")
                locationResult ?: return
                val x = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                currentPoint = x
                //mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(x, 18f))
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
    fun chLoSet(e:NearestEvent){
        checkLocationSetting()
    }
    @Subscribe
    fun crLoRes(e: NearestEvent2){
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
                // access location
                Toast.makeText((activity as OfficeActivity), "location permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions((activity as OfficeActivity)
                        , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        , requestPermission)
            }
        } else {
            createLocationRequest()
        }
    }

  /*  private fun dataBaseSearch(searchedText: String) {
        var addres = ""

        realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val result = realm.where(KotlinItemModel::class.java).equalTo("groupId", groupId).findAll()
        g_name = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.name!!
        g_url = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.g_url!!

        realm.commitTransaction()

    }*/



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
        EventBus.getDefault().unregister(this)
    }
}
