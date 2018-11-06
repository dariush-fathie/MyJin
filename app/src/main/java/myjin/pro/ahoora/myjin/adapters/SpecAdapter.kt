package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import io.realm.Realm
import io.realm.Sort
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.activitys.SpecActivity
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel
import myjin.pro.ahoora.myjin.utils.Converter
import myjin.pro.ahoora.myjin.utils.StaticValues

class SpecAdapter(ctx: Context, provId: Int, cityId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val context = ctx
    private var mProvId = provId
    private var mCityId = cityId


    private val tArr = ArrayList<KotlinSpecialityModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_specility_layout, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return tArr.size
    }

    init {
        fillBuffer()
    }

    private fun fillBuffer() {
        tArr.clear()
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(KotlinSpecialityModel::class.java).equalTo("saved", true).distinct("specialtyId").findAll()
        result?.sort("name", Sort.ASCENDING)
        realm.commitTransaction()

        result.forEach { item: KotlinSpecialityModel ->
            tArr.add(item)
        }
    }

    val width = Converter.getScreenWidthPx(context) / 2
    val height = Converter.pxFromDp(context, 80f).toInt()
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).tvSpec.text = tArr[position].name

        Glide.with(context)
                .load(tArr[position].specImg)
                .apply(RequestOptions()
                        .override(width, height)
                        .centerInside())
                .into(holder.ivSpec)
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {

            val array = ArrayList<Int>()
            array.add(tArr[adapterPosition].specialtyId)

            val k = Intent(context, OfficeActivity::class.java)
            k.putExtra(StaticValues.CATEGORY, 1)
            k.putExtra(StaticValues.PROVID, mProvId)
            k.putExtra(StaticValues.CITYID, mCityId)
            k.putIntegerArrayListExtra("spArray",array)
            (context as SpecActivity).filterArray.clear()
            context.startActivity(k)
        }

         val ivSpec: AppCompatImageView = itemView.findViewById(R.id.iv_spec)
         val tvSpec: AppCompatTextView = itemView.findViewById(R.id.tv_spec)
         private val cvSpec: MaterialCardView = itemView.findViewById(R.id.cv_spec)


        init {
            cvSpec.setOnClickListener(this)
        }
    }

}