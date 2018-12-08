package myjin.pro.ahoora.myjin.fragments


import android.content.Context

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_usefull_tool.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.UseFoolAdapter
import myjin.pro.ahoora.myjin.customClasses.CustomToast
import myjin.pro.ahoora.myjin.customClasses.TwoColGridDecoration
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import myjin.pro.ahoora.myjin.models.events.customToastEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class UsefullToolFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_usefull_tool, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {

        EventBus.getDefault().unregister(this)
        super.onStop()

    }

    private fun setAdapter(){
        activity as Context
       val adapter = UseFoolAdapter(activity!!)
        mainList.layoutManager = GridLayoutManager(activity, 2)
        mainList.adapter=adapter

        while (mainList.itemDecorationCount > 0) {
            mainList.removeItemDecorationAt(0)
        }

        val itemDecoration = TwoColGridDecoration(activity as Context, 8)
        mainList.addItemDecoration(itemDecoration)
    }
    @Subscribe
    public fun customToast(t:customToastEvent) {
        CustomToast().Show_Toast(context, cl_usefultool,
                getString(R.string.early))
    }

}
