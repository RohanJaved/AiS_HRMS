package com.appinsnap.aishrm.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.appinsnap.aishrm.ui.activities.login.models.LoginResponseModel
import com.appinsnap.aishrm.ui.fragments.home.models.GetLeaveCounts
import com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse
import com.appinsnap.aishrm.ui.fragments.home.models.categories
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class SessionManagement {
    var context: Context? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    val loginResponse: LoginResponseModel? = null
    private var sessionManagement: SessionManagement? = null

    constructor(context: Context?) {
        this.context = context
        try {
            sharedPreferences = context?.getSharedPreferences("aishrms", MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
        } catch (e: Exception) {
//            MyHelperClass.getInstance().log()
            LoggerGenratter.getInstance().printLog("PREF", e.message)

        }
    }
    fun setUserInfo(loginresponse: LoginResponseModel) {
        if (loginresponse.body.fullName != null) {
            editor?.putString(Constants.empname, loginresponse.body.fullName)
        }
        if(loginresponse.body.isPMObit!=null)
        {
            editor?.putBoolean(Constants.isPMObit,loginresponse.body.isPMObit)
        }

        if (loginresponse.body.employeeID != null) {
            editor?.putString(Constants.empid, loginresponse.body.employeeID.toString())
        }
        if (loginresponse.body.profileURL != null) {
            editor?.putString(Constants.profileurl, loginresponse.body.profileURL)
        }
        if (loginresponse.body.reportingTo != null) {
            editor?.putString(Constants.reportingto, loginresponse.body.reportingTo)

        }
        if (loginresponse.body.department != null) {
            editor?.putString(Constants.department, loginresponse.body.department)
        }
        if (loginresponse.body.designation != null) {
            editor?.putString(Constants.designation, loginresponse.body.designation)
        }
        if (loginresponse.body.cellNumber != null) {
            editor?.putString(Constants.cellno, loginresponse.body.cellNumber)
        }
        if (loginresponse.body.email != null) {
            editor?.putString(Constants.email, loginresponse.body.email)
        }
        if (loginresponse.body.viewTypeID != null) {
            editor?.putInt(Constants.viewid, loginresponse.body.viewTypeID)
        }
        if (loginresponse.body.memberCount != null) {
            editor?.putInt(Constants.memberCount, loginresponse.body.memberCount)
        }
        if (loginresponse.body.departmentID != null) {
            editor?.putInt(Constants.departmentId, loginresponse.body.departmentID)
        }
        if (loginresponse != null) {
            editor?.putBoolean(Constants.islogin, true)
        }
        editor?.commit()
        editor?.apply()


    }

    fun setDepartmentId(departmentid: Int) {
        editor?.putInt("dptidTemp", departmentid)
        editor?.commit()
        editor?.apply()
    }




    fun setSelectedDeptId(departmentid: Int) {
        editor?.putInt("selectedId", departmentid)
        editor?.commit()
        editor?.apply()
    }
    fun setLateSittingNotID(departmentid: Int) {
        editor?.putInt(Constants.latesittingnotifid, departmentid)
        editor?.commit()
        editor?.apply()
    }
    fun getLateSittingNotID():Int{
        return sharedPreferences?.getInt(Constants.latesittingnotifid, -1)!!

    }

    fun getSelectedDeptId(): Int{
        return sharedPreferences?.getInt("selectedId", 0)!!
    }

    fun setSelectedDep(selectId: Int) {
        editor?.putInt("SelectedDepID", selectId)
        editor?.commit()
        editor?.apply()
    }


    fun setLocations(locations: String) {
        editor?.putString(Constants.OFFICE_LOCATIONS, locations)
        editor?.commit()
        editor?.apply()
    }

    fun setLeavesData(leavedata: String) {
        editor?.putString(Constants.Leaves_data, leavedata)
        editor?.commit()
        editor?.apply()
    }
    fun getLeaveData():ArrayList<GetLeaveCounts>{
        var leavesdata:ArrayList<GetLeaveCounts> = arrayListOf()
        val data = sharedPreferences?.getString(Constants.Leaves_data, "")
        val listType = object : TypeToken<ArrayList<GetLeaveCounts>>() {}.type
        val gson = Gson()
        if(data != null && data !="")
            leavesdata = gson.fromJson(data, listType)
        return leavesdata
    }

    fun setLastLocation(locations: String) {
        editor?.putString(Constants.LAST_LOCATIONS, locations)
        editor?.commit()
        editor?.apply()
    }

    fun setAttendanceStatus(name:String)
    {
        editor?.putString(Constants.attendanceStatus, name)
        editor?.commit()
        editor?.apply()
    }
    fun setCharacterLimit(limit:Int)
    {
        editor?.putInt(Constants.characterlimit,limit)
        editor?.commit()
        editor?.apply()
    }
    fun getCharacterLimit():Int
    {
        return sharedPreferences?.getInt(Constants.characterlimit, 0)!!
    }

    fun setCheckInTime(checkintime:String)
    {
        editor?.putString(Constants.checkinTime,checkintime)
        editor?.commit()
        editor?.apply()
    }
    fun getCheckInTime():String
    {
        return sharedPreferences?.getString(Constants.checkinTime, "")!!    }

    fun setLatitude(latitude:String)
    {
        editor?.putString(Constants.latitude,latitude)
        editor?.commit()
        editor?.apply()
    }
    fun getLatitude():String
    {
        return sharedPreferences?.getString(Constants.latitude, "")!!    }


fun setLongitude(longitude:String)
{
    editor?.putString(Constants.longitude,longitude)
    editor?.commit()
    editor?.apply()
}
  fun getLongitude():String
  {
    return sharedPreferences?.getString(Constants.longitude, "")!!
  }
    fun setIsApprovalLayout(approvallayout:String)
    {
        editor?.putString(Constants.approvallayout,approvallayout)
        editor?.commit()
        editor?.apply()
    }

    fun getIsApprovalLayout():String{
        return sharedPreferences?.getString(Constants.approvallayout, "")!!
    }

    fun setCheckInDate(date:String)
    {
        editor?.putString(Constants.checkindate,date)
        editor?.commit()
        editor?.apply()
    }
    fun getCheckInDate():String{
        return sharedPreferences?.getString(Constants.checkindate, "")!!
    }

    fun setStatus(status:String){
        editor?.putString(Constants.status,status)
        editor?.commit()
        editor?.apply()
    }
    fun getStatus():String{
        return sharedPreferences?.getString(Constants.status, "")!!
    }
    fun setEmployeeLoc(location:String){
        editor?.putString(Constants.employeeLoc,location)
        editor?.commit()
        editor?.apply()
    }
    fun getEmployeeLoc():String{
        return sharedPreferences?.getString(Constants.employeeLoc, "")!!
    }

    fun setNoOfDays(days:String){
        editor?.putString(Constants.noOfDays,days)
        editor?.commit()
        editor?.apply()
    }
    fun getNoOfDays():String{
        return sharedPreferences?.getString(Constants.noOfDays, "")!!
    }
    fun setCheckoutTime(checkoutTime:String)
    {
        editor?.putString(Constants.checkouttime,checkoutTime)
        editor?.commit()
        editor?.apply()
    }
    fun getCheckoutTime():String{
        return sharedPreferences?.getString(Constants.checkouttime, "")!!
    }

    fun getAttendanceStatus():String{
        return sharedPreferences?.getString(Constants.attendanceStatus, "")!!
    }

    fun setCategories(categories:String)
    {
        editor?.putString(Constants.categories, categories)
        editor?.commit()
        editor?.apply()
    }
    fun getCategories():List<categories>{
        var categories: ArrayList<categories> = arrayListOf()
        val data = sharedPreferences?.getString(Constants.categories, "")
        val listType = object : TypeToken<ArrayList<categories?>>() {}.type
        val gson = Gson()
        if(data != null && data !="")
            categories = gson.fromJson(data, listType)
        return categories
    }
    fun getLastLocation(): String{
        return sharedPreferences?.getString(Constants.LAST_LOCATIONS, "outoffice")!!
    }
    fun getLocations(): ArrayList<SettingApiResponse.Location?> {
        var officesLocationResponse: ArrayList<SettingApiResponse.Location?> = arrayListOf()
        val data = sharedPreferences?.getString(Constants.OFFICE_LOCATIONS, "")
        LoggerGenratter.getInstance().printLog("PrefLocations",data)
        val listType = object : TypeToken<ArrayList<SettingApiResponse.Location?>>() {}.type
        val gson = Gson()
        if(data != null && data !="")
            officesLocationResponse = gson.fromJson(data, listType)
        return officesLocationResponse
    }


    fun clearPreference() {
        editor?.clear()
        editor?.apply()
        editor?.commit()
    }

    fun getDepartmentId(): Int {
        return sharedPreferences?.getInt(Constants.departmentId, 0)!!
    }
    fun selectedPDFViewTab(name:String)
    {
        editor?.putString(Constants.selectedTab, name)
        editor?.commit()
        editor?.apply()
    }
    fun getPDFViewTab():String
    {
        return sharedPreferences?.getString(Constants.selectedTab, "")!!
    }

    fun setLateSittingStatus(status:Boolean)
    {
        editor?.putBoolean(Constants.latesitting, status)
        editor?.commit()
        editor?.apply()
    }

    fun getLateSittingStatus():Boolean
    {
        return sharedPreferences?.getBoolean(Constants.latesitting, false)!!
    }

    fun getSelectedDep(): Int {
        return sharedPreferences?.getInt("SelectedDepID", 0)!!
    }


    fun getDptidTemp(): Int {
        return sharedPreferences?.getInt("dptidTemp", 0)!!
    }


    fun getCellNo(): String {
        return sharedPreferences?.getString(Constants.cellno, "")!!
    }


    fun getName(): String? {
        return sharedPreferences?.getString(Constants.empname, "")!!
    }

    fun getDesignation(): String {
        return sharedPreferences?.getString(Constants.designation, "")!!
    }

    fun getMemberCount(): Int {
        return sharedPreferences?.getInt(Constants.memberCount, 0)!!
    }

    fun getEmpId(): String {
        return sharedPreferences?.getString(Constants.empid, "")!!
    }

    fun getProfileUrl(): String {
        return sharedPreferences?.getString(Constants.profileurl, "")!!
    }

    fun getReportingTo(): String {
        return sharedPreferences?.getString(Constants.reportingto, "")!!
    }

    fun isLogin(): Boolean? {

        var islogin = sharedPreferences?.getBoolean(Constants.islogin, false)
        if (islogin != null) {
            return islogin
        }
        return islogin
    }

    fun getDepartment(): String {
        return sharedPreferences?.getString(Constants.department, "")!!
    }
    fun getPMOStatus():Boolean{
            return sharedPreferences!!.getBoolean(Constants.isPMObit,false)

    }

    fun getEmail(): String {
        return sharedPreferences?.getString(Constants.email, "")!!
    }

    fun getViewId(): Int? {
        if (sharedPreferences != null && sharedPreferences?.getInt(Constants.viewid, 0) != null) {
            return sharedPreferences?.getInt(Constants.viewid, 0)

        } else {
            return 0
        }
    }

}