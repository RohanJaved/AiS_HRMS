package com.appinsnap.aishrm.ui.activities.forgotpassword

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivityForgotPasswordBinding
import com.appinsnap.aishrm.ui.activities.forgotpassword.models.ForgotPasswordRequestModel
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.otp.OTPActivity
import com.appinsnap.aishrm.util.GeneralDialogActivity
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.showProgressBar
import com.appinsnap.aishrm.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPassword : GeneralDialogActivity(), GeneralDialogActivity.locationPermission,
    GeneralDialogActivity.onClick {
    private lateinit var email: String
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var progressBarDialogRegister: ProgressBarDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get()!!.activity = this@ForgotPassword
        progressBarDialogRegister = ProgressBarDialog(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        livedataObserver()
        settingForgotEmail()
        handlingPasswordField()
        onClickListener()
    }

    private fun settingForgotEmail() {
        val sharedPreference: SharedPreferences =
            getSharedPreferences("forgotemail", Context.MODE_PRIVATE)
        val usernames = sharedPreference.getString("forgotemail","")
        binding.edtemail.setText(usernames)
    }

    private fun livedataObserver() {
        authenticationViewModel.progressLoading.observe(this, androidx.lifecycle.Observer {
            showProgressBar(progressBarDialogRegister, it)

        })
        authenticationViewModel._forgotpasswordresponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    savingForgotPasswordEmail()
                    val intent = Intent(this, OTPActivity::class.java)
                    intent.putExtra("useremail", email)
                    startActivity(intent)
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
        }

    }

    private fun savingForgotPasswordEmail() {
        email = binding.edtemail.text.toString()
        val sharedPreference: SharedPreferences =
            getSharedPreferences("forgotemail", Context.MODE_PRIVATE)
        val edit = sharedPreference.edit()
        edit.putString("forgotemail", email)
        edit.clear()
        edit.commit()
    }

    private fun onClickListener() {
        binding.backtologin.setSafeOnClickListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()

            
            this@ForgotPassword.onBackPressedDispatcher.onBackPressed()

        }
        binding.btnSend.setSafeOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val forgotpasswordrequest = ForgotPasswordRequestModel(username = email)

                CoroutineScope(Dispatchers.Main).launch {
                    authenticationViewModel.getGeneratedOtp(forgotpasswordrequest)
                }


            } else {

                it.showSnackbar("${validationResult.second}")


            }

        }
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        email = binding.edtemail.text.toString().trim()
        return authenticationViewModel.validateEmailCredential(email)
    }
    private fun handlingPasswordField() {

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().contains(" "))
                {
                    binding.edtemail.setText(s.toString().replace(" ",""))
                    binding.edtemail.setSelection(s.toString().length-1)
                }
            }


        }
        binding.edtemail.addTextChangedListener(textWatcher)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onclick(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onLocationPermission(locationvalue: Boolean) {
        TODO("Not yet implemented")
    }

}