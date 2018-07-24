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
import io.realm.Realm
import ir.paad.audiobook.decoration.VerticalLinearLayoutDecoration
import kotlinx.android.synthetic.main.fragment_messages.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.MainActivity2
import myjin.pro.ahoora.myjin.adapters.MessagesAdapter
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel
import myjin.pro.ahoora.myjin.models.events.TryAgainEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : Fragment(), TabLayout.OnTabSelectedListener {


    private var firstLoad = false
    private var updated = false

    private var loadFlag = false

    @Subscribe
    fun onBecomeVisible(e: VisibilityEvent) {
        if (e.position == 1) {
            Log.e(MessagesFragment::class.java.simpleName, "${e.position}")
            if (firstLoad) {
                getMessages()
            }
            if (updated) {
                // todo : do something honey!!
            }
        }
    }

    @Subscribe
    fun tryAgainEvent(e: TryAgainEvent) {
        // todo load if fragment is visible else wait until become visible
        if (!loadFlag) {
            getMessages()
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

        loadTabs()

        // todo remove this line please
        getMessages()

    }


    private fun loadTabs() {
        ctb_messages.addTab(ctb_messages.newTab().setText("همه"))
        ctb_messages.addTab(ctb_messages.newTab().setText("آموزشی"))
        ctb_messages.addTab(ctb_messages.newTab().setText("هشدار"))
        ctb_messages.addTab(ctb_messages.newTab().setText("اطلاعیه"))
        ctb_messages.addTab(ctb_messages.newTab().setText("سلامت"))
        ctb_messages.addTab(ctb_messages.newTab().setText("طب سنتی"))

        ctb_messages.removeOnTabSelectedListener(this)
        ctb_messages.addOnTabSelectedListener(this)
    }


    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }


    private fun getMessages() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)

        val response = apiInterface.messages
        response.enqueue(object : Callback<List<KotlinMessagesModel>> {
            override fun onResponse(call: Call<List<KotlinMessagesModel>>?, response: Response<List<KotlinMessagesModel>>?) {

                response?.body() ?: onFailure(call, Throwable("null body"))
                response?.body() ?: return

                val result = response.body()
                // todo saveMessages in realm
                result?.forEach { item: KotlinMessagesModel ->
                    Log.e("Messages", item.toString())
                }

                loadAdapter(result!!)
            }

            override fun onFailure(call: Call<List<KotlinMessagesModel>>?, t: Throwable?) {
                Log.e("Messages", "${t?.message} - error")
                loadFlag = false
                (activity as MainActivity2).showNetErrSnack()
            }
        })

    }

    private fun loadAdapter(list: List<KotlinMessagesModel>) {
        rv_messages.layoutManager = LinearLayoutManager(activity)
        rv_messages.addItemDecoration(VerticalLinearLayoutDecoration(activity as Context
                , 8, 8, 8, 8))
        rv_messages.adapter = MessagesAdapter(activity as Context, list)
    }

}