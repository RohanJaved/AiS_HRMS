package com.appinsnap.aishrm.network

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
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.DownloadAttendanceReq
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestResubmissionRequest
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestResubmissionResponse
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models.LeaveResponseModel
import com.appinsnap.aishrm.ui.fragments.Policies.models.PoliciesSOPResponse
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectRequestModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectResponseModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationRequest
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationResponse
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.NewsResponseModel
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryRequestModel
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryResponseModel
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.ManagerCTOEmployeeRequest
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.ManagerCtoEmployeeResponse
import com.appinsnap.aishrm.ui.fragments.history.models.CancelRequestModel
import com.appinsnap.aishrm.ui.fragments.history.models.CancelResponseModel
import com.appinsnap.aishrm.ui.fragments.history.models.historyrequestmodel
import com.appinsnap.aishrm.ui.fragments.history.models.historyresponsemodel
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardRequest
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardResponse
import com.appinsnap.aishrm.ui.fragments.home.models.DashboardDataResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.DashboardDataRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.WeekAttendanceRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.*
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphRequest
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphResponse
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.FilterResponse
import com.appinsnap.aishrm.ui.fragments.home.models.statisticsmodels.GetStatisticsGraphRequest
import com.appinsnap.aishrm.ui.fragments.home.models.statisticsmodels.GetStatisticsGraphResponse
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalResponse
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
import com.appinsnap.aishrm.util.Constants
import com.appinsnap.aishrm.util.Constants.Companion.SETTING_API
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body

interface NetworkApi {
    @POST(Constants.Login_API)
    suspend fun getUserInfo(@Body loginrequest: LoginResquestModel): Response<LoginResponseModel>

    @POST(Constants.ForgotPassword_API)
    suspend fun getForgotPassword(@Body forgotpasswordrequest:com.appinsnap.aishrm.ui.activities.forgotpassword.models.ForgotPasswordRequestModel): Response<ForgotPasswordResponseModel>

    @POST(Constants.OTP_API)
    suspend fun getOTPResponse(@Body otprequest:com.appinsnap.aishrm.ui.activities.otp.models.OTPRequestModel):Response<OTPResponseModel>

    @POST(Constants.PasswordUpdate_API)
    suspend fun getPasswordUpdateResponse(@Body passwordupdate: PasswordUpdateRequestModel):Response<PasswordUpdateResponseModel>

    @POST(Constants.Dashboard_API)
    suspend fun getDashboardData(@Body dashboardrequest: DashboardDataRequestModel):Response<DashboardDataResponseModel>

    @POST(Constants.Attendance_API)
    suspend fun getAttendanceInfo(@Body attendancerequest: AddAttendanceRequestModel): Response<AddAttendanceResponseModel>

    @POST(Constants.AttendanceHistory_API)
    suspend fun getUserAttendanceHistory(@Body attendancehistoryrequest:AttendanceHistoryRequestModel):Response<AttendanceHistoryResponseModel>

    @POST(Constants.Notifications_API)
    suspend fun getNotifications(@Body inAppRequest: GetNotificationRequest):Response<GetNotificationResponse>

    @POST(Constants.Approval_API)
    suspend fun getApproval(@Body approvalRequest: ApprovalRequest):Response<ApprovalResponse>

    @POST(Constants.NotificatioCount_API)
    suspend fun getNotificationTotalCount(@Body noticationCount:NotificationCountRequestModel):Response<NotificationCountResponseModel>

    @POST(Constants.WeeklyAttendance_API)
    suspend fun getTotalWeeklyAttendance(@Body weekattendance:WeekAttendanceRequestModel):Response<WeeklyAttendanceResponseModel>

    @POST(Constants.DownloadAttendance_API)
    suspend fun downloadAttendance(@Body downloadAttendanceReq:DownloadAttendanceReq):Response<ResponseBody>

    @POST(Constants.DownloadGraphData)
    suspend fun downloadGraphData(@Body downloadGraphDataRequest: DownloadGraphDataRequest):Response<ResponseBody>

    @POST(SETTING_API)
    suspend fun dashboardSetting(@Header("AppSource") source: String,@Body request:SettingApiRequest):Response<SettingApiResponse>

    @POST(Constants.CTOManagerEmployee_API)
    suspend fun getCtoManagerEmployeeDetail(@Body ctomanageremployee: ManagerCTOEmployeeRequest):Response<ManagerCtoEmployeeResponse>

    @POST(Constants.CTOManagerDashboardView)
    suspend fun getCTOManagerDashboardAtt(@Body ctomanagerrequest:CTOManagerDashboardRequest):Response<CTOManagerDashboardResponse>

    @POST(Constants.REQUESTATTENDANCE_API)
    suspend fun getRequestAttendanceResponse(@Body requestattendance:RequestResubmissionRequest):Response<RequestResubmissionResponse>

    @POST(Constants.NotificationView_API)
    suspend fun updateNotificationCount(@Body requestnotificationcount:NotificationViewRequest):Response<NotificationViewResponse>

    @POST(Constants.LEAVES_DETAIL_API)
    suspend fun getleavesdetail(@Body requestleavesdetails:LeavesDetailRequestModel):Response<LeavesDetailResponseModel>


    @POST(Constants.LEAVE_REQUEST)
    suspend fun getleaverequest(
       @Body requestBody: MultipartBody):Response<LeaveResponseModel>
    @POST(Constants.HISTORY_API)
    suspend fun gethistory(@Body historyrequest:historyrequestmodel):Response<historyresponsemodel>

    @GET(Constants.NEWS_API)
    suspend fun getNews():Response<NewsResponseModel>

    @GET(Constants.GET_Policies_SOP)
    suspend fun getPoliciesSOPs():Response<PoliciesSOPResponse>

    @POST(Constants.AcceptRejectAPI)
    suspend fun getAcceptRejectResponse(@Body request:AcceptRejectRequestModel):Response<AcceptRejectResponseModel>

    @GET(Constants.GET_GRAPH_FILTERS)
    suspend fun getGraphFilters():Response<FilterResponse>

    @POST(Constants.GET_DASHBOARD_STATICS_GRAPH)
    suspend fun getStaticsGraphFilters(@Body request: GetStatisticsGraphRequest):Response<GetStatisticsGraphResponse>

    @POST(Constants.GET_DASHBOARD_ATTENDANCE_GRAPH)
    suspend fun getAttandenceGraph(@Body request: AttendanceGraphRequest):Response<AttendanceGraphResponse>

    @POST(Constants.Dashboard_API)
    suspend fun getDashboardInformation(@Body request:DashboardDataRequestModel):Response<GetDashboardResponseModelX>


    @POST(Constants.CancelRequest_API)
    suspend fun getCancelRequestResponse(@Body request:CancelRequestModel):Response<CancelResponseModel>

    @POST(Constants.LateSittingDetail_API)
    suspend fun getLateSittingDetail(@Body request:LateSittingDetailRequestModel):Response<LateSittingDetailsResponseModel>

    @POST(Constants.LateSittingDetailsSubmitt_API)
    suspend fun getLateSittingSubmittInfo(@Body request: SubmitLateSittingDetailsRequest):Response<SubmitLateSittingDetailsResponse>

    @POST(Constants.UpdateLateSittingDetails)
    suspend fun updateLateSitting(@Body request:UpdateLateSittingRequest ):Response<UpdateLateSittingResponse>

    @POST(Constants.isLateSittingEnableAPI)
    suspend fun getLateSittingStaus(@Body request:LateSittingStatusRequest):Response<LateSittingStausResponse>

    @POST(Constants.Logout_API)
    suspend fun logoutStatus(@Body request:LogoutRequestModel):Response<LogoutResponseModel>

    @POST(Constants.latesittingcheckin)
    suspend fun getLateSittingCheckin(@Body request:LateSittingCheckInRequest):Response<LateSittingCheckInResponse>

}