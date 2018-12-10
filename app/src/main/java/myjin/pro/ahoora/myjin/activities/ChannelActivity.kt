package myjin.pro.ahoora.myjin.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_channel.*
import myjin.pro.ahoora.myjin.R

class ChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_channel)
        val drawable = ContextCompat.getDrawable(this@ChannelActivity, R.drawable.insured)
        tv_user.text = intent.getStringExtra("title")
        Glide.with(this@ChannelActivity)
                .load(intent.getStringExtra("url"))
                .apply {
                    RequestOptions()
                            .fitCenter()
                            .placeholder(drawable)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                }.into(civMessageProfile)
    }
}
