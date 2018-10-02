package myjin.pro.ahoora.myjin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityOptionsCompat
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
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.DetailMessagesActivity
import myjin.pro.ahoora.myjin.activitys.MainActivity2
import myjin.pro.ahoora.myjin.interfaces.SendIntentForResult
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.utils.Converter
import myjin.pro.ahoora.myjin.utils.DateConverter


class MessagesAdapter(private val context: Context, private val list: List<KotlinMessagesModel>, private val iSend: SendIntentForResult) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onlyForFirstTime = true
    lateinit var markedItem: BooleanArray
    val width = Converter.getScreenWidthPx(context)
    private var filledStarDrawable: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill_msg)!!
    private var converter: DateConverter? = null


    val requestCode = 1025

    init {
        filledStarDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val savedItems = db.where(KotlinMessagesModel::class.java).equalTo("saved", true).findAll()
            markedItem = BooleanArray(list.size)
            val s = ArrayList<Int>() // to hold saved item centerId
            savedItems.forEach { model: KotlinMessagesModel? ->
                val a = model?.messageId
                s.add(a!!)
            }

            for (i in 0 until list.size) {
                val f = s.contains(list[i].messageId)
                markedItem[i] = f // if item is saved put true in markedItem else put false
            }
        }
        realm.close()
        converter = DateConverter(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageHolder(v)
    }

    fun saveItem(Id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", Id)
                    .findFirst()!!
            item.saved = true
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

    fun deleteItem(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
            item.saved = false
        }
    }

    fun getModelByCenterId(id: Int): KotlinMessagesModel {
        val realm = Realm.getDefaultInstance()
        var item = KotlinMessagesModel()
        realm.executeTransaction { db ->
            item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
        }
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
        holder.date.text = converter?.convert2(messageItem.regDate)
        holder.type.text = messageItem.type
        holder.source.text = messageItem.groupName

        if (markedItem[position]) {
            holder.ivStar.setImageDrawable(filledStarDrawable)
        } else {
            holder.ivStar.setImageResource(R.drawable.ic_bookmark_empty_msg)
        }
        val bg = "#ff" + messageItem.bgColor
        try {
            holder.message_cl.setBackgroundColor(Color.parseColor(bg))
        }catch (e:Exception){
            holder.message_cl.setBackgroundColor(Color.WHITE)
        }

    }

    fun mark(position: Int, mark: Boolean) {
        markedItem[position] = mark
        notifyItemChanged(position)
    }

    internal inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @SuppressLint("RestrictedApi")
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.iv_save_message -> {
                    val item = getModelByCenterId(list.get(adapterPosition).messageId)
                    if (item.saved) {
                        deleteItem(item.messageId) // set saved flag to false
                        ivStar.setImageResource(R.drawable.ic_bookmark_empty_msg)
                        markedItem[adapterPosition] = false
                    } else {
                        saveItem(item.messageId) // set save flag to ture
                        ivStar.setImageDrawable(filledStarDrawable)
                        markedItem[adapterPosition] = true
                    }
                    animateBookmark(ivStar)
                }
                R.id.message_cl -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation((context as MainActivity2),
                                image, "transition_name")
                        val i = Intent(context, DetailMessagesActivity::class.java)
                        i.putExtra("messageId", list.get(adapterPosition).messageId)
                        i.putExtra("position", adapterPosition)
                        i.putExtra("tf", false)
                        iSend.send(i, options.toBundle(), requestCode)
                    } else {
                        val i = Intent(context, DetailMessagesActivity::class.java)
                        i.putExtra("position", adapterPosition)
                        i.putExtra("messageId", list.get(adapterPosition).messageId)
                        i.putExtra("tf", false)
                        iSend.send(i, null, requestCode)
                    }
                }
            }
        }

        val ivStar: AppCompatImageView = itemView.findViewById(R.id.iv_save_message)
        val image: AppCompatImageView = itemView.findViewById(R.id.iv_messageImage)
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_messageTitle)
        val date: AppCompatTextView = itemView.findViewById(R.id.tv_messageDate)
        val type: AppCompatTextView = itemView.findViewById(R.id.tv_messageType)
        val source: AppCompatTextView = itemView.findViewById(R.id.tv_messageSource)
        val message_cl: ConstraintLayout = itemView.findViewById(R.id.message_cl)

        init {
            message_cl.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }


}

