package myjin.pro.ahoora.myjin.activities.tools

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_bmi_result.*
import myjin.pro.ahoora.myjin.R
import java.text.DecimalFormat

class BmiResultActivity : AppCompatActivity() {

    private var result = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_result)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent != null) {
            try {
                result = intent.getFloatExtra("result", 0f)
                if (result == 0f) {
                    Toast.makeText(this, "خطایی پیش آمد دوباره امتحان کنید", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                finish()
            }
        }

        Log.e("result", result.toString())

        if (result <= 18.5) {
            circular_progress.progressColor = ContextCompat.getColor(this, R.color.c1)
            tv_low.textSize = 22f
        } else if (result > 18.5 && result <= 25.0) {
            circular_progress.progressColor = ContextCompat.getColor(this, R.color.colorPrimaryDark1)
            tv_good.textSize = 22f
        } else if (result > 25.0 && result <= 30.0) {
            circular_progress.progressColor = ContextCompat.getColor(this, R.color.colorAccent)
            tv_hight.textSize = 22f
        } else if (result > 30.0) {
            circular_progress.progressColor = ContextCompat.getColor(this, R.color.red3)
            tv_veryHeight.textSize = 22f
        }

        circular_progress.maxProgress = 35.0
        circular_progress.setCurrentProgress(result.toDouble())

        circular_progress.setProgressTextAdapter { d ->
            val decimalFormat = DecimalFormat("#.##")
            return@setProgressTextAdapter "BMI = ${decimalFormat.format(d)}"
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
