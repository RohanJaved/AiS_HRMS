package com.appinsnap.aishrm.util

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.BaseFragment
import com.appinsnap.aishrm.ui.activities.login.LoginActivity
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener

open class GeneralDialogFragment : BaseFragment(){

    open fun showGeneralDialogFragment(
        context: Context,
        bottom:Boolean,
        click: onClick,
        setCancelAble: Boolean,
        value:String,
//        isCancel: Boolean,
        location: locationPermission,
        yourTitle: String,
        yourMessage: String,
        icon: Int,
        code:String=""

    ) {

        val dialog = Dialog(context)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        //   dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(setCancelAble)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.general_dialog_design)

        val okBtn = dialog.findViewById(R.id.ok) as? ConstraintLayout
        val alert = dialog.findViewById(R.id.txtalert) as TextView
//        val cancelBtn = dialog.findViewById(R.id.btnCancel) as? TextView
//        val okSingleBtn = dialog.findViewById(R.id.okSingleButton) as? TextView
       // val imageView = dialog.findViewById(R.id.dialogimg) as? ImageView
//        val pairButton = dialog.findViewById(R.id.pairButtons) as? ConstraintLayout

       // val title = dialog.findViewById(R.id.title) as TextView
        val message = dialog.findViewById(R.id.txtsuccess) as TextView
        val popIcon = dialog.findViewById(R.id.imgPopIcon) as ImageView

        popIcon.setImageResource(icon)

        // for single and pair button.
//        if (isCancel) {
//            pairButton?.isVisible = true
//            okSingleBtn?.isVisible = false
//        } else {
//            pairButton?.isVisible = false
//            okSingleBtn?.isVisible = true
//        }
        // for image and title
//        if (yourTitle.isEmpty()){
//            imageView?.isVisible = true
//           // title.isVisible = false
//            imageView?.setImageDrawable(ContextCompat.getDrawable(context, newImageView))
//        }else{
//            imageView?.isVisible = false
//            //title.isVisible = true
//           // title.text = yourTitle
//        }

        message.text = yourMessage

        // for pair button
//        if (isCancel){
//            okBtn?.setSafeOnClickListener {
//                getPair?.getOkValue(true)
//                dialog.dismiss()
//            }
//
//        }else{
//            okBtn?.setSafeOnClickListener {
//                dialog.dismiss()
//            }
        //   }

        okBtn?.setSafeOnClickListener {


            if (code=="203"){
                dialog.dismiss()
                SessionManagement(context).clearPreference()
                startActivity(Intent(context,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

                return@setSafeOnClickListener
            }


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

        // for single button
//        if (isOtherOK){
//            okSingleBtn?.setSafeOnClickListener {
//                getOk?.getOkValue(true)
//                dialog.dismiss()
//            }
//        }else{
//            okSingleBtn?.setSafeOnClickListener {
//                dialog.dismiss()
//            }
//        }

        dialog.show()
    }open fun showGeneralDialogFragment(
        context: Context,
        bottom:Boolean,
        click: onClick,
        setCancelAble: Boolean,
        value:String,
//        isCancel: Boolean,
        yourTitle: String,
        yourMessage: String,
        icon: Int,
        code:String=""

    ) {

        val dialog = Dialog(context)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        //   dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(setCancelAble)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.general_dialog_design)

        val okBtn = dialog.findViewById(R.id.ok) as? ConstraintLayout
        val alert = dialog.findViewById(R.id.txtalert) as TextView
//        val cancelBtn = dialog.findViewById(R.id.btnCancel) as? TextView
//        val okSingleBtn = dialog.findViewById(R.id.okSingleButton) as? TextView
       // val imageView = dialog.findViewById(R.id.dialogimg) as? ImageView
//        val pairButton = dialog.findViewById(R.id.pairButtons) as? ConstraintLayout

       // val title = dialog.findViewById(R.id.title) as TextView
        val message = dialog.findViewById(R.id.txtsuccess) as TextView
        val popIcon = dialog.findViewById(R.id.imgPopIcon) as ImageView

        popIcon.setImageResource(icon)

        // for single and pair button.
//        if (isCancel) {
//            pairButton?.isVisible = true
//            okSingleBtn?.isVisible = false
//        } else {
//            pairButton?.isVisible = false
//            okSingleBtn?.isVisible = true
//        }
        // for image and title
//        if (yourTitle.isEmpty()){
//            imageView?.isVisible = true
//           // title.isVisible = false
//            imageView?.setImageDrawable(ContextCompat.getDrawable(context, newImageView))
//        }else{
//            imageView?.isVisible = false
//            //title.isVisible = true
//           // title.text = yourTitle
//        }

        message.text = yourMessage

        // for pair button
//        if (isCancel){
//            okBtn?.setSafeOnClickListener {
//                getPair?.getOkValue(true)
//                dialog.dismiss()
//            }
//
//        }else{
//            okBtn?.setSafeOnClickListener {
//                dialog.dismiss()
//            }
        //   }

        okBtn?.setSafeOnClickListener {


            if (code=="203"){
                dialog.dismiss()
                SessionManagement(context).clearPreference()
                startActivity(Intent(context,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))

                return@setSafeOnClickListener
            }


            if(bottom&&value=="yes"){
                click.onclick(true)
                dialog.dismiss()
            }
            else if(bottom)
            {

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

        // for single button
//        if (isOtherOK){
//            okSingleBtn?.setSafeOnClickListener {
//                getOk?.getOkValue(true)
//                dialog.dismiss()
//            }
//        }else{
//            okSingleBtn?.setSafeOnClickListener {
//                dialog.dismiss()
//            }
//        }

        dialog.show()
    }
    interface onClick{
        fun onclick(value:Boolean)

    }
    interface locationPermission{
        fun onLocationPermission(locationvalue:Boolean)
    }



}