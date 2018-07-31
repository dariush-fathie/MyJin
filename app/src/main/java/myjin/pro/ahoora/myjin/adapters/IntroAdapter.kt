package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.customClasses.SVGLoader.GlideApp
import myjin.pro.ahoora.myjin.customClasses.SVGLoader.SvgSoftwareLayerSetter
import myjin.pro.ahoora.myjin.models.KotlinSlideModel
import myjin.pro.ahoora.myjin.utils.Colors
import java.util.*


class IntroAdapter(private val context: Context, private val list: Array<KotlinSlideModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val requestBuilder: RequestBuilder<PictureDrawable> = GlideApp.with(context)
            .`as`(PictureDrawable::class.java)
            .placeholder(R.drawable.pl_ho_intro)
            .error(R.drawable.ic_neterror)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(SvgSoftwareLayerSetter())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.image_big_item_intro, parent, false)
        return ImageHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
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

            requestBuilder.load(list[position].fileUrl).into(holder.ivImage)
            holder.tvDescription.text = list[position].description
            holder.rootLayout.setBackgroundColor(getColor(position))

        } catch (e: Exception) {
            Log.e("glideErrIntro", e.message + " ")
        }


    }

    private val colorUtil = Colors(context)

    internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage: AppCompatImageView = itemView.findViewById(R.id.iv_imageBig)
        val tvDescription: AppCompatTextView = itemView.findViewById(R.id.tv_slides_content)
        val rootLayout: ConstraintLayout = itemView.findViewById(R.id.cl_slidesRoot)

    }

    private fun getColor(position: Int): Int {
        when (position) {
            0 -> return colorUtil.green
            1 -> return colorUtil.orange
            2 -> return colorUtil.niceBlue
            3 -> return colorUtil.title
            4 -> return colorUtil.blue3
            5 -> return colorUtil.red1
        }
        return randomColor()
    }

    private fun randomColor(): Int {
        val random = Random()
        when (Math.abs(random.nextInt(5))) {
            0 -> return colorUtil.green
            1 -> return colorUtil.orange
            2 -> return colorUtil.niceBlue
            3 -> return colorUtil.title
            4 -> return colorUtil.blue3
            5 -> return colorUtil.red1
        }
        return colorUtil.green
    }

}
