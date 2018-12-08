package myjin.pro.ahoora.myjin.customClasses


import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

import com.google.android.material.button.MaterialButton

import java.util.ArrayList

import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel
import myjin.pro.ahoora.myjin.utils.SharedPer

class SpinnerDialog(private val context: AppCompatActivity, private val dTitle: String, private val closeTitle: String) {
    private val items: ArrayList<KotlinProvCityModel> = ArrayList()
    private var onSpinerItemClick: OnSpinerItemClick? = null
    private var alertDialog: AlertDialog? = null
    private var provId: Int = 0
    private var cityId: Int = 0
    private val style: Int = 0
    private var name: String? = null
    private var idP = 0
    private var onsearch = false
    val provArr = ArrayList<String>()

    val sp = SharedPer(context)

    fun bindOnSpinerListener(onSpinerItemClick1: OnSpinerItemClick) {
        this.onSpinerItemClick = onSpinerItemClick1
    }

    private fun getNameP() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val res = realm.where(KotlinProvCityModel::class.java).distinct("provId").findAll()
        realm.commitTransaction()
        var i = -1;
        res.forEach { item ->
            i++
            provArr.add(item.nameP.toString())
            if (item.provId == provId) {
                idP = i
            }
        }
    }

    private fun fillBuffer(t: String) {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        val res = realm.where(KotlinProvCityModel::class.java)
                .contains("nameC", t).or().contains("nameP", t).findAll()

        realm.commitTransaction()
        var i = -1;
        if (res.size > 0) {
            provArr.forEach { s ->
                i++
                if (res[0]!!.nameP == s) {
                    idP = i
                }
            }
        }

        items.clear()
        items.addAll(res)
    }

    fun showSpinerDialog() {
        val adb = AlertDialog.Builder(this.context)
        val v = this.context.layoutInflater.inflate(R.layout.dialog_layout, null as ViewGroup?)
        val rippleViewClose = v.findViewById<MaterialButton>(R.id.close)
        val title = v.findViewById<AppCompatTextView>(R.id.spinerTitle)
        val spinnerProv = v.findViewById<Spinner>(R.id.spinner_prov)

        val n = sp.getString(context.getString(R.string.provCityPair))
        if (n == "") {
            rippleViewClose.visibility=View.INVISIBLE
        }

        provId = sp.getInteger(context.getString(R.string.provId))
        getNameP()
        Handler().postDelayed({
            spinnerProv.setSelection(idP)

        },50)

        val provAdapter = ArrayAdapter<String>(context, R.layout.items_view, R.id.text1, provArr)
        provAdapter.setDropDownViewResource(R.layout.items_view)
        spinnerProv.adapter = provAdapter


        rippleViewClose.text = this.closeTitle
        title.text = this.dTitle
        val listView = v.findViewById<RecyclerView>(R.id.list)
        val searchBox = v.findViewById<AppCompatEditText>(R.id.searchBox)


        val layoutmanager = LinearLayoutManager(this.context)
        listView.layoutManager = layoutmanager
        val adapter = AdapterSpiner()
        listView.adapter = adapter
        adb.setView(v)
        this.alertDialog = adb.create()
        this.alertDialog!!.window!!.attributes.windowAnimations = this.style
        this.alertDialog!!.setCancelable(false)



        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                onsearch = true
                fillBuffer(searchBox.text!!.toString())
                spinnerProv.setSelection(idP)
                listView.adapter = null
                listView.adapter = AdapterSpiner()


            }
        })

        rippleViewClose.setOnClickListener { v1 -> this@SpinnerDialog.alertDialog!!.dismiss() }
        this.alertDialog!!.show()


        spinnerProv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (!onsearch) {
                    onsearch = false

                    Log.e("ostan", provArr[i])
                    fillBuffer(provArr[i])
                    listView.adapter = null
                    listView.adapter = AdapterSpiner()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    inner class AdapterSpiner : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View = LayoutInflater.from(context).inflate(R.layout.items_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

          /*  if (items[position].cityId != 0) {
                this@SpinnerDialog.name = items[position].nameP + " ، " + items[position].nameC
            } else {
                this@SpinnerDialog.name = items[position].nameP
            }*/

            this@SpinnerDialog.name = items[position].nameC
            (holder as ViewHolder).text1.text = this@SpinnerDialog.name
        }

        override fun getItemCount(): Int {
            return items.size
        }

        private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            internal var text1: TextView = itemView.findViewById(R.id.text1)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View) {

                this@SpinnerDialog.provId = items[adapterPosition].provId!!
                this@SpinnerDialog.cityId = items[adapterPosition].cityId!!

                var str=text1.text.toString()

                if (this@SpinnerDialog.cityId == 0) {
                   str= items[adapterPosition].nameP!!
                }

                this@SpinnerDialog.onSpinerItemClick!!.onClick(str, this@SpinnerDialog.provId, this@SpinnerDialog.cityId)
                this@SpinnerDialog.alertDialog!!.dismiss()
            }
        }
    }
}


