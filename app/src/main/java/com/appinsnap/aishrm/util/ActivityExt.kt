package com.appinsnap.aishrm.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.google.android.material.snackbar.Snackbar





fun Activity.hideKeyboard(editText: EditText){
     editText.clearFocus()
     val inputMethodManager: InputMethodManager? =
          getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
     inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
}
fun View.showSnackbar( messageRes: String, length: Int = Snackbar.LENGTH_LONG) {
     val snackBar = Snackbar.make(this, messageRes, length)
     snackBar.show()
     snackBar.apply{
          this.view.setBackgroundColor(Color.parseColor("#ff9200"))
          this.setTextColor(Color.parseColor("#ffffff"))
          this.show()
     }
}

fun showProgressDialog(isShow: Boolean,context: Context) {
     var progressDialog: ProgressBarDialog? = null
     if (isShow) {
          if (progressDialog == null) {
               progressDialog = ProgressBarDialog(context)
          }
          progressDialog?.show()
     } else {
               progressDialog?.dismiss()

          }
     }








