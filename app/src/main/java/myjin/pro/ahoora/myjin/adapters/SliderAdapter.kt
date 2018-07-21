package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.paad.audiobook.utils.Converter
import myjin.pro.ahoora.myjin.R
import java.util.*


class SliderAdapter(context: Context, list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext = context
    val item = list


    val width = Converter.getScreenWidthPx(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_big_item, parent, false)
        return ImageHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*try {
            Glide.with(mContext)
                    .load(item.get(position))
                    .apply(RequestOptions()
                            .fitCenter()
                            .placeholder(R.color.colorAccent))
                    .into((holder as ImageHolder)
                            .ivImage)


        } catch (e: Exception) {
            Log.e("glideErr", e.message + " ")
        }*/
    }

    internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val i = Intent()
            i.action = mContext.getString(R.string.reciver)
            mContext.sendBroadcast(i)
        }

        private val cvImage: CardView = itemView.findViewById<CardView>(R.id.cv_bigItem)
        val ivImage: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.iv_imageBig)

        init {
            itemView.setOnClickListener(this)
            val layoutParams = cvImage.layoutParams
            layoutParams.width = width - (0.30 * width).toInt()
            cvImage.layoutParams = layoutParams
        }
    }

}

