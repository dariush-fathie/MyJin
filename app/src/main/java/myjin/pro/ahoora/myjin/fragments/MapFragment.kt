package myjin.pro.ahoora.myjin.fragments;

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.adapters.HListAdapter
import myjin.pro.ahoora.myjin.customClasses.CustomBottomSheetBehavior
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.utils.Utils
import java.util.concurrent.TimeUnit

class MapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {
    override fun onClick(v: View?) {
        // directionRequest()
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            ivToggle.rotation = 180f
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            ivToggle.rotation = 0f
        }
    }

    lateinit var map: GoogleMap
    lateinit var mapList: RecyclerView
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var bottomSheetLayout: ConstraintLayout
    lateinit var ivToggle: AppCompatImageView
    lateinit var idArray: ArrayList<Int>
    private var snapedPosition: Int = 0
    private lateinit var mMarker: Marker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)
        idArray = (activity as OfficeActivity).idArray


        initViews(v)
        initMap()
        return v
    }

    private fun initViews(v: View) {
        ivToggle = v.findViewById(R.id.iv_toggle)
        ivToggle.setOnClickListener(this)
        mapList = v.findViewById(R.id.rv_mapList)
        bottomSheetLayout = v.findViewById(R.id.cl_mapBottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
    }

    private fun initList() {
        mapList.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)
        mapList.adapter = HListAdapter(activity as Context, idArray)
        mapList.addItemDecoration(CustomMapListDecoration())
        val snapHelper = LinearSnapHelper()
        mapList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val fvi = layoutManager.findFirstVisibleItemPosition()
                val lvi = layoutManager.findLastVisibleItemPosition()
                val fcvi = layoutManager.findFirstCompletelyVisibleItemPosition()
                val lcvi = layoutManager.findLastCompletelyVisibleItemPosition()

                if (fcvi == lcvi) {
                    snapedPosition = fcvi // or lcvi
                } else {

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) // if list stop moving
                    onListSnaped()
            }
        })
        snapHelper.attachToRecyclerView(mapList)
        addMarker(true)
    }

    private var currentSnapPosition: Int = -1


    private fun onListSnaped() {
        if (currentSnapPosition != snapedPosition) {
            if (snapedPosition == -1) {
                snapedPosition = 0
            }
            currentSnapPosition = snapedPosition
            addMarker(false)
            Log.e("listSnapedToPosition", "${currentSnapPosition}")
        }
    }

    private fun addMarker(firstPosition: Boolean) {

        var i = 0
        if (!firstPosition) {
            i = currentSnapPosition
        }
        val realm = Realm.getDefaultInstance()
        var latlng = LatLng(-1.0, -1.0)
        var name = ""




        realm.executeTransaction { db: Realm? ->
            val item = db?.where(KotlinItemModel::class.java)?.equalTo("centerId", idArray[i])?.findFirst()
            name = item?.firstName + " " + item?.lastName
            val lat = item?.addressList!![0]?.latitude
            val lng = item.addressList!![0]?.longitude
            latlng = LatLng(lat?.toDouble()!!, lng?.toDouble()!!)
            Log.e("LatLng", "${latlng}")
            Log.e("name", name)
        }
        if (this::mMarker.isInitialized) {
            mMarker.position = latlng
            mMarker.title = name
        } else {
            mMarker = map.addMarker(MarkerOptions().position(latlng).title(name))
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18f))
    }

    private fun initMap() {
        var mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if (mapFragment == null) {
            val fm = fragmentManager
            val ft: FragmentTransaction
            if (fm != null) {
                ft = fm.beginTransaction()
                mapFragment = SupportMapFragment.newInstance()
                ft.replace(R.id.map, mapFragment).commit()
            }
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            // TODO: 4/29/2018 show error dialog to user
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val sydney = LatLng(35.311339, 46.995957)
        /*googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19f))*/
        Handler().postDelayed({ EventBus.getDefault().post("loaded") }, 150)
        initList()
    }

    inner class CustomMapListDecoration : RecyclerView.ItemDecoration() {

        private val screenWidthDp = Utils.getScreenWidthDp(activity as Context)
        private val screenWidthPx = Utils.getScreenWidthPx(activity as Context)
        private var itemPadding = 0

        init {
            itemPadding = Utils.pxFromDp(activity as Context, 16f).toInt()
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition
            val itemCount = parent.adapter?.itemCount
            val itemWidth = view.layoutParams.width
            outRect.top = itemPadding
            outRect.bottom = itemPadding
            outRect.left = itemPadding
            outRect.right = itemPadding
            if (itemPosition == 0) {
                outRect.left = (screenWidthPx - itemWidth) / 2
            }

            if (itemPosition == itemCount?.minus(1)) {
                outRect.right = (screenWidthPx - itemWidth) / 2
            }
        }
    }


}
