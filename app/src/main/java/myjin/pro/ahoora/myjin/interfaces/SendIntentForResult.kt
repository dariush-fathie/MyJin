package myjin.pro.ahoora.myjin.interfaces

import android.content.Intent
import android.os.Bundle

interface SendIntentForResult {
    fun send(i: Intent, bundle: Bundle?, requestCode: Int)
}