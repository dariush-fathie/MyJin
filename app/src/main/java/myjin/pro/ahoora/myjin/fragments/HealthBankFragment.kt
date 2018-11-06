package myjin.pro.ahoora.myjin.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import myjin.pro.ahoora.myjin.utils.Converter
import kotlinx.android.synthetic.main.fragment_health_bank.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.MainActivity2
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.activitys.SpecActivity
import myjin.pro.ahoora.myjin.customClasses.OnSpinerItemClick
import myjin.pro.ahoora.myjin.customClasses.SpinnerDialog
import myjin.pro.ahoora.myjin.customClasses.ThreeColGridDecorationCatagory
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import myjin.pro.ahoora.myjin.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HealthBankFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_location -> {
                openProvAndCityDialog()
            }
            R.id.btn_healthBankTryAgain -> {
                tryAgain()
            }
        }
    }

    private var provId = 19
    private var cityId = 0

    private var loadFlag = false

    private var scrollToBottom = true
    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            scrollToBottom = dy > 0
        }
    }

    private var netAvailability = false


    @Subscribe
    fun netEvent(e: NetChangeEvent) {
        netAvailability = e.isCon
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_health_bank, container, false)
    }

    override fun onStop() {
        mainList.removeOnScrollListener(scrollListener)
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mainList.addOnScrollListener(scrollListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity2).tvLocation.setOnClickListener(this)
        netAvailability = NetworkUtil().isNetworkAvailable(activity as Context)

        btn_healthBankTryAgain.setOnClickListener(this)

        getCityAndProvFromSp()
        checkNetState()
    }


    private fun getCityAndProvFromSp() {
        val sp = SharedPer(activity as Context)
        provId = sp.getInteger(getString(R.string.provId))
        cityId = sp.getInteger(getString(R.string.cityId))
        var n = sp.getString(getString(R.string.provCityPair))
        if (n == "") {
            n = "کردستان"
            provId = 19
            cityId = 0
        }
        (activity as MainActivity2).tvLocation.text = n
    }

    private fun checkNetState() {
        if (netAvailability) {
            hideErrLayout()
            getGroupCount()
        } else {
            // error
            showErrLayout()
        }
    }


    private fun tryAgain() {
        checkNetState()
    }

    private fun showErrLayout() {
        tv_healthBankText.visibility = View.VISIBLE
        btn_healthBankTryAgain.visibility = View.VISIBLE
    }

    private fun hideErrLayout() {
        tv_healthBankText.visibility = View.GONE
        btn_healthBankTryAgain.visibility = View.GONE

    }

    private var lock = false

    private fun getGroupCount() {
        if (!lock) {
            lock = true
            showCPV()
            hideErrLayout()
            // (activity as MainActivity2).hideSearchFab()

            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.getGroupCount(provId, cityId)
            response.enqueue(object : Callback<List<KotlinGroupModel>> {
                override fun onResponse(call: Call<List<KotlinGroupModel>>?, response: Response<List<KotlinGroupModel>>?) {
                    response?.body() ?: onFailure(call, Throwable("null body"))
                    response?.body() ?: return
                    val list: List<KotlinGroupModel>? = response.body()

                    var c = 0
                    list!!.forEach { item ->
                        c += item.counter
                    }

                    (activity as MainActivity2).tvLocation.append(" ($c مرکز)")
                    val realm = Realm.getDefaultInstance()
                    realm.executeTransactionAsync { db: Realm? ->
                        db?.where(KotlinGroupModel::class.java)?.findAll()?.deleteAllFromRealm()
                        db?.copyToRealm(list)
                        /*val r = db?.where(KotlinGroupModel::class.java)?.findAll()
                        r?.forEach { model: KotlinGroupModel? ->
                            Log.e("GM", "${model?.name}:${model?.groupId}")
                        }*/
                    }
                    //val c = list!!.size
                    // todo : control
                    //AllCentars.text = "$c مرکز "

                    loadAdapter(list)
                    lock = false


                }

                override fun onFailure(call: Call<List<KotlinGroupModel>>?, t: Throwable?) {
                    Toast.makeText(activity, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                    lock = false
                    loadFlag = false

                    showErrLayout()
                    hideCPV()
                }
            })
        }
    }

    private lateinit var adapter: CategoryAdapter

    private fun loadAdapter(list: List<KotlinGroupModel>) {
        // one second delay to see animation
        Handler().postDelayed({

            activity as Context
            adapter = CategoryAdapter(activity!!, list)
            mainList.layoutManager = GridLayoutManager(activity, 3)

            while (mainList.itemDecorationCount > 0) {
                mainList.removeItemDecorationAt(0)
            }

            val itemDecoration = ThreeColGridDecorationCatagory(activity as Context, 8)
            mainList.addItemDecoration(itemDecoration)


            Handler().postDelayed({
                hideCPV()
                //(activity as MainActivity2).showSearchFab()
                mainList.adapter = adapter
            }, 400)

        }, 500)

        loadFlag = true
    }

    private fun showCPV() {
        rl_hbf.visibility = View.VISIBLE
    }

    private fun hideCPV() {
        rl_hbf.visibility = View.GONE
    }

    private fun openProvAndCityDialog() {
        val dialog = SpinnerDialog(activity, getString(R.string.jmdssh), getString(R.string.nemikham))
        dialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(var1: String, var2: Int, var3: Int) {
                //initList()
                if (var2 != 19) {
                    Toast.makeText(activity, getString(R.string.ebdhhtokpm), Toast.LENGTH_LONG).show()
                } else {
                    (activity as MainActivity2).tvLocation.text = var1
                    this@HealthBankFragment.provId = var2
                    this@HealthBankFragment.cityId = var3
                    saveProv(var1)
                    loadFlag = false
                    clearAdapter()
                    // todo : check net connection first please
                    getGroupCount()
                }
            }
        })
        dialog.showSpinerDialog()
    }

    private fun clearAdapter() {
        mainList.adapter = null
    }

    private fun saveProv(name: String) {
        val sp = SharedPer(activity as Context)
        sp.setInteger(getString(R.string.provId), provId)
        sp.setInteger(getString(R.string.cityId), cityId)
        sp.setString(getString(R.string.provCityPair), name)
    }

    inner class CategoryAdapter(private val context: Context, gList: List<KotlinGroupModel>)
        : RecyclerView.Adapter<CategoryAdapter.ItemHolder>() {

        var groupsList: List<KotlinGroupModel> = gList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view: View = LayoutInflater.from(context)
                    .inflate(R.layout.main_category_item, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int {
            return groupsList.size
        }


        val width = Converter.getScreenWidthPx(context) / 2
        val height = Converter.pxFromDp(context, 80f).toInt()

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            if (groupsList.size > position) {
                holder.subTitle.text = "${groupsList.get(position).counter}  مرکز"
                holder.titleTv.text = groupsList.get(position).name
            } else {
                holder.subTitle.text = "0  مرکز"
            }
            setAnimation(holder.container)

            Glide.with(context)
                    .load(groupsList[position].g_url)
                    .apply(RequestOptions()
                            .override(width, height)
                            .centerInside())
                    .into(holder.imageView)
        }

        private fun setAnimation(viewToAnimate: View) {
            var a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 300f, 0f)
            if (!scrollToBottom) {
                a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -300f, 0f)
            }
            val r = Random()
            val i1 = r.nextInt(200) + 250
            a.duration = i1.toLong()
            a.start()

        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            override fun onClick(v: View?) {
                if (groupsList[adapterPosition].counter > 0) {

                    val i: Intent
                    if (groupsList[adapterPosition].groupId > 1) {
                        i = Intent(activity, OfficeActivity::class.java)
                    } else {
                        i = Intent(activity, SpecActivity::class.java)
                    }

                    i.putExtra(StaticValues.CATEGORY, groupsList.get(adapterPosition).groupId)
                    i.putExtra(StaticValues.PROVID, provId)
                    i.putExtra(StaticValues.CITYID, cityId)
                    startActivity(i)
                } else {
                    popupToast()
                }
            }

            val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_mainCategoryImage)
            val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategoryTitle)
            val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategorySubTitle)
            val container: CardView = itemView.findViewById(R.id.cv_mainCategoryContainer)

            init {
                container.setOnClickListener(this)
            }
        }

        private fun popupToast() {

            val builder = AlertDialog.Builder(context)
            val dialog: AlertDialog
            val view = View.inflate(context, R.layout.pop_win_for_g, null)
            builder.setView(view)
            dialog = builder.create()
            dialog.show()
        }

    }

}
