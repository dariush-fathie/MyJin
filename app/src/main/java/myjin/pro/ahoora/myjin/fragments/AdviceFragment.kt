package myjin.pro.ahoora.myjin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import io.realm.Case
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_advice.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.AdviceAdapter
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import myjin.pro.ahoora.myjin.models.KotlinSpecialOffersModel
import myjin.pro.ahoora.myjin.models.events.SearchMEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.NetworkUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class AdviceFragment : Fragment(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    private val uniqueIds = ArrayList<Int>()
    private val tabNames = ArrayList<String>()
    private var realm: Realm = Realm.getDefaultInstance()
    private var loadFlag = false
    private val buffer = ArrayList<KotlinAdviceModel>()
    lateinit var list: List<KotlinAdviceModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_advice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_goUp.setOnClickListener(this)
        ctb_advice.addOnTabSelectedListener(this)
    }

    private fun showErrLayout() {
        tv_MessagesText.visibility = View.VISIBLE
        btn_adviceTryAgain.visibility = View.VISIBLE
        hideCPV()
    }

    private fun hideErrLayout() {
        tv_MessagesText.visibility = View.GONE
        btn_adviceTryAgain.visibility = View.GONE
    }

    private fun showCPV() {
        rl_speOffer.visibility = View.VISIBLE
    }

    private fun hideCPV() {
        rl_speOffer.visibility = View.GONE
    }

    private fun tryAgain() {
        hideErrLayout()
        if (NetworkUtil().isNetworkAvailable(activity as Context)) {
            getQuesAnsewer()
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
        if (e.position == 0) {
            if (!loadFlag) {
                tryAgain()
            }
        }
    }

    @Subscribe
    fun searchInToMessages(v: SearchMEvent) {

        val value = v.value
        val pos = v.pos
        if (pos == 0) {
            if (value != "") {
                realm.beginTransaction()
                val res = realm.where(KotlinAdviceModel::class.java)
                        ?.contains("title", value, Case.INSENSITIVE)?.or()?.contains("context", value, Case.INSENSITIVE)?.findAll()
                res?.sort("regDate", Sort.DESCENDING)
                realm.commitTransaction()

                setAdapter(res!!)



                uniqueIds.clear()
                tabNames.clear()

                list.forEach { item ->
                    if (!uniqueIds.contains(item.idType)) {
                        uniqueIds.add(item.idType)
                        tabNames.add(item.type!!)
                    }
                }
                ctb_advice?.removeAllTabs()
                addTab()
                loadFlag = true

                hideCPV()
                hideErrLayout()
                lock = false

            } else {
                lock = false
                tryAgain()
            }
        }

    }

    private var lock = false
    private fun getQuesAnsewer() {
        if (!lock) {
            lock = true
            hideErrLayout()
            showCPV()
            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.quesAndAnswer

            response.enqueue(object : Callback<List<KotlinAdviceModel>> {
                override fun onResponse(call: Call<List<KotlinAdviceModel>>?, response: Response<List<KotlinAdviceModel>>?) {

                    response?.body() ?: onFailure(call, Throwable("null body"))
                    response?.body() ?: return

                    val result = response.body()
                    result ?: onFailure(call, Throwable("null list"))
                    result ?: return

                    list = response.body()!!

                    realm.executeTransactionAsync { db: Realm? ->
                        db?.where(KotlinAdviceModel::class.java)?.findAll()?.deleteAllFromRealm()
                        db?.copyToRealm(list)
                    }

                    setAdapter(list)

                    uniqueIds.clear()
                    tabNames.clear()

                    list.forEach { item ->
                        if (!uniqueIds.contains(item.idType)) {
                            uniqueIds.add(item.idType)
                            tabNames.add(item.type!!)
                        }
                    }
                    loadFlag = true

                    ctb_advice?.removeAllTabs()
                    addTab()

                    hideCPV()
                    hideErrLayout()
                    lock = false
                }

                override fun onFailure(call: Call<List<KotlinAdviceModel>>?, t: Throwable?) {
                    Toast.makeText(activity as Context, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun addTab() {
        if (tabNames.size > 0) {
            ctb_advice.addTab(ctb_advice.newTab().setText("همه"))
            tabNames.forEach { name: String ->
                ctb_advice.addTab(ctb_advice.newTab().setText(name))
            }
        }
    }

    private fun setAdapter(data: List<KotlinAdviceModel>) {

        rv_advice.layoutManager = LinearLayoutManager(activity!!)
        rv_advice.adapter = AdviceAdapter(activity!!, data)
    }


    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        if (p0!!.position == 0) {
            setAdapter(list)
        } else {
            realm.beginTransaction()
            val res = realm.where(KotlinAdviceModel::class.java).equalTo("idType", uniqueIds[p0.position - 1]).findAll()
            realm.commitTransaction()
            setAdapter(res)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_goUp -> rv_advice.smoothScrollToPosition(0)
            R.id.btn_speTryAgain -> {
                tryAgain()
            }
        }
    }


}
