package myjin.pro.ahoora.myjin.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_advice.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAdviceModel
import myjin.pro.ahoora.myjin.utils.DateConverter

class AdviceActivity : AppCompatActivity(), View.OnClickListener {
    var change = false
    private var isSaved = false
    private var Id: Int = 1
    private var converter: DateConverter? = null
    private val realm = Realm.getDefaultInstance()!!


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_save -> {
                change = true
                if (isSaved) {

                    val draw = ContextCompat.getDrawable(this@AdviceActivity, R.drawable.ic_bookmark_empty_msg)
                    draw?.setColorFilter(ContextCompat.getColor(this@AdviceActivity, R.color.white), PorterDuff.Mode.SRC_IN)

                    iv_save.setImageDrawable(draw)
                    deleteItem()
                    isSaved = false

                } else {
                    val draw = ContextCompat.getDrawable(this@AdviceActivity, R.drawable.ic_bookmark_fill_msg)
                    draw?.setColorFilter(ContextCompat.getColor(this@AdviceActivity, R.color.white), PorterDuff.Mode.SRC_IN)
                    iv_save.setImageDrawable(draw)
                    saveItem()
                    isSaved = true
                }

                animateBookmark(iv_save)
            }

            R.id.iv_share -> share()
            R.id.iv_copy -> copy()

            R.id.iv_goback -> onBackPressed()
        }
    }
    private fun share() {

        var str = "سوال : " + tv_title.text
        str += "\n\n"
        str += tv_content.text
        str += "\n\n"
        str += "ژین من (www.MyJin.ir) :"

        val shareIntent = Intent()

        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, str)

        startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    private fun copy() {

        var str = "سوال : " + tv_title.text
        str += "\n\n"
        str +=tv_content.text
        str += "\n\n"
        str += "ژین من (www.MyJin.ir) :"

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("متن کپی شد", str)
        clipboard.primaryClip = clip
    }
    private fun animateBookmark(view: ImageView) {
        val animation = AnimationSet(true)
        animation.addAnimation(AlphaAnimation(0.0f, 1.0f))
        animation.addAnimation(ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f))
        animation.duration = 400
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    private fun saveItem() {
        realm.executeTransaction { db ->
            val item = db.where(KotlinAdviceModel::class.java)
                    .equalTo("Id",Id)
                    .findFirst()!!
            item.saved = true
        }
    }

    private fun deleteItem() {
        realm.executeTransaction { db ->
            val item = db.where(KotlinAdviceModel::class.java)
                    .equalTo("Id",Id)
                    .findFirst()!!
            item.saved = false
        }
    }

    private fun checkItemIsSaved(): Boolean {
        var isSaved = false
        realm.executeTransaction { db ->
            val model = db.where(KotlinAdviceModel::class.java)
                    ?.equalTo("Id", Id)
                    ?.equalTo("saved", true)
                    ?.findAll()!!
            isSaved = model.count() > 0
        }
        return isSaved
    }

    @SuppressLint("SetTextI18n")
    private fun fill() {

        realm.executeTransaction { db ->
            val Items = db.where(KotlinAdviceModel::class.java).equalTo("Id", Id).findFirst()
            tv_title.text = Items?.title

            tv_content.text = Items?.context
            tv_time.text=converter?.convert2(Items?.regDate)


        }

        isSaved = checkItemIsSaved()
        if (isSaved) {
            val draw = ContextCompat.getDrawable(this@AdviceActivity, R.drawable.ic_bookmark_fill_msg)
            draw?.setColorFilter(ContextCompat.getColor(this@AdviceActivity, R.color.white), PorterDuff.Mode.SRC_IN)
            iv_save.setImageDrawable(draw)
        } else {
            val draw = ContextCompat.getDrawable(this@AdviceActivity, R.drawable.ic_bookmark_empty_msg)
            draw?.setColorFilter(ContextCompat.getColor(this@AdviceActivity, R.color.white), PorterDuff.Mode.SRC_IN)
            iv_save.setImageDrawable(draw)
        }
    }
    override fun onBackPressed() {
        if (change) {

            val resultPayload = Intent(this@AdviceActivity, MainActivity2::class.java)
           /* if (tf){
                resultPayload = Intent(this@AdviceActivity, FavMessageActivity::class.java)
            }*/
            resultPayload.putExtra("save", isSaved)
            resultPayload.putExtra("Id", Id)
            resultPayload.putExtra("position", position)
            setResult(AppCompatActivity.RESULT_OK, resultPayload)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private var position: Int = 0
    private var tf =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice)
        if (intent != null) {

            Id = intent.getIntExtra("Id", 1)
            position = intent.getIntExtra("position", 1)
           // tf=intent.getBooleanExtra("tf",false)
        }
        converter = DateConverter(this@AdviceActivity)
        fill()

        iv_save.setOnClickListener(this)
        iv_share.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
        iv_copy.setOnClickListener(this)
    }
}
