package myjin.pro.ahoora.myjin.adapters

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView

import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

import java.util.ArrayList
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.Sort
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinInternalMessegeModel
import myjin.pro.ahoora.myjin.utils.DateConverter

class InternalMessageAdapter(private val context: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val expansionsCollection = ExpansionLayoutCollection()
    private val buffer = ArrayList<KotlinInternalMessegeModel>()
    private var converter: DateConverter? = null
    val realm = Realm.getDefaultInstance()

    init {
        converter = DateConverter(context)
        expansionsCollection.openOnlyOne(true)
        fillBuffer()

    }

    private fun fillBuffer() {

        realm.beginTransaction()
        val res = realm.where(KotlinInternalMessegeModel::class.java)
                .findAll().sort("id", Sort.DESCENDING)
        realm.commitTransaction()
        buffer.addAll(res)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.international_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val bg = "#ff" + buffer[position].color_!!
        try {
            (holder as ViewHolder).tv_service_title.setTextColor(Color.parseColor(bg))
        } catch (e: Exception) {
            (holder as ViewHolder).tv_service_title.setTextColor(Color.WHITE)
        }

        (holder as ViewHolder).tv_service_title.text = buffer[position].title
        holder.tv_service_context.text = buffer[position].context

        holder.tv_dataReg.text = converter?.convert2(buffer[position].regDate)

        if (buffer[position].newRecord == "ok") {
            holder.ivTblBadge.visibility = View.VISIBLE
        } else {
            holder.ivTblBadge.visibility = View.INVISIBLE
        }


        expansionsCollection.add(holder.expansionLayout)
    }

    override fun getItemCount(): Int {
        return buffer.size
    }

    private fun share(pos: Int) {

        var str = "عنوان پیام : " + buffer[pos].title
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

    var y0 = 0f
    var y1 = 0f

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {


     /*       MotionEvent.ACTION_DOWN


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

        internal var tv_service_title: AppCompatTextView = itemView.findViewById(R.id.tv_service_title)
        internal var tv_service_context: AppCompatTextView = itemView.findViewById(R.id.tv_service_context)
        internal var expansionLayout: ExpansionLayout = itemView.findViewById(R.id.expansionLayout)
        internal var iv_share: AppCompatImageView = itemView.findViewById(R.id.iv_share)
        internal var tv_dataReg: AppCompatTextView = itemView.findViewById(R.id.tv_dataReg)
        internal var ivTblBadge: AppCompatImageView = itemView.findViewById(R.id.iv_tbl_badge)

        init {
            tv_service_context.setOnTouchListener(this)
            tv_service_context.movementMethod = ScrollingMovementMethod()
            iv_share.setOnClickListener(this)


            expansionLayout.addListener { expansionLayout, expanded ->

                if (expanded) {

                    if (buffer[adapterPosition].newRecord == "ok") {

                        ivTblBadge.visibility = View.INVISIBLE

                        realm.executeTransaction { db ->
                            buffer[adapterPosition].newRecord = "no"
                            val res = db.where(KotlinInternalMessegeModel::class.java)?.equalTo("id", buffer[adapterPosition].id)?.findFirst()
                            res?.newRecord = "no"
                        }
                    }

                }
            }
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {

                R.id.iv_share -> {
                    share(adapterPosition)
                }

            }
        }
    }


}
