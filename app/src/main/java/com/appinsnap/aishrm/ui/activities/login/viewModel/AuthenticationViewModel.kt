package com.appinsnap.aishrm.ui.activities.login.viewModel

import android.app.Application
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.base.BaseViewModel
import com.appinsnap.aishrm.data.Repository
import com.appinsnap.aishrm.ui.activities.forgotpassword.models.ForgotPasswordRequestModel
import com.appinsnap.aishrm.ui.activities.forgotpassword.models.ForgotPasswordResponseModel
import com.appinsnap.aishrm.ui.activities.login.models.LoginResponseModel
import com.appinsnap.aishrm.ui.activities.login.models.LoginResquestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.LogoutRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.LogoutResponseModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.NotificationCountRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.NotificationCountResponseModel
import com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models.PasswordUpdateRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.newpassword.models.PasswordUpdateResponseModel
import com.appinsnap.aishrm.ui.activities.otp.models.OTPResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.DashboardDataResponseModel
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel  @Inject constructor(private val repository: Repository, application: Application) : AndroidViewModel(application){
    val context:Context
    init {

        context = application.applicationContext
    }

    var progressLoading = MutableLiveData<Boolean>()

    var _forgotpasswordresponse: MutableLiveData<NetworkResult<ForgotPasswordResponseModel>> = MutableLiveData()
    val forgotpasswordresponse: LiveData<NetworkResult<ForgotPasswordResponseModel>> = _forgotpasswordresponse
    var _notificationcountresponse: MutableLiveData<NetworkResult<NotificationCountResponseModel>> = MutableLiveData()
    val notificationcountresponse: LiveData<NetworkResult<NotificationCountResponseModel>> = _notificationcountresponse
    var _otpresponse: MutableLiveData<NetworkResult<OTPResponseModel>> = MutableLiveData()
    val otpresponse: LiveData<NetworkResult<OTPResponseModel>> = _otpresponse
    var _loginresponse:MutableLiveData<NetworkResult<LoginResponseModel>> = MutableLiveData()
    var loginresponse:LiveData<NetworkResult<LoginResponseModel>> =_loginresponse
    var _passwordupdateresponse: MutableLiveData<NetworkResult<PasswordUpdateResponseModel>> = MutableLiveData()
    val passwordupdateresponse: LiveData<NetworkResult<PasswordUpdateResponseModel>> = _passwordupdateresponse

    var _logoutstatusresponse: MutableLiveData<NetworkResult<LogoutResponseModel>> = MutableLiveData()
    val logoutstatusresponse: LiveData<NetworkResult<LogoutResponseModel>> = _logoutstatusresponse

    private lateinit var sessionManagement: SessionManagement


    fun validateCredentials(emailAddress: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress)) {
            result = Pair(false, "Please provide email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email address")
        }
        else if (TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide password")
        }
        else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        else if(password.contains(" "))
        {
            result = Pair(false, "Spaces are not allowed in password")
        }
        return result
    }
    fun validateEmailCredential(email: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(email)) {
            result = Pair(false, "Please provide email")

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please provide valid email address")

        }
        return result
    }
    fun validatePasswordCredential(password:String,confirmpassword:String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(password)) {
            result = Pair(false, "Please enter password")

        }
      else if  (TextUtils.isEmpty(confirmpassword)) {
            result = Pair(false, "Please enter confirm password")

        }
        else if(password.contains(" "))
        {
            result = Pair(false, "Blank spaces are not allowed in password")

        }
        else if(confirmpassword.contains(" "))
        {
            result = Pair(false, "Blank spaces are not allowed in confirm password")

        }
        else if (password!=confirmpassword) {
            result = Pair(false, "Password doesn't match")

        }
        else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        else if (confirmpassword.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
    suspend fun loginUser( loginUser:LoginResquestModel)
    {

        progressLoading.postValue(true)
        _loginresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection())
        {



            try {

                val response = repository.remote.getLoginResponse(loginUser)
                when(response.code()) {
                    200-> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _loginresponse.postValue(NetworkResult.Success(data))

                                } else if (data.statusCode == "201") {
                                    _loginresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }
                                else{
                                    _loginresponse.postValue(NetworkResult.Error(data.statusMessage))
                                    progressLoading.postValue(false)
                                }

                            }

                        }
                    }
                    400->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LoginResponseModel::class.java)
                        _loginresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                        progressLoading.postValue(false)
                    }
                    else->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LoginResponseModel::class.java)

                        _loginresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }

                }


            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("LOGIN", e.message)

                progressLoading.postValue(false)
                _loginresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))

            }

        }
        else{
            _loginresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }
    suspend fun getGeneratedOtp( forgotPassword:ForgotPasswordRequestModel)
    {

        progressLoading.postValue(true)
        _forgotpasswordresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getForgotPasswordResponse(forgotPassword)
                when (response.code()) {
                    200 -> {

                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _forgotpasswordresponse.postValue(NetworkResult.Success(data))
                                } else if (data.statusCode == "201") {
                                    _forgotpasswordresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }
                                else {
                                    _forgotpasswordresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        }
                    }
                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, ForgotPasswordResponseModel::class.java)

                        _forgotpasswordresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                        progressLoading.postValue(false)
                    }
                    else ->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, ForgotPasswordResponseModel::class.java)

                        _forgotpasswordresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("LOGIN", e.message)
                progressLoading.postValue(false)
                _forgotpasswordresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))

            }

        }
        else{
            _forgotpasswordresponse.postValue( NetworkResult.Error("No Internet Connection."))
            progressLoading.postValue(false)

        }

    }

    suspend fun logoutUser( logoutUser:LogoutRequestModel)
    {

        progressLoading.postValue(true)
        _logoutstatusresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection())
        {



            try {

                val response = repository.remote.getLogoutStatus(logoutUser)
                when(response.code()) {
                    200-> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.status == 200) {
                                    _logoutstatusresponse.postValue(NetworkResult.Success(data))

                                }
                                else{
                                    _logoutstatusresponse.postValue(NetworkResult.Error(data.message))
                                    progressLoading.postValue(false)
                                }

                            }

                        }
                    }
                    400->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LogoutResponseModel::class.java)
                        _logoutstatusresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                        progressLoading.postValue(false)
                    }
                    else->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LogoutResponseModel::class.java)

                        _logoutstatusresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }

                }


            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("LOGIN", e.message)

                progressLoading.postValue(false)
                _logoutstatusresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))

            }

        }
        else{
            _logoutstatusresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)
        }
    }
    suspend fun validateOTP( otp:com.appinsnap.aishrm.ui.activities.otp.models.OTPRequestModel)
    {

        progressLoading.postValue(true)
        _otpresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getOTPResponse(otp)

                when (response.code()) {

                    200 -> {


                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _otpresponse.postValue(NetworkResult.Success(data))
                                } else if (data.statusCode == "201") {
                                    _otpresponse.postValue(NetworkResult.Error(data.statusMessage))


                                } else {
                                    _otpresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        } else {
                            _otpresponse.postValue(NetworkResult.Error(""))
                            progressLoading.postValue(false)
                        }
                    }
                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, OTPResponseModel::class.java)

                        _otpresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                        progressLoading.postValue(false)
                    }
                    else ->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, OTPResponseModel::class.java)

                        _otpresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }

                }
            }
        catch (e: Exception) {

            LoggerGenratter.getInstance().printLog("LOGIN", e.message)

            _otpresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)

            }
        }
        else{
            _otpresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }





        }
    suspend fun passwordUpdate( passwordupdate: PasswordUpdateRequestModel)
    {

        progressLoading.postValue(true)
        _forgotpasswordresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getPasswordUpdateResponse(passwordupdate)
                when(response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _passwordupdateresponse.postValue(NetworkResult.Success(data))
                                } else if (data.statusCode == "201") {
                                    _passwordupdateresponse.postValue(NetworkResult.Error(data.statusMessage))


                                } else {
                                    _passwordupdateresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, PasswordUpdateResponseModel::class.java)

                        _passwordupdateresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, PasswordUpdateResponseModel::class.java)

                        _passwordupdateresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }




            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("LOGIN", e.message)

                _passwordupdateresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)
            }

        }
        else{
            _passwordupdateresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }
    suspend fun getNoficationCount( notificationcount:NotificationCountRequestModel)
    {

        _notificationcountresponse.postValue(NetworkResult.Loading())
        if(Utils.hasInternetConnection())
        {

            try {

                val response = repository.remote.getTotalNotificationCount(notificationcount)
                when(response.code()) {
                    200-> {
                        if (response.isSuccessful) {
                           // progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _notificationcountresponse.postValue(NetworkResult.Success(data))

                                } else if (data.statusCode == "201") {
                                    _notificationcountresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }
                                else{
                                    _notificationcountresponse.postValue(NetworkResult.Error(data.statusMessage))
                                 //   progressLoading.postValue(false)
                                }

                            }

                        }
                    }
                    400->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationCountResponseModel::class.java)
                        _notificationcountresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                     //   progressLoading.postValue(false)
                    }
                    else->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationCountResponseModel::class.java)

                        _notificationcountresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                      //  progressLoading.postValue(false)
                    }

                }


            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("LOGIN", e.message)

                progressLoading.postValue(false)
                _notificationcountresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))

            }


        }
        else{
            _notificationcountresponse.value = NetworkResult.Error("No Internet Connection.")
//            progressLoading.postValue(false)
        }
    }
    fun validateOTPCredential(otp:String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(otp)) {
            result = Pair(false, "Please provide OTP")
        }

       else if(otp.length< 4){
            result= Pair(false,"Please provide 4-digit OTP")
        }
        else{
            result = Pair(true,"")
        }
        return result
    }

}