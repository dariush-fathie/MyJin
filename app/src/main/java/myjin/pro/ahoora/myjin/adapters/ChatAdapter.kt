package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import myjin.pro.ahoora.myjin.R
import java.util.ArrayList

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var list = ArrayList<String>()

    override fun getItemViewType(position: Int): Int {

        var type = 0

        if (position % 2 != 0) {
            type = 1
        }
        return type
    }

    init {
        list.add("سلام آقای دکتر")
        list.add("سلام شما؟")
        list.add("افشین رادمنش هستم\n" +
                "چند روزه درد شدیدی در معده دارم " +
                " خواستم ازتون راهنمایی بگیرم")
        list.add("نگران نباشید\n" +
                " بعد از اندوسکوپی تا چند روز طبیعی هست" +
                " اگر درد ادامه داشت به مطب مراجعه کنید")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (viewType != 0) {
            val v = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false)
            return SendHolder(v)
        } else {
            val v = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false)
            return ReceivedHolder(v)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReceivedHolder) {
            holder.text_message_bodyr.text = list.get(position)
        } else {
            ( holder as SendHolder).text_message_bodys.text = list.get(position)
        }


    }

    class ReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  var text_message_bodyr: AppCompatTextView = itemView.findViewById(R.id.text_message_body)

    }

    class SendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_message_bodys: AppCompatTextView = itemView.findViewById(R.id.text_message_body)
    }
}