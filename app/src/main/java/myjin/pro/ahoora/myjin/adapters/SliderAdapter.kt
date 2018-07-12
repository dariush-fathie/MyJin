package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import myjin.pro.ahoora.myjin.R
import java.util.ArrayList
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent



class SliderAdapter(context: Context,list:ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext = context
    val item = list



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_big_item, parent, false)
        return ImageHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            Glide.with(mContext)
                    .load(item.get(position))
                    .apply(RequestOptions()
                            .fitCenter()
                            .placeholder(R.color.colorAccent))
                    .into((holder as ImageHolder)
                            .ivImage)


        } catch (e: Exception) {
            Log.e("glideErr", e.message + " ")
        }
    }

    internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val i = Intent()
            i.action = mContext.getString(R.string.reciver)
            mContext.sendBroadcast(i)
        }

        val cvImage = itemView.findViewById<CardView>(R.id.cv_bigItem)
        val ivImage = itemView.findViewById<AppCompatImageView>(R.id.iv_imageBig)

        init {
            itemView.setOnClickListener(this)

        }
    }

}

