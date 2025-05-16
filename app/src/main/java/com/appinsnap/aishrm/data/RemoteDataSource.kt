package com.appinsnap.aishrm.data

import com.appinsnap.aishrm.network.NetworkApi
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
import com.appinsnap.aishrm.ui.fragments.home.models.*
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardRequest
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardResponse
import com.appinsnap.aishrm.ui.fragments.home.models.DashboardDataRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.DashboardDataResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.WeekAttendanceRequestModel
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
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val networkApi: NetworkApi
) {



    suspend fun getLoginResponse(login: LoginResquestModel): Response<LoginResponseModel> {
        return networkApi.getUserInfo(login)
    }
    suspend fun getForgotPasswordResponse(forgotpassword:ForgotPasswordRequestModel): Response<ForgotPasswordResponseModel> {
        return networkApi.getForgotPassword(forgotpassword)
    }

   suspend fun getOTPResponse(otprequest: com.appinsnap.aishrm.ui.activities.otp.models.OTPRequestModel): Response<OTPResponseModel>{
       return networkApi.getOTPResponse(otprequest)
   }
    suspend fun getPasswordUpdateResponse(passwordupdaterequest: PasswordUpdateRequestModel):Response<PasswordUpdateResponseModel>
    {
        return networkApi.getPasswordUpdateResponse(passwordupdaterequest)
    }
    suspend fun DashboardData(dashboardrequest: DashboardDataRequestModel):Response<DashboardDataResponseModel>{
        return networkApi.getDashboardData(dashboardrequest)
    }
    suspend fun AttendanceInfo(attendancerequest: AddAttendanceRequestModel): Response<AddAttendanceResponseModel> {
        return networkApi.getAttendanceInfo(attendancerequest)
    }
    suspend fun UserAttendanceHistory(attendancehistoryrequest:AttendanceHistoryRequestModel):Response<AttendanceHistoryResponseModel>
    {
        return networkApi.getUserAttendanceHistory(attendancehistoryrequest)
    }

    suspend fun Notification(inAppRequest: GetNotificationRequest):Response<GetNotificationResponse>
    {
        return networkApi.getNotifications(inAppRequest)
    }
    suspend fun UpdateApprovalStatus(approvalRequest: ApprovalRequest):Response<ApprovalResponse>
    {
        return networkApi.getApproval(approvalRequest)
    }
    suspend fun getTotalNotificationCount(notificationcount: NotificationCountRequestModel):Response<NotificationCountResponseModel>
    {
        return networkApi.getNotificationTotalCount(notificationcount)
    }
    suspend fun getWeeklyAttendance(weeklyattendance:WeekAttendanceRequestModel):Response<WeeklyAttendanceResponseModel>
    {
        return networkApi.getTotalWeeklyAttendance(weeklyattendance)
    }
    suspend fun downloadattendance(downloadAttendanceReq:DownloadAttendanceReq):Response<ResponseBody>
    {
        return networkApi.downloadAttendance(downloadAttendanceReq)
    }
    suspend fun downloadgraphData(downloadGraphAttendanceReq:DownloadGraphDataRequest):Response<ResponseBody>
    {
        return networkApi.downloadGraphData(downloadGraphAttendanceReq)
    }
      suspend fun getManagerCTOEmployees(managerctoemployeelist:ManagerCTOEmployeeRequest):Response<ManagerCtoEmployeeResponse>{
        return networkApi.getCtoManagerEmployeeDetail(managerctoemployeelist)
    }
    suspend fun getManagerCTODasboard(ctomanagerdashboardrequestt:CTOManagerDashboardRequest):Response<CTOManagerDashboardResponse>{
        return networkApi.getCTOManagerDashboardAtt(ctomanagerdashboardrequestt)
    }
    suspend fun  getDashboardSetting(source: String,request:SettingApiRequest):Response<SettingApiResponse>{
        return networkApi.dashboardSetting(source,request)
    }
    suspend fun getRequestAttendanceResult(request:RequestResubmissionRequest):Response<RequestResubmissionResponse>
    {
        return networkApi.getRequestAttendanceResponse(request)
    }
    suspend fun getUpdatedNotificationcounts(request:NotificationViewRequest):Response<NotificationViewResponse>
    {
        return networkApi.updateNotificationCount(request)
    }
    suspend fun getLeavesDetail(request:LeavesDetailRequestModel):Response<LeavesDetailResponseModel>
    {
        return networkApi.getleavesdetail(request)
    } 
    suspend fun getLeavesRequest(requestBody: MultipartBody):Response<LeaveResponseModel>
    {
        return networkApi.getleaverequest(
            requestBody
        )
    }
    suspend fun getHistory(request:historyrequestmodel):Response<historyresponsemodel>
    {
        return networkApi.gethistory(request)
    }
    suspend fun getNewsInfo():Response<NewsResponseModel>
    {
        return networkApi.getNews()
    }

    suspend fun getNotificationAcceptRejectResponse(request:AcceptRejectRequestModel):Response<AcceptRejectResponseModel>
    {
        return networkApi.getAcceptRejectResponse(request)
    }

    suspend fun getGraphFilters():Response<FilterResponse>
    {
        return networkApi.getGraphFilters()
    }

    suspend fun getStatisticsGraph(request: GetStatisticsGraphRequest):Response<GetStatisticsGraphResponse>
    {
        return networkApi.getStaticsGraphFilters(request)
    }

    suspend fun getAttendanceGraph(request: AttendanceGraphRequest):Response<AttendanceGraphResponse>
    {
        return networkApi.getAttandenceGraph(request)
    }

    suspend fun getInformationDashboard(request:DashboardDataRequestModel):Response<GetDashboardResponseModelX>
    {
        return networkApi.getDashboardInformation(request)
    }
    suspend fun getCancelRequest(request:CancelRequestModel):Response<CancelResponseModel>
    {
        return networkApi.getCancelRequestResponse(request)
    }
    suspend fun getPoliciesSOP():Response<PoliciesSOPResponse>
    {
        return networkApi.getPoliciesSOPs()
    }
    suspend fun getLateSittingInfo(request:LateSittingDetailRequestModel):Response<LateSittingDetailsResponseModel>
    {
        return networkApi.getLateSittingDetail(request)
    }

    suspend fun getLateSittingSubmittResponse(request:SubmitLateSittingDetailsRequest):Response<SubmitLateSittingDetailsResponse>
    {
        return networkApi.getLateSittingSubmittInfo(request)
    }

    suspend fun updateLateSitting(request:UpdateLateSittingRequest):Response<UpdateLateSittingResponse>
    {
        return networkApi.updateLateSitting(request)
    }

    suspend fun getLateSittingStatus(request:LateSittingStatusRequest):Response<LateSittingStausResponse>
    {
        return networkApi.getLateSittingStaus(request)
    }

    suspend fun getLogoutStatus(request:LogoutRequestModel):Response<LogoutResponseModel>
    {
        return networkApi.logoutStatus(request)
    }
    suspend fun getLateSittingCheckIn(request:LateSittingCheckInRequest):Response<LateSittingCheckInResponse>
    {
        return networkApi.getLateSittingCheckin(request)
    }
}