package myjin.pro.ahoora.myjin.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.tools.BMIChartActivity
import myjin.pro.ahoora.myjin.utils.Converter
import java.util.*
import kotlin.collections.ArrayList
class UseFoolAdapter(private val context: Context )
    : RecyclerView.Adapter<UseFoolAdapter.ItemHolder>() {


    private val groupsListPair = ArrayList<Pair<String, Int>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view: View = LayoutInflater.from(context)
                .inflate(R.layout.main_tooluseful_item, parent, false)
        return ItemHolder(view)
    }
    init {
        groupsListPair.add(Pair("شاخص توده بدنی",R.drawable.ic_bmi))
        groupsListPair.add(Pair("بانک دارو",R.drawable.ic_daro))
        groupsListPair.add(Pair("نمودار رشد",R.drawable.ic_roshd))
        groupsListPair.add(Pair("یادآوری دارو",R.drawable.ic_yadavari))
    }

    override fun getItemCount(): Int {
        return groupsListPair.size
    }
    val width = Converter.getScreenWidthPx(context) / 2
    val height = Converter.pxFromDp(context, 80f).toInt()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        holder.titleTv.text = groupsListPair[position].first
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,groupsListPair[position].second))

        setAnimation(holder.container)

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
      /*      if (groupsList[adapterPosition].counter > 0) {

           *//*     val array = ArrayList<Int>()
                val i: Intent
                if (groupsList[adapterPosition].groupId > 1) {
                    i = Intent(activity, OfficeActivity::class.java)
                } else {
                    i = Intent(activity, SpecActivity::class.java)
                }

                i.putExtra(StaticValues.CATEGORY, groupsList.get(adapterPosition).groupId)
                i.putExtra(StaticValues.PROVID, provId)
                i.putExtra(StaticValues.CITYID, cityId)
                i.putIntegerArrayListExtra("spArray",array)
                startActivity(i)*//*
            } else {
                popupToast()
            }*/

            if (adapterPosition == 0) {
                context.startActivity(Intent(context, BMIChartActivity::class.java))
            }else{
                Toast.makeText(context,context.getString(R.string.early),Toast.LENGTH_LONG).show()
            }
        }

        val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_mainUseFulImage)
        val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_mainUseFulToolSubTitle)
        val container: CardView = itemView.findViewById(R.id.cv_mainUsefulToolContainer)

        init {
            container.setOnClickListener(this)
        }
    }



}