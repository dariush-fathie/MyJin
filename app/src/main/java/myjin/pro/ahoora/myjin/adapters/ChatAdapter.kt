package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.library.bubbleview.BubbleTextView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.ChatModel
import java.util.*

class ChatAdapter(private val context: Context, dataSet: ArrayList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mDataSet = dataSet
    override fun getItemViewType(position: Int): Int {
        var type = 0
        if (!mDataSet[position].modeMsg) {
            type = 1
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            val v = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false)
            return SendHolder(v)
        } else {
            val v = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false)
            return ReceivedHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReceivedHolder) {
            holder.bubbleMessageBody.text = mDataSet[position].contextMsg
        } else {
            (holder as SendHolder).bubbleMessageBody.text = mDataSet[position].contextMsg
        }
    }

    class ReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bubbleMessageBody: BubbleTextView = itemView.findViewById(R.id.bv_message_body)
    }

    class SendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bubbleMessageBody: BubbleTextView = itemView.findViewById(R.id.bv_message_body)
    }
}