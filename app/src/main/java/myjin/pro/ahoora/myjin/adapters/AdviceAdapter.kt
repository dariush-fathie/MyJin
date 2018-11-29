package myjin.pro.ahoora.myjin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

import java.util.ArrayList
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel



class AdviceAdapter(private val context: Activity ,private val buffer : ArrayList<KotlinAdviceModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filedStarDrawable: Drawable? = null
    private val expansionsCollection = ExpansionLayoutCollection()

    private val realm = Realm.getDefaultInstance()

    init {
        expansionsCollection.openOnlyOne(true)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.advice_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ViewHolder).tv_advice_title.text = buffer[position].title
        holder.tv_advice_context.text = buffer[position].context

        bookmark(holder.iv_bookmark, position)

        expansionsCollection.add(holder.expansionLayout)
    }

    override fun getItemCount(): Int {
        return buffer.size
    }

    private fun bookmark(iv_bookmark: AppCompatImageView, position: Int) {
        if (buffer[position].saved) {
            filedStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill_msg)
            filedStarDrawable!!.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN)
            iv_bookmark.setImageDrawable(filedStarDrawable)

        } else {
            filedStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark_empty_msg)
            filedStarDrawable!!.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN)
            iv_bookmark.setImageDrawable(filedStarDrawable)
        }
    }

    private fun share(pos: Int) {
        var str = buffer[pos].title
        str += "\n"
        str += buffer[pos].context
        str += "\n\n"
        val shareIntent = Intent()

        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, str)

        context.startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    private fun copy(pos: Int) {
        var str = buffer[pos].title
        str += "\n"
        str += buffer[pos].context
        str += "\n\n"

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("متن کپی شد", str)
        clipboard.primaryClip = clip
    }


    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var tv_advice_title: AppCompatTextView = itemView.findViewById(R.id.tv_advice_title)
        internal var tv_advice_context: AppCompatTextView = itemView.findViewById(R.id.tv_advice_context)
        internal var iv_share: AppCompatImageView = itemView.findViewById(R.id.iv_share)
        internal var iv_copy: AppCompatImageView = itemView.findViewById(R.id.iv_copy)
        internal var iv_bookmark: AppCompatImageView = itemView.findViewById(R.id.iv_bookmark)
        internal var expansionLayout: ExpansionLayout = itemView.findViewById(R.id.expansionLayout)

        init {
            iv_copy.setOnClickListener(this)
            iv_share.setOnClickListener(this)
            iv_bookmark.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.iv_copy -> {
                    copy(adapterPosition)
                }
                R.id.iv_share -> {
                    share(adapterPosition)
                }
                R.id.iv_bookmark -> {
                    realm.beginTransaction()
                    val res = realm.where(KotlinAdviceModel::class.java).equalTo("Id", buffer[adapterPosition].Id).findFirst()
                    realm.commitTransaction()
                    val b=buffer[adapterPosition].saved
                    res?.saved=!b
                    buffer[adapterPosition].saved=!b

                    bookmark(iv_bookmark,adapterPosition)

                }
            }
        }
    }


}
