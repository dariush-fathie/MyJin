package myjin.pro.ahoora.myjin.adapters

import android.content.Context
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
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel


class MessagesAdapter(private val context: Context, private val list: List<KotlinMessagesModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onlyForFirstTime = true

    val width = Converter.getScreenWidthPx(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageHolder(v)
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

    }


    internal inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            // todo : start message activity
        }

        val image: AppCompatImageView = itemView.findViewById(R.id.iv_messageImage)
        val title: AppCompatTextView = itemView.findViewById(R.id.tv_messageTitle)
        val shortDesc: AppCompatTextView = itemView.findViewById(R.id.tv_messageShortDesc)
        val date: AppCompatTextView = itemView.findViewById(R.id.tv_messageDate)
        val type: AppCompatTextView = itemView.findViewById(R.id.tv_messageType)

        init {
            itemView.setOnClickListener(this)
        }
    }

}

