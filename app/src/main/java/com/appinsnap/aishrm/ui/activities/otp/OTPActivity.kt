package com.appinsnap.aishrm.ui.activities.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivityOtpactivityBinding
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.NewPasswordActivity
import com.appinsnap.aishrm.ui.activities.otp.models.OTPRequestModel
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
class OTPActivity : GeneralDialogActivity(), GeneralDialogActivity.locationPermission,
    GeneralDialogActivity.onClick {
    var otp: String = ""
    private var useremail: String = ""

    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private lateinit var binding: ActivityOtpactivityBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserEmail()
        MyApplication.get()!!.activity = this@OTPActivity
        progressBarDialogRegister = ProgressBarDialog(this)
        liveDataObserver()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
        binding.btnotp.setSafeOnClickListener {
            val validationresult = validateUserInput()
            if (validationresult.first) {
                val otp = OTPRequestModel(email = useremail, otpCode = otp)

                CoroutineScope(Dispatchers.Main).launch {
                    authenticationViewModel.validateOTP(otp)
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
                    "${validationresult.second}",
                    icon = R.drawable.erroricon
                )

            }

        }
    }

    private fun liveDataObserver() {
        authenticationViewModel.progressLoading.observe(this, androidx.lifecycle.Observer {
            showProgressBar(progressBarDialogRegister, it)

        })
        authenticationViewModel._otpresponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val intent = Intent(this, NewPasswordActivity::class.java)
                    intent.putExtra("userId", response.data?.body?.userID)
                    startActivity(intent)
                    finish()


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

    private fun getUserEmail() {
        val intent = intent.extras
        if (intent != null) {
            useremail = intent.get("useremail") as String
            val string = ""
        }
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        otp = binding.pinview.text.toString()
        return authenticationViewModel.validateOTPCredential(otp)
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
