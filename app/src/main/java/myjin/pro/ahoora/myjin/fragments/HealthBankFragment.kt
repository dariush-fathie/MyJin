package myjin.pro.ahoora.myjin.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import ir.paad.audiobook.utils.Converter
import kotlinx.android.synthetic.main.fragment_health_bank.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.MainActivity2
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.activitys.ServerStatusActivity
import myjin.pro.ahoora.myjin.customClasses.SpinnerDialog
import myjin.pro.ahoora.myjin.customClasses.TwoColGridDecoration
import myjin.pro.ahoora.myjin.interfaces.TempListener
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import myjin.pro.ahoora.myjin.models.events.TryAgainEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
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
        }
    }

    private var provId = 19
    private var cityId = 0
    private var loadFlag = false
    private var scrollToBottom = true

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            scrollToBottom = dy > 0
        }
    }


    @Subscribe
    fun onBecomeVisible(e: VisibilityEvent){
        if (e.position == 0) {
            Log.e(MessagesFragment::class.java.simpleName , "${e.position}")
        }
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

        // todo get pro and city from sharedPrefs
        (activity as MainActivity2).tvLocation.text = "کردستان،همه شهرها"

        checkNetState()
    }

    private fun checkNetState() {
        if (true) {
            getGroupCount()
        } else {
            // error
        }
    }

    private fun getGroupCount() {
        showCPV()
        (activity as MainActivity2).hideSearchFab()
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.getGroupCount(provId, cityId)
        response.enqueue(object : Callback<List<KotlinGroupModel>> {
            override fun onResponse(call: Call<List<KotlinGroupModel>>?, response: Response<List<KotlinGroupModel>>?) {
                response?.body() ?: onFailure(call, Throwable("null body"))
                response?.body() ?: return

                val list: List<KotlinGroupModel>? = response.body()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinGroupModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)
                    /*val r = db?.where(KotlinGroupModel::class.java)?.findAll()
                    r?.forEach { model: KotlinGroupModel? ->
                        Log.e("GM", "${model?.name}:${model?.groupId}")
                    }*/
                }
                //val c = list!!.size
                // todo : control
                //AllCentars.text = "$c مرکز "

                loadAdapter(list!!)
            }

            override fun onFailure(call: Call<List<KotlinGroupModel>>?, t: Throwable?) {
                Toast.makeText(activity, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                loadFlag = false
                showNetErrSnack()
                // todo : control
                //hideProgressLayout()
                //showNetErrLayout()
            }
        })
    }

    private fun showNetErrSnack() {
        (activity as MainActivity2).showNetErrSnack()
    }

    private lateinit var adapter: CategoryAdapter

    private fun loadAdapter(list: List<KotlinGroupModel>) {
        // one second delay to see animation
        Handler().postDelayed({
            hideCPV()
            activity as Context
            adapter = CategoryAdapter(activity!!, list)
            mainList.layoutManager = GridLayoutManager(activity, 2)

            while (mainList.itemDecorationCount > 0) {
                mainList.removeItemDecorationAt(0)
            }

            val itemDecoration = TwoColGridDecoration(activity, 8)
            mainList.addItemDecoration(itemDecoration)
            mainList.adapter = adapter

            Handler().postDelayed({
                (activity as MainActivity2).visibleSearchFab()
            }, 500)

        }, 500)

        loadFlag = true
    }

    private fun showCPV() {
        cpv_hbf.visibility = View.VISIBLE
    }

    private fun hideCPV() {
        cpv_hbf.visibility = View.GONE
    }


    @Subscribe
    fun netEvent(e: NetChangeEvent) {

    }

    @Subscribe
    fun tryAgainEvent(e: TryAgainEvent) {
        if (!loadFlag) {
            // todo : load again
            getGroupCount()
        }
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
                            .centerCrop())
                    .into(holder.imageView)
        }

        private fun setAnimation(viewToAnimate: View) {
            var a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 200f, 0f)
            if (!scrollToBottom) {
                a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -200f, 0f)
            }
            val r = Random()
            val i1 = r.nextInt(200) + 50
            a.duration = i1.toLong()
            a.start()

        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, TempListener {

            override fun IsOk() {
                if (groupsList[adapterPosition].counter > 0) {
                    val i = Intent(context, OfficeActivity::class.java)
                    i.putExtra(StaticValues.CATEGORY, groupsList.get(adapterPosition).groupId)
                    i.putExtra(StaticValues.PROVID, provId)
                    i.putExtra(StaticValues.CITYID, cityId)
                    startActivity(i)
                } else {
                    Toast.makeText(context, "پایگاه داده ژین در حال تکمیل شدن اطلاعات است ...", Toast.LENGTH_SHORT).show()
                }
            }

            override fun IsNotOk() {
                if (VarableValues.NetworkState) {
                    startActivity(Intent(context, ServerStatusActivity::class.java))
                    // todo : what is this ?!!
                    activity?.finish()
                } else {
                    // todo : control
                    //showNetErrLayout()
                    Toast.makeText(context, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClick(v: View?) {
                if (Utils.isNetworkAvailable(activity as Context)) {
                    ss.IsOkServer()
                } else {
                    // todo : control
                    //showNetErrLayout()
                    Toast.makeText(context, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
                }
            }

            val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_mainCategoryImage)
            val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategoryTitle)
            val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategorySubTitle)
            val container: CardView = itemView.findViewById(R.id.cv_mainCategoryContainer)
            val ss: ServerStatus

            init {
                container.setOnClickListener(this)
                ss = ServerStatus(this, context)
            }
        }

    }


    private fun openProvAndCityDialog() {
        val dialog = SpinnerDialog(activity, "ژین من در سایر شهر", "نمیخوام")
        dialog.bindOnSpinerListener { name, provId, cityId ->
            //initList()
            if (provId != 19) {
                Toast.makeText(activity, "این برنامه در حال حاضر تنها استان کردستان را پوشش می دهد ..", Toast.LENGTH_LONG).show()
            } else {
                (activity as MainActivity2).tvLocation.text = name
                this.provId = provId
                this.cityId = cityId
                loadFlag = false
                getGroupCount()
            }
        }
        dialog.showSpinerDialog()
    }
}
