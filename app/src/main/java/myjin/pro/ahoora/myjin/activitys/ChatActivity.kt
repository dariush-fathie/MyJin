package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.CompoundButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_chat.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.ChatAdapter
import myjin.pro.ahoora.myjin.models.ChatModel


class ChatActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {



    private val dataSet = ArrayList<ChatModel>()
    private lateinit var adapter: ChatAdapter

    private var mode = true

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_send -> if (!et_chatbox.text?.isEmpty()!!) {
                val cm = ChatModel()
                cm.contextMsg = et_chatbox.text.toString()
                cm.modeMsg = mode


                dataSet.add(cm)
                adapter.notifyItemInserted(dataSet.size - 1)
                rv_chat.scrollToPosition(dataSet.size - 1)

                et_chatbox.text!!.clear()
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mode = isChecked
        if (mode) {
            rb_sendorreci.text = "راست"
        } else {
            rb_sendorreci.text = "چپ"
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val drawable = ContextCompat.getDrawable(this@ChatActivity, R.drawable.insured)
        tv_user.text = intent.getStringExtra("title")
        Glide.with(this@ChatActivity)
                .load(intent.getStringExtra("url"))
                .apply {
                    RequestOptions()
                            .fitCenter()
                            .placeholder(drawable)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                }
                .into(civMessageProfile)

        rv_chat.layoutManager = LinearLayoutManager(this@ChatActivity)
        adapter = ChatAdapter(this@ChatActivity, dataSet)
        rv_chat.adapter = adapter

        setListener()
    }

    private fun setListener() {
        iv_send.setOnClickListener(this)

        rb_sendorreci.setOnCheckedChangeListener(this)
    }
}
