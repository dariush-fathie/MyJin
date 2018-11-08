package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import io.realm.Realm
import io.realm.Sort
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel

class TAdapter(ctx: Context, filterList: ArrayList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val idsArray = ArrayList<Int>()
    val context = ctx
    private val tArr = ArrayList<String>()
    val tIds = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.t_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return tArr.size
    }

    init {
        fillBuffer()
        filterList.forEach { i: Int ->
            idsArray.add(i)
        }
    }

    private fun fillBuffer() {
        tArr.clear()
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(KotlinSpecialityModel::class.java).equalTo("saved", true).distinct("specialtyId").findAll()

        result?.sort("name", Sort.ASCENDING)
        realm.commitTransaction()
        result.forEach { item: KotlinSpecialityModel ->
            tArr.add(item.name!!)
            tIds.add(item.specialtyId)
        }
    }

    fun clearSelections() {
        idsArray.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).tTitle.text = tArr[position]
        val x = tIds[position]
        holder.cb.isChecked = idsArray.contains(x)
    }
    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var f = false

            idsArray.forEach { i: Int ->
                if (i == tIds[adapterPosition]) {
                    f = true
                }
            }
            if (!f) {
                idsArray.add(tIds[adapterPosition])
                cb.isChecked = true

            } else {
                cb.isChecked = false
                idsArray.remove(tIds[adapterPosition])
            }

        }

        val tTitle: AppCompatTextView = itemView.findViewById(R.id.tv_t_title)
        private val cvTItem: MaterialCardView = itemView.findViewById(R.id.cv_t_item)
        val cb: AppCompatCheckBox = itemView.findViewById(R.id.cb_tItem)

        init {
            cvTItem.setOnClickListener(this)
            cb.isClickable = false
        }
    }

}