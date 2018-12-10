package myjin.pro.ahoora.myjin.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activities.DetailActivity
import myjin.pro.ahoora.myjin.activities.NoDetailActivity
import myjin.pro.ahoora.myjin.activities.OfficeActivity
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.utils.SharedPer
import myjin.pro.ahoora.myjin.utils.StaticValues
import java.util.*

class SearchAdapter(ctx: Context, data: List<KotlinItemModel>, gUrl: String, gName: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val dataSet = data
    private var g_url=gUrl
    private var g_name=gName
    private lateinit var shp: SharedPer
    private var active2 = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }

    private fun setAnimation(viewToAnimate: View) {

        val r = Random()
        val i1 = r.nextInt(200) + 250

        val a = ObjectAnimator.ofFloat(viewToAnimate, "translationX", -300f, 0f)
        a.duration = i1.toLong()
        a.start()

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ItemHolder
        holder.title.text = dataSet.get(position).firstName + " " + dataSet.get(position).lastName

        var str = ""
        if (!dataSet.get(position).gen.equals("0")) {
            if (dataSet.get(position).groupId == 1) {
                str = dataSet.get(position).levelList!![0]?.name + " _ " + dataSet.get(position).specialityList!![0]?.name
            } else {
                str = g_name
            }

        } else {
            str = g_name
        }

        holder.subTitle.text = str
        holder.tv_addr.text = dataSet.get(position).addressList!![0]?.locTitle
        setAnimation(holder.cv_gi)

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_jin)
        var url = ""

        holder.image.colorFilter = null
        if (dataSet.get(position).logoImg.equals("")) {


            if (dataSet.get(position).gen?.equals("0")!!) {
                holder.image.setColorFilter(ContextCompat.getColor(context, R.color.mc_icon_color),android.graphics.PorterDuff.Mode.SRC_IN)

                url = g_url
            } else if (dataSet.get(position).gen?.equals("1")!!) {

                url = context.getString(R.string.ic_doctor_f)
            } else if (dataSet.get(position).gen?.equals("2")!!) {

                url = context.getString(R.string.ic_doctor_m)
            }

        } else {

            url = dataSet.get(position).logoImg!!
        }

        Glide.with(context)
                .load(url)
                .apply {
                    RequestOptions()
                            .placeholder(drawable)
                }
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("RestrictedApi")
        override fun onClick(v: View?) {

            if (dataSet[adapterPosition].active2 != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation((context as OfficeActivity), image, "transition_name")

                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra(StaticValues.ID, dataSet[adapterPosition].centerId)
                    i.putExtra(StaticValues.MODEL, 0)
                    i.putExtra("g_url", g_url)
                    (context as OfficeActivity).startActivityForResult(i, StaticValues.requestCodeOfficeDetail, options.toBundle())

                } else {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra(StaticValues.ID, dataSet[adapterPosition].centerId)
                    i.putExtra(StaticValues.MODEL, 0)
                    i.putExtra("g_url", g_url)
                    context.startActivity(i)
                }
            }else{
                val j = Intent(context, NoDetailActivity::class.java)
                (context as OfficeActivity).startActivity(j)
            }



        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val tv_addr: AppCompatTextView = itemView.findViewById(R.id.tv_addr)
        val item: ConstraintLayout = itemView.findViewById(R.id.cl_item)
        val image: CircleImageView = itemView.findViewById(R.id.iv_itemImage)
        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)
        val cv_gi: CardView = itemView.findViewById(R.id.cv_gi)
        init {
            item.setOnClickListener(this)
            ivStar.visibility = View.GONE
        }
    }
}
