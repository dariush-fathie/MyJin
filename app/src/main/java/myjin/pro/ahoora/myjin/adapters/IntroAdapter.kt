package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinSlideModel


class IntroAdapter(private val context: Context, private val list: Array<KotlinSlideModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   /* private val requestBuilder: RequestBuilder<PictureDrawable> = GlideApp.with(context)
            .`as`(PictureDrawable::class.java)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(SvgSoftwareLayerSetter())*/

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
            Glide.with(context)
                    .load(list[position].fileUrl)
                    .apply(RequestOptions()
                            .placeholder(R.color.green))
                    .into((holder as ImageHolder)
                            .ivImage)


           // requestBuilder.load(list[position].fileUrl).into(holder.ivImage)
            holder.tvDescription.text = list[position].description
            val bg="#ff"+list[position].arrange
            holder.rootLayout.setBackgroundColor(Color.parseColor(bg))

        } catch (e: Exception) {
            Log.e("glideErrIntro", e.message + " ")
        }

    }


    internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage: AppCompatImageView = itemView.findViewById(R.id.iv_imageBig)
        val tvDescription: AppCompatTextView = itemView.findViewById(R.id.tv_slides_content)
        val rootLayout: ConstraintLayout = itemView.findViewById(R.id.cl_slidesRoot)

    }

}
