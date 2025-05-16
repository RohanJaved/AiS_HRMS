package com.appinsnap.aishrm.common

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener


fun Context.getAppVersionName(): String? {
    val packageName = this.packageName
    val packageManager = this.packageManager

    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return null
}
 fun View.show(){
    visibility = View.VISIBLE
}

 fun View.hide() {
    visibility = View.GONE
}

fun Context.showDialogWithSingleClicked(
    click: DialogClicked,
    setCancelAble: Boolean = false,
    yourTitle: String,
    yourMessage: String,
    okBtnText:String = "Ok",
    icon: Int
) {

    val dialog = Dialog(this)
    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    //   dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(setCancelAble)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.general_dialog_design)
    val okBtn = dialog.findViewById(R.id.ok) as? ConstraintLayout
    val alert = dialog.findViewById(R.id.txtalert) as TextView
    val message = dialog.findViewById(R.id.txtsuccess) as TextView
    val popIcon = dialog.findViewById(R.id.imgPopIcon) as ImageView
    val buttonTxt = dialog.findViewById<TextView>(R.id.checkout)
    popIcon.setImageResource(icon)
    buttonTxt.text = okBtnText
    message.text = yourMessage
    okBtn?.setSafeOnClickListener {
        dialog.dismiss()
        click.onclick()
    }
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(dialog.getWindow()?.getAttributes())
    lp.width =    ActionBar.LayoutParams.MATCH_PARENT

    lp.height =   ActionBar.LayoutParams.MATCH_PARENT

    dialog.getWindow()?.setAttributes(lp)
    dialog.show()
}

interface DialogClicked{
    fun onclick()

}