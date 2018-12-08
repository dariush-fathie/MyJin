package myjin.pro.ahoora.myjin.adapters

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import myjin.pro.ahoora.myjin.utils.DateConverter


class AdviceAdapter(private val context: AppCompatActivity, data: List<KotlinAdviceModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filedStarDrawable: Drawable? = null
    private val expansionsCollection = ExpansionLayoutCollection()
    private val buffer = data
    private val realm = Realm.getDefaultInstance()
    private var converter: DateConverter? = null

    init {
        expansionsCollection.openOnlyOne(true)
        converter = DateConverter(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.advice_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).tv_advice_title.text = buffer[position].title
        holder.tv_advice_context.text = buffer[position].context

        holder.tv_dataReg.text = converter?.convert2(buffer[position].regDate)

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

        var str = "سوال : " + buffer[pos].title
        str += "\n\n"
        str += buffer[pos].context
        str += "\n\n"
        str += "ژین من (www.MyJin.ir) :"

        val shareIntent = Intent()

        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, str)

        context.startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    private fun copy(pos: Int) {

        var str = "سوال : " + buffer[pos].title
        str += "\n\n"
        str += buffer[pos].context
        str += "\n\n"
        str += "ژین من (www.MyJin.ir) :"

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("متن کپی شد", str)
        clipboard.primaryClip = clip
    }


    var y0 = 0f
    var y1 = 0f

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {


          /*  MotionEvent.ACTION_DOWN


            if (motionEvent?.action == MotionEvent.ACTION_MOVE) {
                y0 = motionEvent.y
                v!!
                if (y1 - y0 > 0) {
                    // scroll down4
                    val a = v.canScrollVertically(1)
                    Log.e("Y", "+ $a")
                    v.parent.requestDisallowInterceptTouchEvent(v.canScrollVertically(1))
                } else if (y1 - y0 < 0) {
                    // scroll up
                    val a = v.canScrollVertically(-1)
                    Log.e("Y", "- $a")
                    v.parent.requestDisallowInterceptTouchEvent(v.canScrollVertically(-1))
                }
                y1 = motionEvent.y
            }*/

            v?.parent?.requestDisallowInterceptTouchEvent(true)

            return false
        }

        internal var tv_advice_title: AppCompatTextView = itemView.findViewById(R.id.tv_advice_title)
        internal var tv_advice_context: AppCompatTextView = itemView.findViewById(R.id.tv_advice_context)
        internal var iv_share: AppCompatImageView = itemView.findViewById(R.id.iv_share)
        internal var iv_copy: AppCompatImageView = itemView.findViewById(R.id.iv_copy)
        internal var iv_bookmark: AppCompatImageView = itemView.findViewById(R.id.iv_bookmark)
        internal var tv_dataReg: AppCompatTextView = itemView.findViewById(R.id.tv_dataReg)
        internal var expansionLayout: ExpansionLayout = itemView.findViewById(R.id.expansionLayout)

        init {
            iv_copy.setOnClickListener(this)
            iv_share.setOnClickListener(this)
            iv_bookmark.setOnClickListener(this)

            tv_advice_context.setOnTouchListener(this)
            tv_advice_context.movementMethod = ScrollingMovementMethod()
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
                    val b = buffer[adapterPosition].saved
                    res?.saved = !b
                    buffer[adapterPosition].saved = !b

                    bookmark(iv_bookmark, adapterPosition)
                    realm.commitTransaction()


                }
            }
        }
    }


}
