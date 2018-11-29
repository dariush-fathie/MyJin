package myjin.pro.ahoora.myjin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_advice.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.AdviceAdapter
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import java.util.ArrayList


class AdviceFragment : Fragment() {

    private val buffer = ArrayList<KotlinAdviceModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_advice, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillBuffer()
        setAdapter()
    }

    fun fillBuffer(){
        val realm= Realm.getDefaultInstance()
        realm.beginTransaction()
        val res=  realm.where(KotlinAdviceModel::class.java).findAll()
        realm.commitTransaction()

        res.forEach { item->
            buffer.add(item)
        }
    }

    private fun setAdapter(){

        rv_advice.layoutManager = LinearLayoutManager(activity!!)
        rv_advice.adapter = AdviceAdapter(activity!!,buffer)
    }

}
