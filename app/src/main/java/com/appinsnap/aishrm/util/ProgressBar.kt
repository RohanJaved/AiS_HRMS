package com.appinsnap.aishrm.util

import android.content.Context
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog

fun Context.showProgressBar(progressBarDialogRegister: ProgressBarDialog?, isShow: Boolean) {
    if(isShow){
        progressBarDialogRegister!!.setMessage("Processing Your Request")
        progressBarDialogRegister.setCancelable(false)
        progressBarDialogRegister.show()

    }else{
        progressBarDialogRegister!!.dismiss()
    }
}