package com.appinsnap.aishrm.ui.activities.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.BuildConfig
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivityLogin2Binding
import com.appinsnap.aishrm.ui.activities.forgotpassword.ForgotPassword
import com.appinsnap.aishrm.ui.activities.login.models.LoginResquestModel
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import com.appinsnap.aishrm.util.*
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : GeneralDialogActivity(), GeneralDialogActivity.onClick,
    GeneralDialogActivity.locationPermission {
    private lateinit var binding: ActivityLogin2Binding
    private lateinit var email: String
    private val permissionId = 0
    private val deviceType = 1

    private lateinit var pass: String

    companion object {
        var fToken = ""
    }

    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private var sessionmanagement: SessionManagement? = null


    //   private lateinit var progressBarDialogRegister: ProgressBarDialog
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private var isPushNotification = false
    private val STORAGE_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.get()!!.activity = this@LoginActivity
        progressBarDialogRegister = ProgressBarDialog(this)
        sessionmanagement = SessionManagement(context = this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login2)
        checkForPushNotification()
        checkLoginStatus()
        focusOnNextEditText()
        // getAndroidDeviceId()
        observingLiveData()
        onClickListeners()
        settingEmailCredential()
        setFirebaseToken()
        handlingPasswordField()
        if (BuildConfig.DEBUG) {
            binding.version.setText("Version " + BuildConfig.VERSION_NAME + " QA")
        }else
        {
            binding.version.setText("Version " + BuildConfig.VERSION_NAME)

        }
        //  setEmail()

//        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked)
//                binding.checkBox.setbutt
//        }

    }

    private fun focusOnNextEditText() {
        binding.edtemail.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.edtpassword.requestFocus() // Focus on the next EditText
                true
            } else {
                false
            }
        }
    }

    private fun settingEmailCredential() {
        val sharedPreference: SharedPreferences =
            getSharedPreferences("useremail", Context.MODE_PRIVATE)
        val usernames = sharedPreference.getString("username", "")
        binding.edtemail.setText(usernames)

    }

    private fun handlingPasswordField() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().contains(" ")) {
                    binding.edtpassword.setText(s.toString().replace(" ", ""))
                    binding.edtpassword.setSelection(s.toString().length - 1)
                }
            }


        }
        val textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().contains(" ")) {
                    binding.edtemail.setText(s.toString().replace(" ", ""))
                    binding.edtemail.setSelection(s.toString().length - 1)

                }
            }


        }

        binding.edtpassword.addTextChangedListener(textWatcher)
        binding.edtemail.addTextChangedListener(textWatcher2)
    }





    fun getApplicationsWithCameraAccess(context: Context): List<String> {
        val packageManager = context.packageManager
        val applicationsWithCameraAccess = mutableListOf<String>()

        val installedApplications =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        GlobalScope.launch {


            for (applicationInfo in installedApplications) {
                try {
                    val packageInfo = packageManager.getPackageInfo(
                        applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS
                    )
                    val requestedPermissions = packageInfo.requestedPermissions

                    if (requestedPermissions != null) {
                        for (permission in requestedPermissions) {
                            if (permission.toString().contains("ACCESS_MOCK_LOCATION")) {
                                applicationsWithCameraAccess.add(applicationInfo.packageName)
                                break
                            }
                        }
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }

        return applicationsWithCameraAccess
    }
    /* fun isMockLocationEnabled(context: Context): Boolean {
        *//* return Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ALLOW_MOCK_LOCATION,
            0
        ) != 0*//*
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }*/


    private fun checkLoginStatus() {
      val userlogin =   sessionmanagement?.isLogin()
        if(userlogin!=null) {
            if (userlogin) {

                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    ).putExtra("isPushNotification", isPushNotification)
                )
                finish()

            }
        }
    }

    private fun observingLiveData() {
        authenticationViewModel.progressLoading.observe(this, androidx.lifecycle.Observer {
            showProgressBar(progressBarDialogRegister, it)

        })
        authenticationViewModel._loginresponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { SessionManagement(this).setUserInfo(it) }
                    savingEmailCredentials()
                        email = ""
                        pass = ""
                        binding.edtemail.text?.clear()
                        binding.edtpassword.text?.clear()
//                    sessionmanagement?.setLogintStatus()
                        startActivity(Intent(this, MainActivity::class.java).putExtra("isPushNotification", isPushNotification))
                        finish()
                    }






                is NetworkResult.Error -> {
                    // view.showSnackbar("${response.message}")
                    //   Toast.makeText(this, "${response.message}", Toast.LENGTH_LONG).show()
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

    private fun savingEmailCredentials() {
        email = binding.edtemail.text.toString()
        val sharedPreference: SharedPreferences =
            getSharedPreferences("useremail", Context.MODE_PRIVATE)
        val edit = sharedPreference.edit()
        edit.putString("username", email)
        edit.clear()
        edit.commit()
    }


    private fun updatingLoginStatus() {

        sessionmanagement!!.clearPreference()

    }

    private fun onClickListeners() {
        binding.txtforgotpassword.setSafeOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        binding.login.setSafeOnClickListener {


            /* if (!getApplicationsWithCameraAccess(applicationContext).isEmpty()) {
                 // Mock locations are enabled
                 // Display a warning message or restrict functionality
                 Toast.makeText(this,"Invalid location",Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this,"fine location",Toast.LENGTH_SHORT).show()

                 // Mock locations are not enabled
                 // Proceed with normal app behavior
             }*/


            val validationresult = validateUserInput()
            if (validationresult.first) {
                        sendLoginRequest()
                    }

             else {
                it.showSnackbar("${validationresult.second}")

            }

        }
    }

    private fun sendLoginRequest() {
        val id: String = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        val loginrequest = LoginResquestModel(
            username = email, password = pass, token = fToken, device_id = id, device = deviceType
        )


        CoroutineScope(Dispatchers.Main).launch {
            authenticationViewModel.loginUser(loginrequest)
        }


    }

    private fun setFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(OnCompleteListener<String> { task ->
                if (!task.isSuccessful) {
                    Log.e("TAG", "Failed to get firebase the token.")
                    return@OnCompleteListener
                }

                fToken = task.result

                LoggerGenratter.getInstance().printLog("LOGIN", "firebase Token : $fToken")

            }).addOnFailureListener(OnFailureListener { e ->
                Log.e(
                    "TAG",
                    "Failed to get the token : " + e.localizedMessage
                )
            })
    }

    private fun setEmail() {
        sessionmanagement = SessionManagement(this@LoginActivity)
//        binding.edtemail.setText(sessionmanagement!!.getEmail())
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
      //  val myFragment: LeaveApplication = fragmentManager.findFragmentByTag("MY_FRAGMENT_TAG") as LeaveApplication

        return super.dispatchTouchEvent(ev)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        email = binding.edtemail.text.toString().trim()
        pass = binding.edtpassword.text.toString().trim()

        return authenticationViewModel.validateCredentials(email, pass)
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        ) {
            return true
        }
        return false
    }





    private fun requestPermissions() {

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            permissionId
        )
    }

    override fun onclick(value: Boolean) {
        if (value) {
            requestPermissions()
        }
    }

    override fun onLocationPermission(locationvalue: Boolean) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }

    private fun checkForPushNotification() {
        try {
            val intent = intent
            if (intent != null && intent.extras != null) {
                val extras = intent.extras
                isPushNotification = extras!!.getBoolean("isPushNotification", false)

            }
        } catch (exp: Exception) {
            LoggerGenratter.getInstance().printLog("LOGIN", "checkForPushNotification: ${exp.message}")
        }
    }
}