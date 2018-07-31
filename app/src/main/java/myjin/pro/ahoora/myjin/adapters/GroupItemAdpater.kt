package myjin.pro.ahoora.myjin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.DetailActivity
import myjin.pro.ahoora.myjin.activitys.OfficeActivity
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.utils.StaticValues
import android.support.v4.app.ActivityOptionsCompat
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import de.hdodenhof.circleimageview.CircleImageView


class GroupItemAdapter(ctx: Context, idList: ArrayList<Int>, g_url: String, title: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = ctx
    private val ids = idList
    private val g_url = g_url
    private val title = title
    private var realm: Realm = Realm.getDefaultInstance()
    lateinit var markedItem: BooleanArray
    var filedStarDrawable: Drawable

    private var int = 0

    init {
        Clear_GlideCaches()

        realm.executeTransaction { db ->
            val savedItems = db.where(KotlinItemModel::class.java).equalTo("saved", true).findAll()
            markedItem = BooleanArray(ids.size)
            val s = ArrayList<Int>() // to hold saved item centerId
            savedItems.forEach { model: KotlinItemModel? ->
                s.add(model?.centerId!!)
            }

            for (i in 0 until ids.size) {
                markedItem[i] = s.contains(ids[i]) // if item is saved put true in markedItem else put false
            }

        }

        filedStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark)!!
        filedStarDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        return ItemHolder(v)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            holder as ItemHolder
            val item = getModelByCenterId(ids[position])
            // todo use placeholders
            holder.title.text = "${item.firstName} ${item?.lastName}"

            var str = "";
            if (!item.gen.equals("0")!!) {
                if (item.groupId == 1) {
                    str = item.levelList!![0]?.name + " _ " + item.specialityList!![0]?.name
                } else {
                    str = title
                }

            } else {
                str = title
            }

            holder.subTitle.text = str
            holder.tv_addr.text = item.addressList!![0]?.locTitle


            var drawable = ContextCompat.getDrawable(context, R.drawable.ic_jin)
            var url = ""

            if (item.logoImg.equals("")) {


                // holder.image.setColorFilter(ContextCompat.getColor(context, R.color.logoColor), android.graphics.PorterDuff.Mode.SRC_IN)

                if (item.gen?.equals("0")!!) {
                    holder.image.background=ContextCompat.getDrawable(context,R.drawable.t2)
                    url = g_url
                } else if (item.gen?.equals("1")!!) {
                    holder.image.background=ContextCompat.getDrawable(context,R.drawable.t)
                    url = context.getString(R.string.ic_doctor_f)
                } else if (item.gen?.equals("2")!!) {
                    holder.image.background=ContextCompat.getDrawable(context,R.drawable.t)
                    url = context.getString(R.string.ic_doctor_m)
                }

            } else {
                holder.image.background=ContextCompat.getDrawable(context,R.drawable.t)
                url = item.logoImg!!
            }

            Glide.with(context)
                    .load(url)
                    .apply {
                        RequestOptions()
                                .placeholder(drawable)
                    }
                    .into(holder.image)

            if (markedItem[position]) {
                holder.ivStar.setImageDrawable(filedStarDrawable)
            } else {
                holder.ivStar.setImageResource(R.drawable.icons_bookmark_1)
            }


        } catch (e: Exception) {
            Log.e("GroupAdapter", e.message + " ")
        }
    }

    fun Clear_GlideCaches() {
        Handler().postDelayed(Runnable { Glide.get(context).clearMemory() }, 0)

        AsyncTask.execute(Runnable { Glide.get(context).clearDiskCache() })
    }

    override fun getItemCount(): Int {
        val s = ids.size
        return if (s == 0) {
            0
        } else {
            s
        }
    }


    fun getModelByCenterId(centerId: Int): KotlinItemModel {
        var item = KotlinItemModel()
        realm.executeTransaction({ db ->
            item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
        })
        return item
    }

    fun saveItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = true
        })
    }

    fun deleteItem(centerId: Int) {
        realm.executeTransaction { db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = false
        }
    }


    fun onItemMarkedEvent(centerId: Int, savedOrDelete: Boolean) {
        if (clickedItemPosition != -1 && centerId == ids[clickedItemPosition]) {
            markedItem[clickedItemPosition] = savedOrDelete
            notifyItemChanged(clickedItemPosition)
        }
    }


    fun animateBookmark(view: ImageView) {
        val animation = AnimationSet(true)
        animation.addAnimation(AlphaAnimation(0.0f, 1.0f))
        animation.addAnimation(ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f))
        animation.duration = 400
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    var clickedItemPosition = -1

    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        @SuppressLint("RestrictedApi")
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.rl_item -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation((context as OfficeActivity),
                                image, "transition_name")

                        val i = Intent(context, DetailActivity::class.java)
                        i.putExtra(StaticValues.MODEL, 0)
                        i.putExtra(StaticValues.ID, ids.get(adapterPosition))
                        i.putExtra("g_url", g_url)
                        clickedItemPosition = adapterPosition
                        (context as OfficeActivity).startActivityForResult(i, StaticValues.requestCodeOfficeDetail, options.toBundle())


                    } else {
                        val i = Intent(context, DetailActivity::class.java)
                        i.putExtra(StaticValues.MODEL, 0)
                        i.putExtra(StaticValues.ID, ids.get(adapterPosition))
                        i.putExtra("g_url", g_url)
                        clickedItemPosition = adapterPosition
                        (context as OfficeActivity).startActivityForResult(i, StaticValues.requestCodeOfficeDetail)

                    }

                    return
                }
                R.id.iv_starLike -> {
                    val item = getModelByCenterId(ids.get(adapterPosition))
                    if (item.saved) {
                        deleteItem(item.centerId) // set saved flag to false
                        ivStar.setImageResource(R.drawable.icons_bookmark_1)
                        markedItem[adapterPosition] = false
                    } else {
                        saveItem(item.centerId) // set save flag to ture
                        ivStar.setImageDrawable(filedStarDrawable)
                        markedItem[adapterPosition] = true
                    }
                    animateBookmark(ivStar)
                }
            }

        }

        val title: AppCompatTextView = itemView.findViewById(R.id.tv_title)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_subTitle)
        val tv_addr: AppCompatTextView = itemView.findViewById(R.id.tv_addr)
        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_starLike)
        val item: RelativeLayout = itemView.findViewById(R.id.rl_item)
        val image: CircleImageView = itemView.findViewById(R.id.iv_itemImage)

        init {
            item.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }


}
