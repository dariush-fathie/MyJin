package myjin.pro.ahoora.myjin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.github.rahatarmanahmed.cpv.CircularProgressView
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.adapters.SearchAdapter
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.util.*

class SearchFragment : Fragment(), View.OnClickListener {

    private var rvSearch: RecyclerView? = null
    private var tvItemNum: AppCompatTextView? = null
    private var etSearch: AppCompatEditText? = null
    private var ivSearch: AppCompatImageView? = null
    private var fabGoUp: FloatingActionButton? = null
    private var adapter: SearchAdapter? = null
    private var progressView: CircularProgressView? = null
    private var Shimmer: LinearLayout? = null
    private var groupId = 1
    lateinit var realm: Realm

    private var g_url = ""
    private var g_name = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        try {
            groupId = (activity as OfficeActivity).groupId
        } catch (e: Exception) {
            Log.e("ERR_GID", e.message + "")
        }

        etSearch!!.requestFocus()
        return view
    }


    private fun initViews(view: View) {
        Shimmer = view.findViewById(R.id.ll_shimmer)
        progressView = view.findViewById(R.id.cpv_progress)
        rvSearch = view.findViewById(R.id.rv_search)
        tvItemNum = view.findViewById(R.id.tv_itemNums)
        etSearch = view.findViewById(R.id.et_search)
        ivSearch = view.findViewById(R.id.iv_search)
        fabGoUp = view.findViewById(R.id.fab_goUp)
        ivSearch!!.setOnClickListener(this)
        fabGoUp!!.setOnClickListener(this)
        etSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                dataBaseSearch(editable.toString())
            }
        })

    }

    private fun search() {
        if (etSearch!!.text.toString() != "") {
            dataBaseSearch(etSearch!!.text.toString().trim { it <= ' ' })
        } else {
            Toast.makeText(activity, "نام دکتر نباید خالی باشد", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dataBaseSearch(searchedText: String) {
        var addres = ""
        progressView!!.visibility = View.VISIBLE
        realm = Realm.getDefaultInstance()
        realm.beginTransaction()


        val result = realm.where(KotlinItemModel::class.java).equalTo("groupId", groupId).findAll()
        g_name = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.name!!
        g_url = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.g_url!!

        realm.commitTransaction()
        Log.e("SRS", "${result.size}+  edfsdfs")



        if (result.size > 0) {
            val list1 = ArrayList<KotlinItemModel>()
            for (item in result) {

                val aa = ArrayList<String>()

                for (i in item.addressList!!.indices) {

                }

                if (item.firstName!!.contains(searchedText) || item.lastName!!.contains(searchedText) || addres.contains(searchedText)) {

                    list1.add(item)

                }

            }
            if (list1.size != 0) {
                initList(list1)
            } else {
                tvItemNum!!.text = "موردی یافت نشد ."
                rvSearch!!.adapter = null
            }

        } else {
            tvItemNum!!.text = "موردی یافت نشد ."
            rvSearch!!.adapter = null
        }
        progressView!!.visibility = View.GONE

    }


    @SuppressLint("SetTextI18n")
    private fun initList(dataSet: List<KotlinItemModel>) {
        adapter = SearchAdapter(activity!!, dataSet, g_url, g_name)

        while (rvSearch?.itemDecorationCount!! > 0) {
            rvSearch?.removeItemDecorationAt(0)
        }
        val decor = SimpleItemDecoration(activity, 10)
        rvSearch?.addItemDecoration(decor)

        rvSearch!!.layoutManager = LinearLayoutManager(activity)
        rvSearch!!.adapter = adapter

        rvSearch?.clearOnScrollListeners()
        rvSearch?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val i = (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                Log.e("sfdskfls", "flkdsjflkf")
                if (i > 8) {
                    fabGoUp?.visibility = View.VISIBLE
                } else {
                    fabGoUp?.visibility = View.GONE
                }
            }
        })

        Shimmer?.visibility=View.GONE

        tvItemNum!!.text = dataSet.size.toString() + " مورد پیدا شد ."
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().post("loaded")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_search -> {
                search()
                Utils.closeKeyBoard(etSearch!!.windowToken, activity!!)
            }
            R.id.fab_goUp -> rvSearch?.smoothScrollToPosition(0)
        }
    }
}
