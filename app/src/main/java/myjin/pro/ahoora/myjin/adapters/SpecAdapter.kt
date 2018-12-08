package myjin.pro.ahoora.myjin.adapters

import android.animation.ObjectAnimator
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
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.activitys.SpecActivity
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel2
import myjin.pro.ahoora.myjin.utils.Converter
import myjin.pro.ahoora.myjin.utils.StaticValues
import java.util.*

import kotlinx.android.synthetic.main.item_specility_layout2.view.*

class SpecAdapter(ctx: Context, provId: Int, cityId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val context = ctx
    private var mProvId = provId
    private var mCityId = cityId
    private val specCountPair = ArrayList<Pair<String, Int>>()
    private val tArr = ArrayList<KotlinSpecialityModel2>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_specility_layout2, parent, false)
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
        val result = realm.where(KotlinSpecialityModel2::class.java).equalTo("saved", true).distinct("specialtyId").findAll()

        realm.commitTransaction()


        result.forEach { item: KotlinSpecialityModel2 ->
            tArr.add(item)
            val c=realm.where(KotlinItemModel::class.java).equalTo("specialityList.specialtyId",item.specialtyId).count()
            specCountPair.add(Pair(item.name.toString(),c.toInt()))
        }

    }

    val width = Converter.getScreenWidthPx(context) / 2
    val height = Converter.pxFromDp(context, 80f).toInt()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).tvSpec.text = specCountPair[position].first
        holder.tvSpecNumber.text= specCountPair[position].second.toString()

        setAnimation(holder.cvSpec)

        Glide.with(context)
                .load(tArr[position].specImg)
                .apply {
                    RequestOptions()
                            .placeholder(R.color.white)
                }
                .into(holder.ivSpec)
    }


    private fun setAnimation(viewToAnimate: View) {
        val a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 300f, 0f)

        val r = Random()
        val i1 = r.nextInt(200) + 250
        a.duration = i1.toLong()
        a.start()

    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {

            val array = ArrayList<Int>()
            array.add(tArr[adapterPosition].specialtyId)

            val k = Intent(context, OfficeActivity::class.java)
            k.putExtra(StaticValues.CATEGORY, 1)
            k.putExtra(StaticValues.PROVID, mProvId)
            k.putExtra(StaticValues.CITYID, mCityId)
            k.putIntegerArrayListExtra("spArray", array)
            (context as SpecActivity).filterArray.clear()
            context.startActivity(k)
        }

        val ivSpec: AppCompatImageView =itemView.iv_spec
        val tvSpec: AppCompatTextView = itemView.tv_spec
        val tvSpecNumber: AppCompatTextView = itemView.tvSpecNumber
        val cvSpec: MaterialCardView = itemView.cv_spec

       /* fun setSpecText(str:String){

        }*/

        init {
            cvSpec.setOnClickListener(this)
        }
    }

}