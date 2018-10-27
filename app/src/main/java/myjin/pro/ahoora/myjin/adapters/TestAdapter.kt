package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import myjin.pro.ahoora.myjin.R

class TestAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemHolder(LayoutInflater.from(context).inflate(R.layout.main_category_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }

    internal inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
