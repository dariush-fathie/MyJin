package myjin.pro.ahoora.myjin.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_health_bank.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.TestAdapter
import myjin.pro.ahoora.myjin.customClasses.GridItemDecoration

class HealthBankFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_health_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity as Context

        mainList.layoutManager = GridLayoutManager(activity, 2)
        mainList.addItemDecoration(GridItemDecoration(activity, 8))
        mainList.adapter = TestAdapter(activity!!)

    }


}
