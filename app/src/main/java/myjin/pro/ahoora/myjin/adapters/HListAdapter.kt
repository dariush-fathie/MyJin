package myjin.pro.ahoora.myjin.adapters;

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel

class HListAdapter(ctx: Context, array: ArrayList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val context = ctx
    val idArray = array
    private var g_url = ""
    private var g_name = ""

    val realm: Realm = Realm.getDefaultInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.map_bottom_list_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return idArray.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        realm.beginTransaction()
        val item = realm.where(KotlinItemModel::class.java).equalTo("centerId", idArray.get(position)).findFirst()
        g_name = realm.where(KotlinGroupModel::class.java).equalTo("groupId", item?.groupId).findFirst()?.name!!
        g_url = realm.where(KotlinGroupModel::class.java).equalTo("groupId", item?.groupId).findFirst()?.g_url!!
        realm.commitTransaction()
        (holder as ItemHolder).title.text = item?.firstName + " " + item?.lastName



        var str = ""
        if (!item!!.gen.equals("0")!!) {
            if (item.groupId == 1) {
                str = item.levelList!![0]?.name + " _ " + item.specialityList!![0]?.name
            } else {
                str = g_name
            }

        } else {
            str = g_name
        }

        holder.subTitle.text = str

        var drawable = ContextCompat.getDrawable(context, R.drawable.ic_jin)
        var url = ""

        if (item.logoImg.equals("")) {
            if (item.gen?.equals("0")!!) {
                holder.image.setColorFilter(ContextCompat.getColor(context, R.color.green),android.graphics.PorterDuff.Mode.SRC_IN)
                url = g_url
            } else if (item.gen?.equals("1")!!) {
                holder.image.setColorFilter(null)
                url = context.getString(R.string.ic_doctor_f)
            } else if (item.gen?.equals("2")!!) {

                holder.image.setColorFilter(null)
                url = context.getString(R.string.ic_doctor_m)
            }

        } else {
            holder.image.setColorFilter(null)
            url = item.logoImg!!
        }

        Glide.with(context)
                .load(url)
                .apply {
                    RequestOptions()
                            .fitCenter()
                            .placeholder(drawable)
                }
                .into(holder.image)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: CircleImageView = itemView.findViewById(R.id.iv_bottomListImage)
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_bottomListTitle)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_bottomListSubTitle)
        override fun onClick(v: View?) {
            Toast.makeText(context, "$adapterPosition", Toast.LENGTH_SHORT).show()
        }

        init {


        }
    }

}