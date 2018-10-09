package myjin.pro.ahoora.myjin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.DetailActivity
import myjin.pro.ahoora.myjin.activitys.NoDetailActivity
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.utils.SharedPer
import myjin.pro.ahoora.myjin.utils.StaticValues

class SearchAdapter(ctx: Context, data: List<KotlinItemModel>, g_url: String, g_name: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val dataSet = data
    private var g_url=g_url
    private var g_name=g_name
    private lateinit var shp: SharedPer
    private var active2 = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }



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


        var drawable = ContextCompat.getDrawable(context, R.drawable.ic_jin)
        var url = ""

        if (dataSet.get(position).logoImg.equals("")) {


            if (dataSet.get(position).gen?.equals("0")!!) {

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

        init {
            item.setOnClickListener(this)
            ivStar.visibility = View.GONE
        }
    }
}
