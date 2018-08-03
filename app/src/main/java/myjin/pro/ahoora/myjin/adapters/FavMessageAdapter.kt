package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.paad.audiobook.utils.Converter
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.DetailMessagesActivity
import myjin.pro.ahoora.myjin.activitys.MainActivity2
import myjin.pro.ahoora.myjin.interfaces.SendIntentForResult
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.utils.DateConverter


class FavMessageAdapter(private val context: Context, private val list: List<KotlinMessagesModel>, private val iSend: SendIntentForResult) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val width = Converter.getScreenWidthPx(context)
    private var converter: DateConverter? = null

    val requestCode = 1025

    init {
        converter = DateConverter(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageHolder(v)
    }


    fun deleteItem(id: Int) {
        /*val realm = Realm.getDefaultInstance()
        realm.executeTransaction { db ->
            val item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
            item.saved = false
        }*/
    }

    fun getModelByCenterId(id: Int): KotlinMessagesModel? {
        /*val realm = Realm.getDefaultInstance()
        var item = KotlinMessagesModel()
        realm.executeTransaction { db ->
            item = db.where(KotlinMessagesModel::class.java)
                    .equalTo("messageId", id)
                    .findFirst()!!
        }
        return item*/
        return null
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

    }


    internal inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.iv_save_message -> {

                }
                R.id.message_cl -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation((context as MainActivity2),
                                image, "transition_name")
                        val i = Intent(context, DetailMessagesActivity::class.java)
                        i.putExtra("messageId", list.get(adapterPosition).messageId)
                        i.putExtra("position", adapterPosition)
                        iSend.send(i, options.toBundle(), requestCode)
                    } else {
                        val i = Intent(context, DetailMessagesActivity::class.java)
                        i.putExtra("position", adapterPosition)
                        i.putExtra("messageId", list.get(adapterPosition).messageId)
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
        private val messageCl: ConstraintLayout = itemView.findViewById(R.id.message_cl)

        init {
            ivStar.setImageResource(R.drawable.ic_trash)
            ivStar.clearColorFilter()
            messageCl.setOnClickListener(this)
            ivStar.setOnClickListener(this)
        }
    }


}

