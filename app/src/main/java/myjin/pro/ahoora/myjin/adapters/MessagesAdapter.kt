package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.realm.Realm
import ir.paad.audiobook.utils.Converter
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel


class MessagesAdapter(private val context: Context, private val list: List<KotlinMessagesModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onlyForFirstTime = true
    lateinit var markedItem: BooleanArray
    val width = Converter.getScreenWidthPx(context)
    var filedStarDrawable: Drawable

    init {
        filedStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark)!!
        filedStarDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val savedItems = db.where(KotlinMessagesModel::class.java).equalTo("saved", true).findAll()
            markedItem = BooleanArray(list.size)
            val s = ArrayList<Int>() // to hold saved item centerId
            savedItems.forEach { model: KotlinMessagesModel? ->
                s.add(model?.messageId!!)
            }

            for (i in 0 until list.size) {
                markedItem[i] = s.contains(list[i].messageId) // if item is saved put true in markedItem else put false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageHolder(v)
    }

    fun saveItem(Id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction({ db ->
            val item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", Id)
                    .findFirst()!!
            item.saved = true
        })
    }

    fun animateBookmark(view: ImageView) {
        val animation = AnimationSet(true)
        animation.addAnimation(AlphaAnimation(0.0f, 1.0f))
        animation.addAnimation(ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f))
        animation.duration = 400
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    fun deleteItem(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction({ db ->
            val item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
            item.saved = false
        })
    }

    fun getModelByCenterId(id: Int): KotlinMessagesModel {
        val realm = Realm.getDefaultInstance()
        var item = KotlinMessagesModel()
        realm.executeTransaction({ db ->
            item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
        })
        return item
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as MessageHolder
        try {
            Glide.with(context)
                    .load(list[position].imageUrl)
                    .apply(RequestOptions()
                            .fitCenter()
                            .placeholder(R.color.colorAccent))
                    .into(holder.image)
        } catch (e: Exception) {
            Log.e("glideErr", e.message + " ")
        }

        val messageItem = list[position]

        holder.title.text = messageItem.title
        holder.shortDesc.text = messageItem.shortDescription
        holder.date.text = messageItem.regDate
        holder.type.text = messageItem.type

        if (markedItem[position]) {
            holder.ivStar.setImageDrawable(filedStarDrawable)
        } else {
            holder.ivStar.setImageResource(R.drawable.icons_bookmark_1)
        }

    }


    internal inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.iv_save_message -> {
                    val item = getModelByCenterId(list.get(adapterPosition).messageId)
                    if (item.saved) {
                        deleteItem(item.messageId) // set saved flag to false
                        ivStar.setImageResource(R.drawable.icons_bookmark_1)
                        markedItem[adapterPosition] = false
                    } else {
                        saveItem(item.messageId) // set save flag to ture
                        ivStar.setImageDrawable(filedStarDrawable)
                        markedItem[adapterPosition] = true
                    }
                    animateBookmark(ivStar)
                }
            }
        }

        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_save_message)
        val image: AppCompatImageView = itemView.findViewById(R.id.iv_messageImage)
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_messageTitle)
        val shortDesc: AppCompatTextView = itemView.findViewById(R.id.tv_messageShortDesc)
        val date: AppCompatTextView = itemView.findViewById(R.id.tv_messageDate)
        val type: AppCompatTextView = itemView.findViewById(R.id.tv_messageType)

        init {
            itemView.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }

}

