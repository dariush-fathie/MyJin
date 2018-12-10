package myjin.pro.ahoora.myjin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.formatVideoPopupModel

class FormatVideoPopupAdapter(private val items: List<formatVideoPopupModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): formatVideoPopupModel {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView1: View?, parent: ViewGroup): View {
        var convertView = convertView1
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_format_video_popup, null)
            holder = ViewHolder(convertView!!)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.tvTitle.text = getItem(position).title
        holder.ivImage.setImageResource(getItem(position).imageRes)
        return convertView
    }

    internal class ViewHolder(view: View) {
        var tvTitle: AppCompatTextView = view.findViewById(R.id.text)
        var ivImage: AppCompatImageView = view.findViewById(R.id.image)

    }
}
