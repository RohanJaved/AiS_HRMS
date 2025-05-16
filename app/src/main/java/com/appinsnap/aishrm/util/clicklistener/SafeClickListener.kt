package com.appinsnap.aishrm.util.clicklistener

import android.os.SystemClock
import android.view.View
import com.appinsnap.aishrm.util.Constants

class SafeClickListener(

    private var defaultInterval: Int = 200,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - Constants.lastTimeClicked < defaultInterval) {
            return
        }
        Constants.lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}
