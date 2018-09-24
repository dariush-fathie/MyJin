package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.ChatAdapter

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        rv_chat.layoutManager = LinearLayoutManager(this@ChatActivity)
        rv_chat.adapter = ChatAdapter(this@ChatActivity)
    }
}
