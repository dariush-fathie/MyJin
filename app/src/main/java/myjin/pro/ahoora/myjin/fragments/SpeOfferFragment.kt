package myjin.pro.ahoora.myjin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_spe_offer.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.models.KotlinSpecialOffersModel
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.NetworkUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpeOfferFragment : Fragment(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    private val uniqueIds = ArrayList<Int>()
    private val tabNames = ArrayList<String>()
    private var realm: Realm = Realm.getDefaultInstance()
    private var loadFlag = false
    lateinit var list: List<KotlinSpecialOffersModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spe_offer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_goUp.setOnClickListener(this)
        ctb_spe_offer.addOnTabSelectedListener(this)
    }


    private fun tryAgain() {
        hideErrLayout()
        if (NetworkUtil().isNetworkAvailable(activity as Context)) {
            getspeOfferList()
        } else {
            showErrLayout()
        }
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {

        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    fun onBecomeVisible(e: VisibilityEvent) {
        if (e.position == 3) {
            if (!loadFlag) {
                tryAgain()
            }
        }
    }

    private var lock = false
    private fun getspeOfferList() {
        if (!lock) {
            lock = true
            hideErrLayout()
            showCPV()
            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.SpecialOffers

            response.enqueue(object : Callback<List<KotlinSpecialOffersModel>> {

                override fun onResponse(call: Call<List<KotlinSpecialOffersModel>>?, response: Response<List<KotlinSpecialOffersModel>>?) {


                    response?.body() ?: onFailure(call, Throwable("null body"))
                    response?.body() ?: return

                    val result = response.body()

                    result ?: onFailure(call, Throwable("null list"))
                    result ?: return

                    list = response.body()!!

                    realm.executeTransactionAsync { realm: Realm? ->
                        realm?.where(KotlinSpecialOffersModel::class.java)
                                ?.findAll()
                                ?.deleteAllFromRealm()
                        realm?.copyToRealm(list)
                    }

                    loadAdapter(list)

                    uniqueIds.clear()
                    tabNames.clear()

                    list.forEach { item ->

                        if (!uniqueIds.contains(item.adGroupId)) {
                            uniqueIds.add(item.adGroupId)
                            tabNames.add(item.adGroupName!!)
                        }

                    }
                    loadFlag = true
                    addTab()



                    hideCPV()
                    hideErrLayout()
                    lock = false

                }

                override fun onFailure(call: Call<List<KotlinSpecialOffersModel>>?, t: Throwable?) {
                    showErrLayout()
                    hideCPV()
                    loadFlag = false
                    lock = false
                }
            })
        }
    }

    private fun loadAdapter(data: List<KotlinSpecialOffersModel>) {
        rv_Spe_offer.layoutManager = LinearLayoutManager(activity as Context)
        while (rv_Spe_offer.itemDecorationCount > 0) {
            rv_Spe_offer.removeItemDecorationAt(0)
        }
        val decor = SimpleItemDecoration(activity as Context, 10)
        rv_Spe_offer.addItemDecoration(decor)
        rv_Spe_offer.adapter = SpeOfferAdapter(data)
    }


    private fun showErrLayout() {
        tv_MessagesText.visibility = View.VISIBLE
        btn_speTryAgain.visibility = View.VISIBLE
        hideCPV()
    }

    private fun hideErrLayout() {
        tv_MessagesText.visibility = View.GONE
        btn_speTryAgain.visibility = View.GONE
    }

    private fun showCPV() {
        rl_speOffer.visibility = View.VISIBLE
    }

    private fun hideCPV() {
        rl_speOffer.visibility = View.GONE
    }

    private fun addTab() {

        if (tabNames.size > 0) {
            ctb_spe_offer.addTab(ctb_spe_offer.newTab().setText("همه"))
            tabNames.forEach { name: String ->

                ctb_spe_offer.addTab(ctb_spe_offer.newTab().setText(name))

            }
        }
    }


    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {

        if (p0!!.position == 0) {
            loadAdapter(list)
        } else {
            realm.beginTransaction()
            val res = realm.where(KotlinSpecialOffersModel::class.java).equalTo("adGroupId", uniqueIds[p0.position - 1]).findAll()
            realm.commitTransaction()
            loadAdapter(res)
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_goUp -> rv_Spe_offer.smoothScrollToPosition(0)
            R.id.btn_speTryAgain -> {
                tryAgain()
            }
        }
    }


    inner class SpeOfferAdapter(data: List<KotlinSpecialOffersModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val dataSet = data
        override fun getItemCount(): Int {
            return dataSet.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(activity as Context).inflate(R.layout.spe_offer_item, parent, false)
            return ImageHolder(v)
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            try {
                Glide.with(activity as Context)
                        .load(dataSet[position].photoUrl)
                        .apply(RequestOptions()
                                .placeholder(R.color.white))
                        .into((holder as SpeOfferAdapter.ImageHolder)
                                .ivImage)
            } catch (e: Exception) {

            }
        }

        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val cvImage: CardView = itemView.findViewById<CardView>(R.id.cv_bigItem)
            val ivImage: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.iv_imageBig)
        }

    }

}
