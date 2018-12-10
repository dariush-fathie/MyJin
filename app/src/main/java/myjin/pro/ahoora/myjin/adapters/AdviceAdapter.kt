package myjin.pro.ahoora.myjin.adapters


import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activities.AdviceActivity
import myjin.pro.ahoora.myjin.interfaces.SendIntentForResult
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import myjin.pro.ahoora.myjin.utils.DateConverter


class AdviceAdapter(private val context: AppCompatActivity, data: List<KotlinAdviceModel>,private val iSend: SendIntentForResult) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filedStarDrawable: Drawable? = null

    private val buffer = data
    private val realm = Realm.getDefaultInstance()
    private var converter: DateConverter? = null
    val requestCode = 1027
    lateinit var markedItem: BooleanArray
    init {

        realm.executeTransaction { db ->
            val savedItems = db.where(KotlinAdviceModel::class.java).equalTo("saved", true).findAll()
            markedItem = BooleanArray(buffer.size)
            val s = ArrayList<Int>() // to hold saved item centerId
            savedItems.forEach { model: KotlinAdviceModel? ->
                val a = model?.Id
                s.add(a!!)
            }

            for (i in 0 until buffer.size) {
                val f = s.contains(buffer[i].Id)
                markedItem[i] = f // if item is saved put true in markedItem else put false
            }
        }
        realm.close()

        converter = DateConverter(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.advice_item_layout2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).tv_advice_title.text = buffer[position].title
        holder.tv_dataReg.text = converter?.convert2(buffer[position].regDate)

        bookmark(holder.iv_bookmark, position)
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
    fun mark(position: Int, mark: Boolean) {
        markedItem[position] = mark
        notifyItemChanged(position)
    }

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        internal var tv_advice_title: AppCompatTextView = itemView.findViewById(R.id.tv_advice_title)
        internal var iv_bookmark: AppCompatImageView = itemView.findViewById(R.id.iv_bookmark)
        internal var tv_dataReg: AppCompatTextView = itemView.findViewById(R.id.tv_dataReg)
        internal var ll_mainItem: LinearLayout = itemView.findViewById(R.id.ll_mainitem)

        init {
            iv_bookmark.setOnClickListener(this)
            ll_mainItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.iv_bookmark -> {
                    realm.beginTransaction()
                    val res = realm.where(KotlinAdviceModel::class.java).equalTo("Id", buffer[adapterPosition].Id).findFirst()
                    val b = buffer[adapterPosition].saved
                    res?.saved = !b
                    buffer[adapterPosition].saved = !b
                    markedItem[adapterPosition] = !b
                    bookmark(iv_bookmark, adapterPosition)
                    realm.commitTransaction()


                }
                R.id.ll_mainitem->{
                    val i = Intent(context, AdviceActivity::class.java)
                    i.putExtra("position", adapterPosition)
                    i.putExtra("Id", buffer.get(adapterPosition).Id)
                    iSend.send(i, null, requestCode)
                }
            }
        }
    }


}
