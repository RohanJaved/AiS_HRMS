package com.appinsnap.aishrm.ui.fragments.home.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appinsnap.aishrm.base.BaseViewModel
import android.content.Context
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.data.Repository
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.DownloadAttendanceReq
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestResubmissionRequest
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestResubmissionResponse
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models.LeaveResponseModel
import com.appinsnap.aishrm.ui.fragments.Policies.models.PoliciesSOPResponse
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectRequestModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectResponseModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.NewsResponseModel
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryRequestModel
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryResponseModel
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.ManagerCTOEmployeeRequest
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.ManagerCtoEmployeeResponse
import com.appinsnap.aishrm.ui.fragments.history.models.CancelRequestModel
import com.appinsnap.aishrm.ui.fragments.history.models.CancelResponseModel
import com.appinsnap.aishrm.ui.fragments.history.models.historyrequestmodel
import com.appinsnap.aishrm.ui.fragments.history.models.historyresponsemodel
import com.appinsnap.aishrm.ui.fragments.home.models.*
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphRequest
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphResponse
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.FilterResponse
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.NotificationViewRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.NotificationViewResponse
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingCheckInRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingCheckInResponse
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingDetailRequestModel
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingDetailsResponseModel
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingStatusRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingStausResponse
import com.appinsnap.aishrm.ui.fragments.latesitting.models.SubmitLateSittingDetailsRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.SubmitLateSittingDetailsResponse
import com.appinsnap.aishrm.ui.fragments.latesitting.models.UpdateLateSittingRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.UpdateLateSittingResponse
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailRequestModel
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailResponseModel
import com.appinsnap.aishrm.ui.fragments.statics.model.DownloadGraphDataRequest
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository,application: Application) : BaseViewModel() {
    val context: Context
    init {
        context = application.applicationContext
    }

    var progressLoading = MutableLiveData<Boolean>()

    private val userComment: String = ""
    var _dashboardresponse: MutableLiveData<NetworkResult<DashboardDataResponseModel>> =
        MutableLiveData()
//    val dashboardresponse: LiveData<NetworkResult<DashboardDataResponseModel>> = _dashboardresponse


    var _attendancehistory: MutableLiveData<NetworkResult<AttendanceHistoryResponseModel>> =
        MutableLiveData()
    val attendancehistory: LiveData<NetworkResult<AttendanceHistoryResponseModel>> =
        _attendancehistory

    var _getdashboardinformation: MutableLiveData<NetworkResult<GetDashboardResponseModelX>> =
        MutableLiveData()
    val getdashboardinformation: LiveData<NetworkResult<GetDashboardResponseModelX>> =
        _getdashboardinformation
    var _attendanceresponse: MutableLiveData<NetworkResult<AddAttendanceResponseModel>> =
        MutableLiveData()
    val attendanceresponse: LiveData<NetworkResult<AddAttendanceResponseModel>> =
        _attendanceresponse
    var _latesittingstatusresponse: MutableLiveData<NetworkResult<LateSittingStausResponse>> =
        MutableLiveData()
    val latesittingstatusresponse: LiveData<NetworkResult<LateSittingStausResponse>> =
        _latesittingstatusresponse

    var _latesittingcheckin: MutableLiveData<NetworkResult<LateSittingCheckInResponse>> =
        MutableLiveData()
    val latesittingcheckin: LiveData<NetworkResult<LateSittingCheckInResponse>> = _latesittingcheckin
    var _weekattendanceresponse: MutableLiveData<NetworkResult<WeeklyAttendanceResponseModel>> = MutableLiveData()
    val weekattendanceresponse: LiveData<NetworkResult<WeeklyAttendanceResponseModel>> = _weekattendanceresponse
    var _managerandctoemployeedetail: MutableLiveData<NetworkResult<ManagerCtoEmployeeResponse>> =
        MutableLiveData()
    var managerandctoemployeedetail: LiveData<NetworkResult<ManagerCtoEmployeeResponse>> =
        _managerandctoemployeedetail
    var _managerctodashboardresponse: MutableLiveData<NetworkResult<CTOManagerDashboardResponse>> =
        MutableLiveData()
    var managerctodashboardresponse: LiveData<NetworkResult<CTOManagerDashboardResponse>> =
        _managerctodashboardresponse
    val _dashboardSettingResponse: MutableLiveData<NetworkResult<SettingApiResponse>> =
        MutableLiveData()
    val dashboardSettingResponse: LiveData<NetworkResult<SettingApiResponse>> =
        _dashboardSettingResponse
    val _latesittingdetailresponse: MutableLiveData<NetworkResult<LateSittingDetailsResponseModel>> =
        MutableLiveData()
    val latesittingdetailresponse: LiveData<NetworkResult<LateSittingDetailsResponseModel>> =
        _latesittingdetailresponse

    val _updatelatesittingdetailresponse: MutableLiveData<NetworkResult<UpdateLateSittingResponse>> =
        MutableLiveData()
    val updatelatesittingdetailresponse: LiveData<NetworkResult<UpdateLateSittingResponse>> =
        _updatelatesittingdetailresponse

    val _submitlatesittingdetailresponse: MutableLiveData<NetworkResult<SubmitLateSittingDetailsResponse>> =
        MutableLiveData()
    val submitlatesittingdetailresponse: LiveData<NetworkResult<SubmitLateSittingDetailsResponse>> =
        _submitlatesittingdetailresponse
    var _downloadAttendence: MutableLiveData<NetworkResult<ByteArray>> = MutableLiveData()
    val downloadAttendence: LiveData<NetworkResult<ByteArray>> = _downloadAttendence

    var _downloadGraphAttendence: MutableLiveData<NetworkResult<ByteArray>> = MutableLiveData()
    val downloadGraphAttendence: LiveData<NetworkResult<ByteArray>> = _downloadGraphAttendence

    var _requestattendanceemployee: MutableLiveData<NetworkResult<RequestResubmissionResponse>> =
        MutableLiveData()
    var requestattendanceemployee: LiveData<NetworkResult<RequestResubmissionResponse>> =
        _requestattendanceemployee
    var _notificationcountupdate: MutableLiveData<NetworkResult<NotificationViewResponse>> =
        MutableLiveData()
    var notificationcountupdate: LiveData<NetworkResult<NotificationViewResponse>> =
        _notificationcountupdate
    var _leavesdetailinfo: MutableLiveData<NetworkResult<LeavesDetailResponseModel>> =
        MutableLiveData()
    var leavesdetailinfo: LiveData<NetworkResult<LeavesDetailResponseModel>> = _leavesdetailinfo
    var _applyleaveresponse: MutableLiveData<NetworkResult<LeaveResponseModel>> = MutableLiveData()
    var applyleaveresponse: LiveData<NetworkResult<LeaveResponseModel>> = _applyleaveresponse
    var _historyresponse: MutableLiveData<NetworkResult<historyresponsemodel>> = MutableLiveData()
    var historyresponse: LiveData<NetworkResult<historyresponsemodel>> = _historyresponse
    var _newsresponse: MutableLiveData<NetworkResult<NewsResponseModel>> = MutableLiveData()
    var newsresponse: LiveData<NetworkResult<NewsResponseModel>> = _newsresponse
    var _accceptrejectresponse: MutableLiveData<NetworkResult<AcceptRejectResponseModel>> =
        MutableLiveData()
    var acceptrejectresponse: LiveData<NetworkResult<AcceptRejectResponseModel>> =
        _accceptrejectresponse

    var _getGraphFilter: MutableLiveData<NetworkResult<FilterResponse>> = MutableLiveData()
    var getGraphFilter: LiveData<NetworkResult<FilterResponse>> = _getGraphFilter

    var _getStatisticsGraph: MutableLiveData<NetworkResult<AttendanceGraphResponse>> =
        MutableLiveData()
    var getStatisticsGraph: LiveData<NetworkResult<AttendanceGraphResponse>> = _getStatisticsGraph

    var _getAttendanceGraph: MutableLiveData<NetworkResult<AttendanceGraphResponse>> =
        MutableLiveData()
    var getAttendanceGraph: LiveData<NetworkResult<AttendanceGraphResponse>> = _getAttendanceGraph

    var _getCancelRequest: MutableLiveData<NetworkResult<CancelResponseModel>> = MutableLiveData()
    var getCancelRequest: LiveData<NetworkResult<CancelResponseModel>> = _getCancelRequest

    var _getPoliciesSOP: MutableLiveData<NetworkResult<PoliciesSOPResponse>> = MutableLiveData()
    var getPoliciesSOP: LiveData<NetworkResult<PoliciesSOPResponse>> = _getPoliciesSOP
    suspend fun getUpdatedNotificationCount(notificationupdaterequest: NotificationViewRequest) {

        progressLoading.postValue(true)
        _dashboardresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response =
                    repository.remote.getUpdatedNotificationcounts(notificationupdaterequest)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _notificationcountupdate.postValue(NetworkResult.Success(data))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationViewResponse::class.java)
                        _notificationcountupdate.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, NotificationViewResponse::class.java)

                        _notificationcountupdate.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _notificationcountupdate.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)
            }
        } else {
            _notificationcountupdate.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getRequestAttendance(requestattendance: RequestResubmissionRequest) {
        progressLoading.postValue(true)
        _requestattendanceemployee.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getRequestAttendanceResult(requestattendance)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _requestattendanceemployee.postValue(NetworkResult.Success(data))
                                } else {
                                    _requestattendanceemployee.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, RequestResubmissionResponse::class.java)
                        _requestattendanceemployee.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, RequestResubmissionResponse::class.java)

                        _requestattendanceemployee.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _requestattendanceemployee.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)
            }
        } else {
            _requestattendanceemployee.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    private var isApiCalled = false

    suspend fun getLateSittingStatus(request:LateSittingStatusRequest) {

        _latesittingstatusresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLateSittingStatus(request)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _latesittingstatusresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _latesittingstatusresponse.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)
                        _latesittingstatusresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingStausResponse::class.java)

                        _latesittingstatusresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _latesittingstatusresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
            }
        } else {
            _latesittingstatusresponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }
    suspend fun updateLateSittingDetails(request:UpdateLateSittingRequest) {

        progressLoading.postValue(true)
        _updatelatesittingdetailresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.updateLateSitting(request)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _updatelatesittingdetailresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _updatelatesittingdetailresponse.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)
                        _updatelatesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)

                        _updatelatesittingdetailresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _updatelatesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)
            }
        } else {
            _updatelatesittingdetailresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }
    suspend fun getLateSittingCheckIn(request: LateSittingCheckInRequest) {
        _latesittingcheckin.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLateSittingCheckIn(request)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _latesittingcheckin.postValue(NetworkResult.Success(data))
                                } else {
                                    _latesittingcheckin.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingCheckInResponse::class.java)
                        _latesittingcheckin.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))

                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)

                        _latesittingcheckin.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _latesittingcheckin.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
            }
        } else {
            _latesittingcheckin.value = NetworkResult.Error("No Internet Connection.")

        }
    }
    suspend fun getLateSittingDetails(request: LateSittingDetailRequestModel) {

        progressLoading.postValue(true)
        _latesittingdetailresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLateSittingInfo(request)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _latesittingdetailresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _latesittingdetailresponse.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)
                        _latesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LateSittingDetailsResponseModel::class.java)

                        _latesittingdetailresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _latesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _latesittingdetailresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }


    suspend fun getLateSittingSubmittInfo(request: SubmitLateSittingDetailsRequest) {

        progressLoading.postValue(true)
        _submitlatesittingdetailresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLateSittingSubmittResponse(request)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _submitlatesittingdetailresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _submitlatesittingdetailresponse.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, SubmitLateSittingDetailsResponse::class.java)
                        _submitlatesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, SubmitLateSittingDetailsResponse::class.java)

                        _submitlatesittingdetailresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _submitlatesittingdetailresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _submitlatesittingdetailresponse.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getLeavesDetailInformation(requestleavedetails: LeavesDetailRequestModel) {

        progressLoading.postValue(true)
        _leavesdetailinfo.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLeavesDetail(requestleavedetails)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _leavesdetailinfo.postValue(NetworkResult.Success(data))
                                } else {
                                    _leavesdetailinfo.postValue(NetworkResult.Error(data.statusMessage))
                                }
                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LeavesDetailResponseModel::class.java)
                        _leavesdetailinfo.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, DashboardDataResponseModel::class.java)

                        _leavesdetailinfo.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }


            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _leavesdetailinfo.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _leavesdetailinfo.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getManagerCTOAttendance(ctomanagerrequest: CTOManagerDashboardRequest) {

        progressLoading.postValue(true)
        _managerctodashboardresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getManagerCTODasboard(ctomanagerrequest)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            isApiCalled = true
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _managerctodashboardresponse.postValue(
                                        NetworkResult.Success(
                                            data
                                        )
                                    )
                                } else {
                                    _managerctodashboardresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, DashboardDataResponseModel::class.java)
                        _managerctodashboardresponse.postValue(
                            NetworkResult.Error(
                                context.getString(
                                    R.string.bad_request
                                )
                            )
                        )
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, DashboardDataResponseModel::class.java)

                        _managerctodashboardresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _managerctodashboardresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _managerandctoemployeedetail.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getManagerCtoEmployeeList(managerctoemployeeattendance: ManagerCTOEmployeeRequest) {

        progressLoading.postValue(true)
        _managerandctoemployeedetail.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response =
                    repository.remote.getManagerCTOEmployees(managerctoemployeeattendance)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _managerandctoemployeedetail.postValue(
                                        NetworkResult.Success(
                                            data
                                        )
                                    )
                                } else {
                                    _managerandctoemployeedetail.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, ManagerCtoEmployeeResponse::class.java)
                        _managerandctoemployeedetail.postValue(
                            NetworkResult.Error(
                                context.getString(
                                    R.string.bad_request
                                )
                            )
                        )
                        progressLoading.postValue(false)
                    }

                    else -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, ManagerCtoEmployeeResponse::class.java)

                        _managerandctoemployeedetail.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _managerandctoemployeedetail.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _managerandctoemployeedetail.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getWeekAttendance(weekattendancerequest: WeekAttendanceRequestModel) {
        progressLoading.postValue(true)
        _attendancehistory.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getWeeklyAttendance(weekattendancerequest)
                when (response.code()) {
                    200 -> {
                        progressLoading.postValue(false)
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _weekattendanceresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _weekattendanceresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        } else {
                            _weekattendanceresponse.postValue(NetworkResult.Error("Response Not Successful"))
                        }
                    }

                    400 -> {
                        progressLoading.postValue(false)
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, WeeklyAttendanceResponseModel::class.java)

                        _weekattendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))

                    }

                    else -> {
                        _weekattendanceresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                progressLoading.postValue(false)
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _weekattendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
            }
        } else {
            progressLoading.postValue(false)
            _weekattendanceresponse.value = NetworkResult.Error("No Internet Connection.")


        }
    }

    suspend fun downloadAttendence(downloadAttendanceReq: DownloadAttendanceReq) {
        progressLoading.postValue(true)
        _attendancehistory.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.downloadattendance(downloadAttendanceReq)
                when (response.code()) {
                    200 -> {
                        progressLoading.postValue(false)
                        if (response.isSuccessful) {
                            val data = response.body()?.bytes()
                            data?.let { data ->
                                /* if (data.statusCode == "200") {
                                    _weekattendanceresponse.postValue(NetworkResult.Success(data))
                                } else if (data.statusCode == "201") {
                                    _weekattendanceresponse.postValue(NetworkResult.Error(data.statusMessage))


                                } else {
                                    _weekattendanceresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }*/
                                try {
                                    _downloadAttendence.postValue(NetworkResult.Success(data))

                                } catch (e: Exception) {
                                    _downloadAttendence.postValue(NetworkResult.Error(e.message))

                                }
                            }
                        } else {
                            _downloadAttendence.postValue(NetworkResult.Error("Response Not Successful"))
                        }
                    }

                    400 -> {
                        progressLoading.postValue(false)
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, WeeklyAttendanceresponce::class.java)
                        _downloadAttendence.postValue(NetworkResult.Error("No record found"))

                    }

                    else -> {
                        _downloadAttendence.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                progressLoading.postValue(false)
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _downloadAttendence.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
            }
        } else {
            progressLoading.postValue(false)
            _downloadAttendence.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    suspend fun downloadGraphData(downloadGraphDataRequest: DownloadGraphDataRequest) {
        progressLoading.postValue(true)
        _attendancehistory.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.downloadgraphData(downloadGraphDataRequest)
                when (response.code()) {
                    200 -> {
                        progressLoading.postValue(false)
                        if (response.isSuccessful) {
                            val data = response.body()?.bytes()
                            data?.let { data ->
                                /* if (data.statusCode == "200") {
                                     _weekattendanceresponse.postValue(NetworkResult.Success(data))
                                 } else if (data.statusCode == "201") {
                                     _weekattendanceresponse.postValue(NetworkResult.Error(data.statusMessage))


                                 } else {
                                     _weekattendanceresponse.postValue(NetworkResult.Error(data.statusMessage))

                                 }*/
                                try {
                                    _downloadGraphAttendence.postValue(NetworkResult.Success(data))

                                } catch (e: Exception) {
                                    _downloadGraphAttendence.postValue(NetworkResult.Error(e.message))

                                }
                            }
                        } else {
                            _downloadGraphAttendence.postValue(NetworkResult.Error("Response Not Successful"))
                        }
                    }

                    400 -> {
                        progressLoading.postValue(false)
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, WeeklyAttendanceresponce::class.java)
                        _downloadGraphAttendence.postValue(NetworkResult.Error("No record found"))

                    }

                    else -> {
                        _downloadGraphAttendence.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                progressLoading.postValue(false)
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _downloadGraphAttendence.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
            }
        } else {
            progressLoading.postValue(false)
            _downloadGraphAttendence.value = NetworkResult.Error("No Internet Connection.")


        }
    }

    suspend fun getAttendanceHistory(attendanceHistoryRequest: AttendanceHistoryRequestModel) {

        progressLoading.postValue(true)
        _attendancehistory.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.UserAttendanceHistory(attendanceHistoryRequest)
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _attendancehistory.postValue(NetworkResult.Success(data))
                                } else {
                                    _attendancehistory.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        } else {
                            _attendancehistory.postValue(NetworkResult.Error("User Not Found"))
                            progressLoading.postValue(false)
                        }
                    }

                    400 -> {
                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                        _attendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)

                    }

                    else -> {
                        _attendancehistory.postValue(NetworkResult.Error("Internal Server Error"))
                        progressLoading.postValue(false)
                    }
                }

            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _attendancehistory.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _attendancehistory.value = NetworkResult.Error("No Internet Connection.")
            progressLoading.postValue(false)

        }
    }

    suspend fun getAttendanceResponse(attendancerequest: AddAttendanceRequestModel) {

        progressLoading.postValue(true)
        _attendanceresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.AttendanceInfo(attendancerequest)



                when (response.code()) {
                    200 -> {

                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _attendanceresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _attendanceresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        } else {
                            _attendanceresponse.postValue(NetworkResult.Error("User Not Found"))
                            progressLoading.postValue(false)
                        }
                    }

                    400 -> {


                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                        _attendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {

                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                        _attendanceresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _attendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _attendanceresponse.value = NetworkResult.Error("No Internet Connection")
            progressLoading.postValue(false)

        }
    }

    suspend fun getManagerCTOE(attendancerequest: AddAttendanceRequestModel) {

        progressLoading.postValue(true)
        _attendanceresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.AttendanceInfo(attendancerequest)



                when (response.code()) {
                    200 -> {

                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _attendanceresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _attendanceresponse.postValue(NetworkResult.Error(data.statusMessage))

                                }

                            }
                        } else {
                            _attendanceresponse.postValue(NetworkResult.Error("User Not Found"))
                            progressLoading.postValue(false)
                        }
                    }

                    400 -> {


                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                        _attendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {

                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, AddAttendanceResponseModel::class.java)

                        _attendanceresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _attendanceresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                progressLoading.postValue(false)
            }
        } else {
            _attendanceresponse.value = NetworkResult.Error("No Internet Connection")
            progressLoading.postValue(false)

        }
    }


    suspend fun getApplyLeave(requestBody: MultipartBody) {

        progressLoading.postValue(true)
        _applyleaveresponse.postValue(NetworkResult.Loading())
        if (Utils.hasInternetConnection()) {
            try {
                val response = repository.remote.getLeavesRequest(
                    requestBody
                )
                when (response.code()) {
                    200 -> {

                        if (response.isSuccessful) {
                            progressLoading.postValue(false)
                            val data = response.body()
                            data?.let { data ->
                                if (data.statusCode == "200") {
                                    _applyleaveresponse.postValue(NetworkResult.Success(data))
                                } else {
                                    _applyleaveresponse.postValue(NetworkResult.Error(data.statusMessage))
                                }


                            }
                        }
                    }

                    400 -> {


                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LeaveResponseModel::class.java)

                        _applyleaveresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                        progressLoading.postValue(false)
                    }

                    else -> {

                        val json = response.errorBody()?.string()//.body()?.statusMessage
                        val gson = Gson()
                        val data = gson.fromJson(json, LeaveResponseModel::class.java)

                        _applyleaveresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                        progressLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                _applyleaveresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                progressLoading.postValue(false)
            }
        } else {
            _applyleaveresponse.value = NetworkResult.Error("No Internet Connection")
            progressLoading.postValue(false)

        }
    }

    suspend fun getDashboardInformation(request: DashboardDataRequestModel) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getdashboardinformation.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getInformationDashboard(request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getdashboardinformation.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _getdashboardinformation.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    }
                                }
                            } else {
                                _getdashboardinformation.postValue(NetworkResult.Error("User Not Found"))
                                progressLoading.postValue(false)
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, GetDashboardResponseModelX::class.java)

                            _getdashboardinformation.postValue(
                                NetworkResult.Error(
                                    context.getString(
                                        R.string.bad_request
                                    )
                                )
                            )
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, GetDashboardResponseModelX::class.java)

                            _getdashboardinformation.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getdashboardinformation.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getdashboardinformation.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getDashboardSetting(request: SettingApiRequest) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _dashboardSettingResponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getDashboardSetting("PLAYSTORE", request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _dashboardSettingResponse.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    }
                                    if (data.statusCode == "203") {
                                        _dashboardSettingResponse.postValue(
                                            NetworkResult.Error("203")
                                        )
                                    }
                                    if (data.statusCode == "401") {
                                        _dashboardSettingResponse.postValue(
                                            NetworkResult.Error("401")
                                        )
                                    }


                                }
                            } else {
                                _dashboardSettingResponse.postValue(NetworkResult.Error("User Not Found"))
                                progressLoading.postValue(false)
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _dashboardSettingResponse.postValue(
                                NetworkResult.Error(
                                    context.getString(
                                        R.string.bad_request
                                    )
                                )
                            )
                            progressLoading.postValue(false)
                        }

                        403 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _dashboardSettingResponse.postValue(
                                NetworkResult.Error(
                                    response.code().toString()
                                )
                            )
                            progressLoading.postValue(false)
                        }

                        201 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _dashboardSettingResponse.postValue(
                                NetworkResult.Error(
                                    response.code().toString()
                                )
                            )
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _dashboardSettingResponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _dashboardSettingResponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _dashboardSettingResponse.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getNewsData() {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _newsresponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getNewsInfo()
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _newsresponse.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _newsresponse.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, NewsResponseModel::class.java)

                            _newsresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, NewsResponseModel::class.java)

                            _newsresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _newsresponse.postValue(NetworkResult.Error(context.getString(R.string.exception_error2)))
                    progressLoading.postValue(false)
                }
            } else {
                _newsresponse.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getAcceptRejectNotification(request: AcceptRejectRequestModel) {
        viewModelScope.launch {
            progressLoading.postValue(true)
            _accceptrejectresponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getNotificationAcceptRejectResponse(request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _accceptrejectresponse.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _accceptrejectresponse.postValue(NetworkResult.Error(data.statusMessage.toString()))
                                    }


                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, AcceptRejectResponseModel::class.java)

                            _accceptrejectresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, AcceptRejectResponseModel::class.java)

                            _accceptrejectresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _accceptrejectresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _accceptrejectresponse.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getHistoryInfo(request: historyrequestmodel) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _historyresponse.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getHistory(request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _historyresponse.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _historyresponse.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, historyresponsemodel::class.java)

                            _historyresponse.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, historyresponsemodel::class.java)

                            _historyresponse.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _historyresponse.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _historyresponse.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getStatisticsDashboardGraph(request: AttendanceGraphRequest) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getStatisticsGraph.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getAttendanceGraph(request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getStatisticsGraph.postValue(
                                            NetworkResult.Success(data)
                                        )
                                    } else {
                                        _getStatisticsGraph.postValue(NetworkResult.Error(data.statusMessage))
                                    }


                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, AttendanceGraphResponse::class.java)

                            _getStatisticsGraph.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, AttendanceGraphResponse::class.java)

                            _getStatisticsGraph.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getStatisticsGraph.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getStatisticsGraph.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun getAttendanceDashboardGraph(request: AttendanceGraphRequest) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getStatisticsGraph.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getAttendanceGraph(request)
                    when (response.code()) {
                        200 -> {
                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getAttendanceGraph.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _getAttendanceGraph.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {
                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, historyresponsemodel::class.java)
                            _getAttendanceGraph.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _getAttendanceGraph.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getAttendanceGraph.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getAttendanceGraph.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }

    }

    fun cancelRequestResponse(request: CancelRequestModel) {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getCancelRequest.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getCancelRequest(request)
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getCancelRequest.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _getCancelRequest.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {
                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, CancelResponseModel::class.java)

                            _getCancelRequest.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, CancelResponseModel::class.java)
                            _getCancelRequest.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getCancelRequest.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getCancelRequest.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)
            }
        }

    }

    fun getGraphFilters() {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getGraphFilter.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getGraphFilters()
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getGraphFilter.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _getGraphFilter.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, historyresponsemodel::class.java)

                            _getGraphFilter.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _getGraphFilter.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getGraphFilter.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getGraphFilter.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }
    }


    fun getPoliciesSOP() {
        viewModelScope.launch {

            progressLoading.postValue(true)
            _getPoliciesSOP.postValue(NetworkResult.Loading())
            if (Utils.hasInternetConnection()) {
                try {
                    val response = repository.remote.getPoliciesSOP()
                    when (response.code()) {
                        200 -> {

                            if (response.isSuccessful) {
                                progressLoading.postValue(false)
                                val data = response.body()
                                data?.let { data ->
                                    if (data.statusCode == "200") {
                                        _getPoliciesSOP.postValue(
                                            NetworkResult.Success(
                                                data
                                            )
                                        )
                                    } else {
                                        _getPoliciesSOP.postValue(NetworkResult.Error(data.statusMessage))
                                    }
                                }
                            }
                        }

                        400 -> {


                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, historyresponsemodel::class.java)

                            _getPoliciesSOP.postValue(NetworkResult.Error(context.getString(R.string.bad_request)))
                            progressLoading.postValue(false)
                        }

                        else -> {

                            val json = response.errorBody()?.string()//.body()?.statusMessage
                            val gson = Gson()
                            val data = gson.fromJson(json, SettingApiResponse::class.java)

                            _getPoliciesSOP.postValue(NetworkResult.Error("We are experiencing technical issues at the moment. Please try again shortly."))
                            progressLoading.postValue(false)
                        }
                    }
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HOME ViewModel", e.toString())
                    _getPoliciesSOP.postValue(NetworkResult.Error(context.getString(R.string.internt_problem)))
                    progressLoading.postValue(false)
                }
            } else {
                _getPoliciesSOP.value = NetworkResult.Error("No Internet Connection")
                progressLoading.postValue(false)

            }
        }
    }


    fun validateDateCredentials(todate: String, fromdate: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(fromdate)) {
            result = Pair(false, "Please select From Date")
        } else if (TextUtils.isEmpty(todate)) {
            result = Pair(false, "Please select To Date")
        } else if (todate < fromdate) {
            result = Pair(false, "To Date should be greater than From date")
        }
        return result
    }

    fun validateComments(comments: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (comments.isEmpty()) {
            result = Pair(false, "Add Reason for request")
        }
        return result
    }

    fun validateTimeCredentials(
        checkintime: org.threeten.bp.LocalTime?,
        checkouttime: org.threeten.bp.LocalTime?
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (checkintime == null) {
            result = Pair(false, "Please select Check-in time")
        } else if (checkouttime == null) {
            result = Pair(false, "Please select Check-out time")
        } else if (checkouttime != null) {
            if (checkouttime.isBefore(checkintime)) {
                result = Pair(false, "Check-out time should be greater than check-in time")
            }
        }
        return result
    }

    fun validateTimeCredentialsLateSitting(
        checkintime: String?,
        checkouttime: String?
    ): Pair<Boolean, String> {
        val result = Pair(true, "")

        if (checkintime != null && checkouttime != null) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                val startTime = Instant.from(formatter.parse(checkintime.replace("ZZ", "Z")))
                val endTime = Instant.from(formatter.parse(checkouttime.replace("ZZ", "Z")))


                if (endTime.isBefore(startTime)) {
                    return Pair(false, "Selected date and time should not be less than check-in date and time")
                }
            } catch (e: Exception) {
                return Pair(false, "Invalid date-time format")
            }
        }

        return result
    }
}



