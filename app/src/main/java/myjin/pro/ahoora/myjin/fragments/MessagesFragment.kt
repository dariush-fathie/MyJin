package myjin.pro.ahoora.myjin.fragments

import android.content.Context
import android.content.Intent
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
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
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

    private lateinit var res: RealmResults<KotlinMessagesModel>
    private var updated = false
    private var loadFlag = false
    private var netAvailability = false
    private var realm: Realm = Realm.getDefaultInstance()
    val typesArray = ArrayList<String>()
    val sourceArray = ArrayList<String>()
    val idT = ArrayList<Int>()
    val idS = ArrayList<Int>()


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

    override fun onResume() {
        super.onResume()
        Log.e("Messages", "onResume")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_messagesTryAgain.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("messages", "onresult")
        // todo کارتو اینجا بکن !!
    }


    private fun loadTabsAndSpinner() {
        v1.visibility = View.VISIBLE
        spinner_sources.visibility = View.VISIBLE
        spinner_types.visibility = View.VISIBLE
        iv_arrowDown1.visibility = View.VISIBLE
        iv_arrowDown2.visibility = View.VISIBLE

        spinner_types.prompt = "دسته بندی"
        spinner_sources.prompt = "منابع"


        spinner_types.adapter = ArrayAdapter<String>(activity as Context
                , R.layout.spinner_item_layout
                , R.id.tv_spinnerTitle
                , typesArray)

        spinner_types.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("messagesFragment", "types position $position")
                var posS = idS.get(spinner_sources.selectedItemPosition)

                if (!realm.isInTransaction) {
                    realm.beginTransaction()

                    if (idT.get(position) > 0) {
                        if (posS > 0) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("typeId", idT.get(position)).and().equalTo("groupId", posS).findAll()
                        } else {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("typeId", idT.get(position)).findAll()
                        }

                    } else if (idT.get(position) > -1) {
                        if (posS > 0) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("groupId", posS).findAll()
                        } else {
                            res = realm.where(KotlinMessagesModel::class.java).findAll()
                        }
                    } else {
                        if (posS > 0) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("groupId", posS).and().equalTo("saved", true).findAll()
                        } else {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("saved", true).findAll()
                        }
                    }

                    res = res.sort("regDate", Sort.DESCENDING)
                    realm.commitTransaction()

                    var list = ArrayList<KotlinMessagesModel>()

                    res.forEach { ii ->
                        list.add(ii)
                    }

                    Log.e("rrr", posS.toString() + "  " + idT.get(position))
                    loadAdapter(list)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }




        spinner_sources.adapter = ArrayAdapter<String>(activity as Context
                , R.layout.spinner_item_layout
                , R.id.tv_spinnerTitle
                , sourceArray)

        spinner_sources.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("messagesFragment", "sources position $position")
                var posT = idT.get(spinner_types.selectedItemPosition)

                if (!realm.isInTransaction) {
                    realm.beginTransaction()

                    if (idS.get(position) > 0) {
                        if (posT > 0) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("typeId", posT).and().equalTo("groupId", idS.get(position)).findAll()
                        } else if (posT > -1) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("groupId", idS.get(position)).findAll()
                        } else {
                            res = realm.where(KotlinMessagesModel::class.java)
                                    .equalTo("saved", true).and().equalTo("groupId", idS.get(position)).findAll()
                        }


                    } else {
                        if (posT > 0) {
                            res = realm.where(KotlinMessagesModel::class.java).equalTo("typeId", posT).findAll()
                        } else if (posT > -1) {
                            res = realm.where(KotlinMessagesModel::class.java).findAll()
                        } else {
                            res = realm.where(KotlinMessagesModel::class.java)
                                    .equalTo("saved", true).findAll()
                        }
                    }

                    res = res.sort("regDate", Sort.DESCENDING)
                    realm.commitTransaction()

                    var list = ArrayList<KotlinMessagesModel>()

                    res.forEach { ii ->
                        list.add(ii)
                    }

                    Log.e("rrr", posT.toString() + "  " + idS.get(position))
                    loadAdapter(list)
                }

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

                    realm.executeTransactionAsync { realm: Realm? ->

                        val savedItem = realm?.where(KotlinMessagesModel::class.java)
                                ?.equalTo("saved", true)
                                ?.findAll()
                        val savedItemIds = ArrayList<Int>()
                        savedItem?.forEach { model: KotlinMessagesModel? ->
                            savedItemIds.add(model?.messageId!!)
                        }

                        realm?.where(KotlinMessagesModel::class.java)
                                ?.equalTo("saved", false)
                                ?.findAll()
                                ?.deleteAllFromRealm()

                        result?.forEach { kotlinItemModel: KotlinMessagesModel ->
                            if (savedItemIds.contains(kotlinItemModel.messageId)) {
                                kotlinItemModel.saved = true
                            }
                            realm?.copyToRealmOrUpdate(kotlinItemModel)
                        }
                    }


                    sourceArray.clear()
                    typesArray.clear()
                    idS.clear()
                    idT.clear()

                    sourceArray.add("همه")
                    typesArray.add("همه")
                    idT.add(0)
                    idS.add(0)

                    result.forEach { item: KotlinMessagesModel ->

                        if (!sourceArray.contains(item.groupName)) {
                            sourceArray.add(item.groupName)
                            idS.add(item.groupId)
                        }
                        if (!typesArray.contains(item.type)) {
                            typesArray.add(item.type)
                            idT.add(item.typeId)
                        }
                    }
                    typesArray.add("ذخیره شده ها")
                    idT.add(-1)

                    loadTabsAndSpinner()


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
        rv_messages.layoutManager = LinearLayoutManager(activity)

        while (rv_messages.itemDecorationCount > 0) {
            rv_messages.removeItemDecorationAt(0)
        }

        rv_messages.addItemDecoration(VerticalLinearLayoutDecoration(activity as Context
                , 8, 8, 8, 8).apply { lastItemPadding(48) })
        rv_messages.adapter = MessagesAdapter(activity as Context, list, object : MessagesAdapter.SendIntentForResult {
            override fun send(i: Intent, bundle: Bundle?, requestCode: Int) {
                if (bundle != null) {
                    startActivityForResult(i, requestCode, bundle)
                } else {
                    startActivityForResult(i, requestCode)
                }
            }
        })

    }

}