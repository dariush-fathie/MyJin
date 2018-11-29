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

class UsefullToolFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_usefull_tool, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
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

    private fun customToast() {
        CustomToast().Show_Toast(context, cl_usefultool,
                getString(R.string.early))
    }

}
