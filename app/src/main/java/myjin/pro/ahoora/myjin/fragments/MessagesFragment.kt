package myjin.pro.ahoora.myjin.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import ir.paad.audiobook.decoration.VerticalLinearLayoutDecoration
import ir.paad.audiobook.utils.NetworkUtil
import kotlinx.android.synthetic.main.fragment_messages.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.MessagesAdapter
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : Fragment(), TabLayout.OnTabSelectedListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_messagesTryAgain -> {
                tryAgain()
            }
        }
    }

    private var updated = false
    private var loadFlag = false
    private var netAvailability = false


    @Subscribe
    fun netEvent(e: NetChangeEvent) {
        netAvailability = e.isCon
    }

    @Subscribe
    fun onBecomeVisible(e: VisibilityEvent) {
        if (e.position == 1) {
            Log.e(MessagesFragment::class.java.simpleName, "${e.position}")
            if (!loadFlag) {
                if (NetworkUtil().isNetworkAvailable(activity as Context)) {
                    getMessages()
                } else {
                    showErrLayout()
                }
            } else {
                if (updated) {
                    tryAgain()
                }
            }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_messagesTryAgain.setOnClickListener(this)
    }

    private fun loadTabsAndSpinner() {
        v1.visibility = View.VISIBLE
        spinner_sources.visibility = View.VISIBLE
        spinner_types.visibility = View.VISIBLE
        iv_arrowDown1.visibility = View.VISIBLE
        iv_arrowDown2.visibility = View.VISIBLE

        spinner_types.prompt = "دسته بندی"
        spinner_sources.prompt = "منابع"

        val typesArray = ArrayList<String>()
        typesArray.add("ذخیره شده ها")
        typesArray.add("همه")
        typesArray.add("آموزشی")
        typesArray.add("هشدار")
        typesArray.add("اطلاعیه")

        spinner_types.adapter = ArrayAdapter<String>(activity as Context
                , R.layout.spinner_item_layout
                , R.id.tv_spinnerTitle
                , typesArray)

        spinner_types.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("messagesFragment", "types position $position")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val sourceArray = ArrayList<String>()
        // todo load sources
        sourceArray.add("دانشگاه علوم پزشکی")
        sourceArray.add("استانداری استان کردستان")
        sourceArray.add("مجمع عالی پزشکان")
        sourceArray.add("بیمارستان بعثت")

        spinner_sources.adapter = ArrayAdapter<String>(activity as Context
                , R.layout.spinner_item_layout
                , R.id.tv_spinnerTitle
                , sourceArray)

        spinner_sources.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("messagesFragment", "sources position $position")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }


    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }

    private var lock = false

    private fun getMessages() {
        if (!lock) {
            lock = true
            hideErrLayout()
            showCPV()
            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.messages
            response.enqueue(object : Callback<List<KotlinMessagesModel>> {
                override fun onResponse(call: Call<List<KotlinMessagesModel>>?, response: Response<List<KotlinMessagesModel>>?) {

                    response?.body() ?: onFailure(call, Throwable("null body"))
                    response?.body() ?: return

                    val result = response.body()

                    result ?: onFailure(call, Throwable("null list"))
                    result ?: return

                    // todo saveMessages in realm
                    result.forEach { item: KotlinMessagesModel ->
                        Log.e("Messages", item.toString())
                    }

                    loadTabsAndSpinner()

                    loadAdapter(result)
                    loadFlag = true

                    hideCPV()
                    hideErrLayout()
                    lock = false
                }

                override fun onFailure(call: Call<List<KotlinMessagesModel>>?, t: Throwable?) {
                    Log.e("Messages", "${t?.message} - error")
                    loadFlag = false
                    showErrLayout()
                    hideCPV()
                    lock = false
                }
            })
        }
    }

    private fun tryAgain() {
        hideErrLayout()
        if (NetworkUtil().isNetworkAvailable(activity as Context)) {
            getMessages()
        } else {
            showErrLayout()
        }
    }

    private fun showErrLayout() {
        tv_MessagesText.visibility = View.VISIBLE
        btn_messagesTryAgain.visibility = View.VISIBLE
    }

    private fun hideErrLayout() {
        tv_MessagesText.visibility = View.GONE
        btn_messagesTryAgain.visibility = View.GONE
    }

    private fun showCPV() {
        cpv_messages.visibility = View.VISIBLE
    }

    private fun hideCPV() {
        cpv_messages.visibility = View.GONE
    }


    private fun loadAdapter(list: List<KotlinMessagesModel>) {
        Log.e("messages", "loadAdapter")
        rv_messages.layoutManager = LinearLayoutManager(activity)
        rv_messages.addItemDecoration(VerticalLinearLayoutDecoration(activity as Context
                , 8, 8, 8, 8))
        rv_messages.adapter = MessagesAdapter(activity as Context, list)

    }

}