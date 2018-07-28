package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.customClasses.SVGLoader.GlideApp
import myjin.pro.ahoora.myjin.customClasses.SVGLoader.SvgSoftwareLayerSetter
import java.util.*


class IntroAdapter(context: Context, list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mContext = context

    val item = list

    private val requestBuilder: RequestBuilder<PictureDrawable> = GlideApp.with(context)
            .`as`(PictureDrawable::class.java)
            .placeholder(R.drawable.ic_doctor_f)
            .error(R.drawable.ic_neterror)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(SvgSoftwareLayerSetter())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_big_item_intro, parent, false)
        return ImageHolder(v)
    }

    override fun getItemCount(): Int {

        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ImageHolder
        try {
            /*Glide.with(mContext)
                    .load(item.get(position))
                    .apply(RequestOptions()
                            .placeholder(R.color.colorAccent))
                    .into((holder as ImageHolder)
                            .ivImage)

            */
            requestBuilder.load(item[position]).into(holder.ivImage)

        } catch (e: Exception) {
            Log.e("glideErrIntro", e.message + " ")
        }


    }

    internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvImage = itemView.findViewById<CardView>(R.id.cv_bigItem)
        val ivImage = itemView.findViewById<ImageView>(R.id.iv_imageBig)

    }


}
