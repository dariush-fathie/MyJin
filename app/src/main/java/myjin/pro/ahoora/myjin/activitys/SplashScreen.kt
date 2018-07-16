package myjin.pro.ahoora.myjin.activitys

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import myjin.pro.ahoora.myjin.R


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Clear_GlideCaches()

        Handler().postDelayed({ finish() }, 2500)


    }

    override fun onStart() {
        super.onStart()
    }

    fun Clear_GlideCaches() {
        Handler().postDelayed(Runnable { Glide.get(this@SplashScreen).clearMemory() }, 0)
        AsyncTask.execute(Runnable { Glide.get(this@SplashScreen).clearDiskCache() })
    }


}
