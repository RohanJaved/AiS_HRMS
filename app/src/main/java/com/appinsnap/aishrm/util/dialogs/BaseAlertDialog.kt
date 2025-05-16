package com.appinsnap.aishrm.util.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import javax.inject.Inject

@SuppressLint("HandlerLeak")
open class BaseAlertDialog @Inject constructor(context: Context) : AlertDialog(context) {
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false
        }
        return true
    }

    fun setButton(
        whichButton: Int, text: Int,
        listener: DialogInterface.OnClickListener?
    ) {
        super.setButton(whichButton, context.getString(text), listener)
    }

    fun setButton(whichButton: Int, text: String?, listener: DialogInterface.OnClickListener?) {
        super.setButton(whichButton, text, listener)
    }

    override fun show() {
        try {
            super.show()
            getButton(BUTTON_POSITIVE).textSize = 20f
            getButton(BUTTON_NEGATIVE).textSize = 20f
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        super.dismiss()
    }
}