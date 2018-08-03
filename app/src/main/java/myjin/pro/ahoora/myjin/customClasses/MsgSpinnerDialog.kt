package myjin.pro.ahoora.myjin.customClasses


import android.app.Activity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.interfaces.OnSpinnerItemSelected
import java.util.*


class MsgSpinnerDialog {

    var items: ArrayList<String>
    var context: Activity
    var dTitle: String
    var closeTitle = "بستن"
    private lateinit var onSpinnerItemSelected: OnSpinnerItemSelected
    lateinit var alertDialog: AlertDialog
    var pos: Int = 0
    var style: Int = 0

    constructor(activity: Activity, items: ArrayList<String>, dialogTitle: String) {
        this.items = items
        this.context = activity
        this.dTitle = dialogTitle
    }

    constructor(activity: Activity, items: ArrayList<String>, dialogTitle: String, closeTitle: String) {
        this.items = items
        this.context = activity
        this.dTitle = dialogTitle
        this.closeTitle = closeTitle
    }

    constructor(activity: Activity, items: ArrayList<String>, dialogTitle: String, style: Int) {
        this.items = items
        this.context = activity
        this.dTitle = dialogTitle
        this.style = style
    }

    constructor(activity: Activity, items: ArrayList<String>, dialogTitle: String, style: Int, closeTitle: String) {
        this.items = items
        this.context = activity
        this.dTitle = dialogTitle
        this.style = style
        this.closeTitle = closeTitle
    }

    fun setOnSpinnerItemSelectedListener(i: OnSpinnerItemSelected) {
        this.onSpinnerItemSelected = i
    }

    fun show() {
        val adb = AlertDialog.Builder(this.context)
        val v = this.context.layoutInflater.inflate(R.layout.dialog_msg_layout, null as ViewGroup?)
        val rippleViewClose: AppCompatButton = v.findViewById(R.id.btn_close)
        val title: AppCompatTextView = v.findViewById(R.id.spinerTitle)
        rippleViewClose.text = this.closeTitle
        title.text = this.dTitle
        val listView = v.findViewById<ListView>(R.id.list)
        val searchBox = v.findViewById<AppCompatEditText>(R.id.searchBox)
        val adapter = ArrayAdapter(this.context, R.layout.items_msg_view, this.items)
        listView.adapter = adapter
        adb.setView(v)
        this.alertDialog = adb.create()
        this.alertDialog.window!!.attributes.windowAnimations = this.style
        this.alertDialog.setCancelable(true)
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val t = view.findViewById<View>(R.id.text1) as AppCompatTextView
            for (j in this@MsgSpinnerDialog.items.indices) {
                if (t.text.toString().equals(this@MsgSpinnerDialog.items[j], ignoreCase = true)) {
                    this@MsgSpinnerDialog.pos = j
                }
            }
            this@MsgSpinnerDialog.onSpinnerItemSelected.onClick(t.text.toString(), this@MsgSpinnerDialog.pos)
            this@MsgSpinnerDialog.alertDialog.dismiss()
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                adapter.filter.filter(searchBox.text.toString())
            }
        })
        rippleViewClose.setOnClickListener { this@MsgSpinnerDialog.alertDialog.dismiss() }
        this.alertDialog.show()
    }
}

