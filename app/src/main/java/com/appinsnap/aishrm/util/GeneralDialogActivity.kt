package com.appinsnap.aishrm.util

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener

open class GeneralDialogActivity:AppCompatActivity() {
    open fun showGeneralDialogActivity(
        context: Context,
        bottom:Boolean,
        value:String,
        click: onClick,
        location:locationPermission,
        setCancelAble: Boolean,
//        isCancel: Boolean,
        yourTitle: String,
        yourMessage: String,
        icon: Int
    ) {
        val dialog = Dialog(context)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        //   dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(setCancelAble)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.general_dialog_design)

        val okBtn = dialog.findViewById(R.id.ok) as? ConstraintLayout
        val alert = dialog.findViewById(R.id.txtalert) as TextView

        val message = dialog.findViewById(R.id.txtsuccess) as TextView
        val popupIcon = dialog.findViewById(R.id.imgPopIcon) as ImageView

        popupIcon.setImageResource(icon)

        message.text = yourMessage

        okBtn?.setSafeOnClickListener {
            if(bottom&&value=="yes"){
                click.onclick(true)
                dialog.dismiss()
            }
            else if(bottom)
            {
                location.onLocationPermission(true)
                dialog.dismiss()
            }
            else{
                dialog.dismiss()
            }

        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow()?.getAttributes())
        lp.width =    ActionBar.LayoutParams.MATCH_PARENT

        lp.height =   ActionBar.LayoutParams.MATCH_PARENT

        dialog.getWindow()?.setAttributes(lp)

        dialog.show()
    }
    interface onClick{
        fun onclick(value:Boolean)
    }
    interface locationPermission{
        fun onLocationPermission(locationvalue:Boolean)
           }

}