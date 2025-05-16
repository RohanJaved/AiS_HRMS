package com.appinsnap.aishrm.ui.activities.mainactivity.newpassword

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivityNewPassword2Binding
import com.appinsnap.aishrm.ui.activities.login.LoginActivity
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models.PasswordUpdateRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models.PasswordUpdateResponseModel
import com.appinsnap.aishrm.util.GeneralDialogActivity
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.showProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPasswordActivity : GeneralDialogActivity(), GeneralDialogActivity.locationPermission,
    GeneralDialogActivity.onClick {
    private lateinit var newpassword: String
    private var userid: String = ""
    private lateinit var confirmpassword: String
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var progressBarDialogRegister: ProgressBarDialog

    private lateinit var binding: ActivityNewPassword2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get()!!.activity = this@NewPasswordActivity
        progressBarDialogRegister = ProgressBarDialog(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_password2)
        getUserId()
        focusOnNextEditText()
        handlingPasswordField()
        livedataObserver()
        OnClickListeners()
    }
    private fun focusOnNextEditText() {
        binding.edtnewpassword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.edtconfirmpassword.requestFocus() // Focus on the next EditText
                true
            } else {
                false
            }
        }
    }

    private fun livedataObserver() {
        authenticationViewModel.progressLoading.observe(this, androidx.lifecycle.Observer {
            showProgressBar(progressBarDialogRegister, it)

        })
        authenticationViewModel._passwordupdateresponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showUpdatedPasswordDialog(response)

                }
                is NetworkResult.Error -> {
                    showGeneralDialogActivity(
                        this,
                        false,
                        "",
                        this,
                        this,
                        false,
                        "",
                        "${response.message}",
                        icon = R.drawable.reject
                    )
                }
                is NetworkResult.Loading -> {

                }
            }


        })
    }

    private fun getUserId() {
        val intent = intent.extras
        userid = intent?.get("userId") as String
    }

    private fun OnClickListeners() {
        binding.setpassword.setSafeOnClickListener {
            val validateResult = validateUserInput()
            if (validateResult.first) {

                val passwordupdate =
                    PasswordUpdateRequestModel(newPassword = newpassword, userID = userid)

                CoroutineScope(Dispatchers.Main).launch {
                    authenticationViewModel.passwordUpdate(passwordupdate)
                }




            } else {
                showGeneralDialogActivity(
                    this,
                    false,
                    "",
                    this,
                    this,
                    false,
                    "",
                    "${validateResult.second}",
                    icon = R.drawable.erroricon
                )

            }
        }
    }

    private fun showUpdatedPasswordDialog(response:NetworkResult.Success<PasswordUpdateResponseModel>) {
        val logoutDialog = Dialog(this)
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.general_dialog_design)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )

        val img:ImageView=logoutDialog.findViewById(R.id.imgPopIcon)
        val ok: ConstraintLayout = logoutDialog.findViewById(R.id.ok)
        val text:TextView = logoutDialog.findViewById(R.id.txtsuccess)
       // val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        //   val img:ImageView=logoutDialog.findViewById()
        text.text= "${response.data?.statusMessage}"
        img.setImageResource(R.drawable.tickicon)

        ok.setSafeOnClickListener {


            startActivity(Intent(this@NewPasswordActivity, LoginActivity::class.java))
            finish()

            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        newpassword = binding.edtnewpassword.text.toString()
        confirmpassword = binding.edtconfirmpassword.text.toString()

        return authenticationViewModel.validatePasswordCredential(newpassword, confirmpassword)
    }
    private fun handlingPasswordField() {

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().contains(" ")) {
                    binding.edtconfirmpassword.setText(s.toString().replace(" ", ""))
                    binding.edtconfirmpassword.setSelection(s.toString().length - 1)

                }


            }
        }
        val textWatcher2 = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().contains(" "))
                {

                    binding.edtnewpassword.setText(s.toString().replace(" ",""))
                    binding.edtnewpassword.setSelection(s.toString().length-1)
                }
            }


        }
        binding.edtconfirmpassword.addTextChangedListener(textWatcher)
        binding.edtnewpassword.addTextChangedListener(textWatcher2)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onclick(value: Boolean) {
    }

    override fun onLocationPermission(locationvalue: Boolean) {
    }
}