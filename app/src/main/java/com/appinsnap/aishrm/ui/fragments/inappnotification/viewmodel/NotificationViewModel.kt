package com.appinsnap.aishrm.ui.fragments.inappnotification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.base.BaseViewModel
import com.appinsnap.aishrm.data.Repository
import com.appinsnap.aishrm.ui.activities.forgotpassword.models.ForgotPasswordResponseModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationRequest
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationResponse
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceResponseModel
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalResponse
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.InAppResponse
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.NotificationViewRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.NotificationViewResponse
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {
    var progressLoading = MutableLiveData<Boolean>()

    var _inappresponse: MutableLiveData<NetworkResult<GetNotificationResponse>> =
        MutableLiveData()
    val dashboardresponse: LiveData<NetworkResult<GetNotificationResponse>> = _inappresponse
    var _approvalResponse: MutableLiveData<NetworkResult<ApprovalResponse>> =
        MutableLiveData()
    var _notificationcountupdate:MutableLiveData<NetworkResult<NotificationViewResponse>> = MutableLiveData()
    var notificationcountupdate:LiveData<NetworkResult<NotificationViewResponse>> =_notificationcountupdate
    suspend fun getNotificationData(inAppRequest: GetNotificationRequest) {

        if(Utils.hasInternetConnection()) {

            progressLoading.postValue(true)
            _inappresponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.Notification(inAppRequest)
                    when (response.code()) {
                        200 -> {
                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _inappresponse.postValue(NetworkResult.Success(data))
                                    } else if (data.statusCode == "201") {
                                        _inappresponse.postValue(NetworkResult.Error(data.statusMessage))


                                    } else {
                                        progressLoading.postValue(false)
                                        _inappresponse.postValue(NetworkResult.Error(data.statusMessage))

                                    }

                                }
                            }
                        }

                        400 -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, InAppResponse::class.java)

                            _inappresponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                            progressLoading.postValue(false)
                        }

                        else -> {
                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, ForgotPasswordResponseModel::class.java)

                            _inappresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)


                        }
                    }

                }catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("NOTIFICATION ViewModel", e.toString())
                    _inappresponse.postValue(NetworkResult.Error("There is no or poor internet Connection. Please connect to stable internet connection and try again."))
                    progressLoading.postValue(false)
                }
            } else {
                _inappresponse.value = NetworkResult.Error("No Internet Connection.")
                progressLoading.postValue(false)

            }
        } else{
            _inappresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }

    }
    suspend fun getUpdatedNotificationCount(notificationupdaterequest: NotificationViewRequest) {

        _notificationcountupdate.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getUpdatedNotificationcounts(notificationupdaterequest)
                when(response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _notificationcountupdate.postValue(NetworkResult.Success(data))
                                }
                            }
                        }
                    }
                    400 ->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationViewResponse::class.java)
                        _notificationcountupdate.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                    }
                    else->{
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationViewResponse::class.java)

                        _notificationcountupdate.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                    }
                }

            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("NOTIFICATION ViewModel", e.toString())
                _notificationcountupdate.postValue(NetworkResult.Error("There is no or poor internet Connection. Please connect to stable internet connection and try again."))
            }
        } else {
            _notificationcountupdate.value = NetworkResult.Error("No Internet Connection.")

        }
    }

    suspend fun approveRequest(approvalRequest: ApprovalRequest) {

        if(Utils.hasInternetConnection()) {

            progressLoading.postValue(true)
            _inappresponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.UpdateApprovalStatus(approvalRequest)
                    when (response.code()) {
                        200-> {
                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _approvalResponse.postValue(NetworkResult.Success(data))
                                    } else if (data.statusCode == "201") {
                                        _approvalResponse.postValue(NetworkResult.Error(data.statusMessage))


                                    } else {
                                        progressLoading.postValue(false)
                                        _approvalResponse.postValue(NetworkResult.Error(data.statusMessage))

                                    }

                                }
                            }
                        }
                        400 ->{
                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, ApprovalResponse::class.java)

                            _approvalResponse.postValue(NetworkResult.Error("Unable to process your request at this time, please try again later!"))
                            progressLoading.postValue(false)
                        }
                        else ->{

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                            _approvalResponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                }catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("NOTIFICATION ViewModel", e.toString())
                    _approvalResponse.postValue(NetworkResult.Error(e.message))
                    _approvalResponse.postValue(NetworkResult.Error("There is no or poor internet Connection. Please connect to stable internet connection and try again."))

                    progressLoading.postValue(false)
                }
            }


        }
        else {
            _approvalResponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)


        }

    }


}