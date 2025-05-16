

package com.appinsnap.aishrm.ui.fragments.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.*
import com.appinsnap.aishrm.databinding.FragmentHomeBinding
import com.appinsnap.aishrm.databinding.ShowfiltersdialogBinding
import com.appinsnap.aishrm.ui.activities.login.LoginActivity
import com.appinsnap.aishrm.ui.activities.login.LoginActivity.Companion.fToken
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo
import com.appinsnap.aishrm.ui.fragments.home.adapter.*
import com.appinsnap.aishrm.ui.fragments.home.models.*
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphRequest
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.CtoAndManagerModelresponceSummery
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.EmployeedetailModelresponces
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.FilterResponse
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingCheckInRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingStatusRequest
import com.appinsnap.aishrm.ui.fragments.statics.model.StatisticAttendanceInfo
import com.appinsnap.aishrm.util.*
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.services.LocationService
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import worker8.com.github.radiogroupplus.RadioGroupPlus
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*

@AndroidEntryPoint
class HomeFragment : GeneralDialogFragment(),
    GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission, DepartmentGroupAdapter.onGroupItemCick,
    DepartmentEmployeeAdapter.onDeptMenuClick, ManagerAttendanceAdapter.onAttendanceClick,
    MyBroadcastReceiver.MyListener {
    var outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
    private var moreThanOne: Boolean = false
    val xAxisValues: ArrayList<String> = ArrayList()
    var isStatisticsGraph: Boolean = true
    private val NOTIFICATION_SETTINGS_REQUEST_CODE = 678
    private var isLateSittingCheckout:Boolean = false
    private var forgottocheckout:Boolean = true
    //    private var formattedFirstCheckin:LocalTime?=null
//    private var formattedLastCheckout:LocalTime?=null
    private var isLateSittingEnabled:Boolean = false
    private var userSelectedTime:String=""
    private var isUserLateSitting:Boolean = false
    private var currentTime:String=""
    private var forgottocheckoutdate:String=""
    private var userWorkingHour:Double=0.000
    private var forgotToCheckout:Boolean=false
    private var characterLimit:Int=0
    private var selectedCheckOutTime:TextView?=null
    private var SPAN_COUNT = 0
    private var selectedcheckouttime:TextView?=null
    var isdaysFive: Boolean = true
    private var departmentgrouplist: ArrayList<DepartmentGroup> = ArrayList()
    private var presentcount: Int = 0
    private var leavescount: Int = 0
    private var latearrivalcount: Int = 0
    private var absentcount: Int = 0
    private var managerview: Boolean = true
    private var showAttendanceProgressBar: Boolean = true
    private var empDesignation: String = ""
    lateinit var checkdate: org.threeten.bp.LocalDate
    var deptdataresponselist: List<BodyXX>? = null
    var addeddataresponselist: ArrayList<BodyXX>? = ArrayList()
    private var checkInTime: String = ""
    private var email: String? = null
    private var checkOutTime: String = ""
    private var isLateSttingEnabled:Boolean = false
    private var empStatusCheckIn: String = ""
    private var empStatusCheckOut: String = ""
    lateinit var currentDateCto: org.threeten.bp.LocalDate
    private var entriesList: List<CtoAndManagerModelresponceSummery>? = null
    private var employeedetailresponces: List<EmployeedetailModelresponces>? = null
    var currentClick = ""
    fun getCurentDate(): org.threeten.bp.LocalDate {
        return LocalDate.now()
    }
    private var officeLat: String = ""
    private var officeLong: String = ""
    private var employeeLocation: String = ""
    private var employeeRecievedLocation: String = ""
    private var isLateSittingCheckIn:Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userComment: String = ""
    private var comment: EditText? = null
    private var status: String = ""
    private var empstatus:String=""
    private var firstCheckInStatus: String = ""
    private var lastCheckOutStatus: String = ""
    private var lastActivty: String = ""
    private var appliedDaysStatus: Boolean = false
    private var longitude: String = ""
    private var recievedTodate: String = ""
    private var recievedFromdate: String = ""
    private var latitude: String = ""
    private val permissionId = 2
    private var empId: String = ""
    private var hideProgressDialog: Boolean = true
    var leaveslist = ArrayList<leavesdata>()
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    val f1: SimpleDateFormat =
        SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)
    val f2: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

    // private val homeViewModel: HomeViewModel by viewModels()
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    var txtdate: TextView? = null

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    var formatteddateWithDashes = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
    var dateforstatisticsgraph = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
    var startdateforattendancegraph = ""
    var enddateforattendancegraph = ""
    var departmentid: ArrayList<Int> = ArrayList()
    var designationid: ArrayList<Int> = ArrayList()
    var employeeID: Int = 0
    var genderid: ArrayList<Int> = ArrayList()
    var groupid: ArrayList<Int> = ArrayList()
    var attendanceid: Int = 0
    var officeid: ArrayList<Int> = ArrayList()
    var isStatistics: Boolean = true
    private var sessionmanagement: SessionManagement? = null

    private var _binding: FragmentHomeBinding? = null

    //use binding instance to avoid null check in every where in statement
    private val binding get() = _binding!!
    private val mDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
    var fromDate: String = ""
    var toDate: String = ""
    var noOfDays = ""
    var officesLocationResponse: ArrayList<SettingApiResponse.Location?> = arrayListOf()
    var attendanceTypeOffice: ArrayList<SettingApiResponse.AttendanceType?> = arrayListOf()
    private var receiver: MyBroadcastReceiver? = null
    var filterDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDateCto = getCurentDate()
        checkdate = getCurentDate()
        onClickObserver()
        onClickListeners()
        getCurrentWeekDate()

//        initApproveRejectBottomSheet()
    }
    private fun getCurrentWeekDate() {
        // Set the calendar to the current day
        calendar.time = Date()
        val currentDate = Calendar.getInstance().time
        // Find the first day of the current week (Monday)
        val currentDateString = dateFormat.format(currentDate)
        enddateforattendancegraph = currentDateString

        // Get the date of 5 days ago
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val fifthDayAgoDate = calendar.time
        val fifthDayAgoDateString = dateFormat.format(fifthDayAgoDate)
        startdateforattendancegraph = fifthDayAgoDateString
        var parsedate = inputdateformat.parse(fifthDayAgoDateString)
        var formateddatefifthdayago = outputDateFormats.format(parsedate)
        binding.txtselectedfromattendancedate.text = formateddatefifthdayago
        var parsedates = inputdateformat.parse(currentDateString)
        var currentddateFormated = outputDateFormats.format(parsedates)
        binding.txtselectedtoattendancedate.text = currentddateFormated
    }

    private fun getCurrentMonthDate() {
        val currentDate = LocalDate.now()
        val thirtyDaysAgoDate = LocalDate.now().minusDays(30)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDateString = currentDate.format(formatter)
        enddateforattendancegraph = currentDateString
        val thirtyDaysAgoDateString = thirtyDaysAgoDate.format(formatter)
        startdateforattendancegraph = thirtyDaysAgoDateString

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root
        //getCurrentLocation()
        currentDateCto = getCurentDate()
        checkdate = getCurentDate()
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        hideProgressDialog = true
        showAttendanceProgressBar = true
        isLateSttingEnabled = false
        filterDialog = null
        clearAllIds()
        sessionmanagement = SessionManagement(requireContext())
        employeeID = sessionmanagement!!.getEmpId().toInt()
        changingDashBoardView()
        getDashboardInformation(view)
        getEmployeeDesignation()
        refreshDashboardOnSwipe()
        initEvent()
        isLateSittingCheckout = false
        receiver = MyBroadcastReceiver(this)
        val filter = IntentFilter()
        filter.addAction("com.appinsnap.aishrm") // Customize the action to match your needs
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver!!, filter)
        return binding.root
    }

    private fun initEvent() {
        callSettingApi()
        with(binding) {
            checkInCheckOutBtn.setSafeOnClickListener {
                isLateSittingCheckIn = sessionmanagement?.getLateSittingStatus()!!
                if (lastActivty.lowercase().contains("checkin")) {
                    status = "CheckOut"
                    showCheckInCheckOutDialog(false, "Check-out")
                }
                else if(lastActivty.lowercase().contains("checkout")&&isLateSittingCheckIn)
                {
                    status = "CheckOut"
                    showCheckInCheckOutDialog(false, "Check-out")
                }
                else
                {
                    status = "CheckIn"
                    userSelectedTime = ""
                    forgotToCheckout = false
                    checkIn()
                }
            }
        }
    }
    private fun callSettingApi() {
        val settingRequest = SettingApiRequest().apply {
            appVersion = requireActivity().getAppVersionName() ?: ""
            deviceModel = "Android"
            deviceName = getDeviceName()
            deviceToken = fToken
            deviceVersion = getDeviceVersion()
        }
        homeViewModel.getDashboardSetting(settingRequest)
    }

    private fun getDeviceModel() = Build.MODEL
    private fun getDeviceName() = Build.MANUFACTURER + " " + Build.MODEL
    private fun getDeviceVersion() = Build.VERSION.RELEASE
    private fun setDashboardTiles(dashboardTiles: List<SettingApiResponse.DashboardTile?>) {
        SPAN_COUNT = if (dashboardTiles.size <= 3) 1 else 2
        val layoutManager = GridLayoutManager(
            requireContext(), SPAN_COUNT,
            GridLayoutManager.HORIZONTAL, false
        )
        binding.rvleaves.layoutManager = layoutManager


        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density

        /*  val snapHelper: SnapHelper = PagerSnapHelper()
          snapHelper.attachToRecyclerView(binding.rvleaves)
  */
        val adapter = LeavesAdapter(requireContext(), dashboardTiles) { data ->
            onclick(data)

        }

        with(binding) {
            rvleaves.show()
            rvleaves.apply {
                this.adapter = adapter
            }
        }
    }

    private fun refreshDashboardOnSwipe() {
        var viewid = sessionmanagement?.getViewId()
        binding.swipelayout.setOnRefreshListener {
            hideProgressDialog = false
            if (viewid == 3) {
                callWeekAttendanceApi()
            }
            if (viewid == 2) {
                currentDateCto = LocalDate.now()
                val formatteddate = formatDate(currentDateCto)
                binding.departmenttxtcheck.text = formatteddate.toString()
                binding.rightarrow.setColorFilter(requireContext().resources.getColor(R.color.light_gray))
                managerview = true
                callManagerDasboardApi()
            }
            if (viewid == 1) {
                if (isStatisticsGraph == true) {
                    callStaticticsGraphApi()
                } else {
                    callAttendanceGraphApi()
                }
                currentDateCto = LocalDate.now()

                val formatteddate = formatDate(currentDateCto)
                binding.departmenttxtcheck.text = formatteddate.toString()
                managerview = false
                callManagerDasboardApi()
            }
            if (viewid == 4) {
                currentDateCto = LocalDate.now()
                val formatteddate = formatDate(currentDateCto)
                binding.departmenttxtcheck.text = formatteddate.toString()
                managerview = true
                callManagerDasboardApi()
            }
            callSettingApi()
            callDashboardApi()
        }
    }

    private fun getEmployeeDesignation() {
        try {
            empDesignation = sessionmanagement!!.getDesignation()
            var viewid = sessionmanagement!!.getViewId()
            binding.txtdesignation.text = empDesignation
            if (viewid == 3) {
                binding.txtdesignation.text = empDesignation
            }
            if (viewid == 2) {
                if(empDesignation.length>20)
                {
                    var truncateddesignation = empDesignation.substring(0,25)
                    var finalTruncatedDesignation = truncateddesignation +" "+ "..."
                    binding.txtdesignation.text = finalTruncatedDesignation + " " + "-"
                }
                else{
                    binding.txtdesignation.text = empDesignation + " " + "-"
                }
            }
            if (viewid == 4) {
                if(empDesignation.length>20)
                {
                    var truncateddesignation = empDesignation.substring(0,25)
                    var finalTruncatedDesignation = truncateddesignation +" "+"..."
                    binding.txtdesignation.text = finalTruncatedDesignation + " " + "-"
                }
                else{
                    binding.txtdesignation.text = empDesignation + " " + "-"
                }
            }
            if (viewid == 1) {
                if(empDesignation.length>20)
                {
                    var truncateddesignation = empDesignation.substring(0,25)
                    var finalTruncatedDesignation = truncateddesignation+" "+"..."
                    binding.txtdesignation.text = finalTruncatedDesignation + " " + "-"
                }
                else{
                    binding.txtdesignation.text = empDesignation + " " + "-"
                }
            }
        }
        catch (e: Exception) {

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //  showProgressDialog(it)
            if (!hideProgressDialog) {

            }

            else {
                if (showAttendanceProgressBar) {
                    showProgressDialog(it)
                    hideProgressDialog = true
                }
            }
        })

        homeViewModel._weekattendanceresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        markingWeeklyAttendance(response)
                        binding.mcvattendanceweek.show()
                        binding.imgnext.show()
                    }

                    is NetworkResult.Error -> {
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })
        homeViewModel._latesittingcheckin.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        showProgressDialog(false)
//                        lastActivty = "CheckIn"
//                        status = "CheckOut"
//                        sessionmanagement?.setLateSittingStatus(true)
                        hideProgressDialog = false
                        callDashboardApi()
                        if(response.data?.body!=null)
                        {
                            var responselatesitting = response.data?.body
                            var checkincheckouttext = responselatesitting.toString().replace("[", "").replace("]", "")
                            binding.checkincheckout.text = checkincheckouttext
                        }

                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.data?.statusMessage}",
                            icon = R.drawable.tickicon
                        )
                    }
                    is NetworkResult.Error -> {
                        showProgressDialog(false)

                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            icon = R.drawable.tickicon
                        )

                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })

        homeViewModel._latesittingstatusresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        if(response.data?.body!=null)
                        {
                            isLateSittingCheckIn =  sessionmanagement!!.getLateSittingStatus()
                            if(response.data.body.islateSetting!=null)
                            {
                                isUserLateSitting = response.data?.body!!.islateSetting
                                if(lastActivty.contains("CheckOut"))
                                {
                                    if(isUserLateSitting&&isLateSittingCheckIn==false)
                                    {
                                        showProgressDialog(true)
                                        callLateSittingCheckIn()
                                    }
                                    else if(isUserLateSitting&&isLateSittingCheckIn==true)
                                    {
                                        checkOut()
                                    }
                                    else{
                                        checkLocation()
                                    }
                                }
                                else{
                                    checkOut()
                                }
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        showProgressDialog(false)
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.data?.statusMessage}",
                            icon = R.drawable.reject
                        )
                    }

                    is NetworkResult.Loading -> {

                    }
                }
            })

        homeViewModel._managerctodashboardresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        if (managerview) {
                            markingManagerDepartmentAttendance(response)
                        } else {
                            displayDepartmentAttendance(response)
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.txtnorecord.visibility = View.VISIBLE
                    }
                    is NetworkResult.Loading -> {

                    }
                }
            })

        homeViewModel._attendanceresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        forgottocheckout = true
                        //    displayCheckInOutStatus()
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.data?.statusMessage}",
                            icon = R.drawable.tickicon
                        )
                        sessionmanagement?.setLateSittingStatus(false)
                        hideProgressDialog = false
                        callDashboardApi()
                        var viewId = sessionmanagement?.getViewId()
                        if (viewId == 3) {
                            callWeekAttendanceApi()
                        }
                        if (viewId == 2) {
                            managerview = true
                            callManagerDasboardApi()
                        }
                        if (viewId == 1) {
                            managerview = false
                            callManagerDasboardApi()
                        }
                        if (viewId == 4) {
                            managerview = true
                            callManagerDasboardApi()
                        }


                        showProgressDialog(false)

                        userComment = ""
                        employeeLocation = ""
                        fromDate = ""
                        toDate = ""
                        noOfDays = ""
                    }

                    is NetworkResult.Error -> {
                        hideProgressDialog = false
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            location = this,
                            icon = R.drawable.erroricon
                        )
                        callDashboardApi()
                        showProgressDialog(false)
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })
        homeViewModel._dashboardSettingResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressDialog = false
                    showProgressDialog(false)
                    response.data?.body?.let { responseBody ->
                        responseBody.dashboardTiles?.let {
                            setDashboardTiles(it)
                        }
                        responseBody.promotionalImages?.let {
                            setupSlidShow(it)
                        }
                        responseBody.locations?.let { list ->
                            //setOfficesLocation(list)
                            officesLocationResponse.clear()
                            officesLocationResponse =
                                list as ArrayList<SettingApiResponse.Location?>
                            val gson = Gson()
                            val response = gson.toJson(responseBody.locations)
                            SessionManagement(requireContext()).setLocations(response)
                        }
                        responseBody.attendanceTypes?.let {
                            attendanceTypeOffice.clear()
                            attendanceTypeOffice =
                                it as ArrayList<SettingApiResponse.AttendanceType?>
                        }

                        responseBody.categories.let {
                            val gson = Gson()
                            val response = gson.toJson(responseBody.categories)
                            SessionManagement(requireContext()).setCategories(response)
                        }
                    }
                    LoggerGenratter.getInstance()
                        .printLog("HOME FRAGMENT", "onClickObserver: $response")
                }

                is NetworkResult.Error -> {

                    if (response.message == "203") {
                        showForceUpdateDialog()
                    } else if (response.message == "401") {
                        sessionmanagement?.clearPreference()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    } else {
                        hideProgressDialog = false
                        requireActivity().showDialogWithSingleClicked(
                            okBtnText = "Retry",
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            icon = R.drawable.erroricon,
                            //   setCancelAble = false,
                            click = object : DialogClicked {
                                override fun onclick() {
                                    var viewid = sessionmanagement?.getViewId()
                                    callSettingApi()
                                    hideProgressDialog = false
                                    callDashboardApi()
                                    if (viewid == 3) {
                                        callWeekAttendanceApi()
                                    }
                                    if (viewid == 2) {
                                        managerview = true
                                        callManagerDasboardApi()
                                    }
                                    if (viewid == 1) {
                                        managerview = false
                                        callManagerDasboardApi()
                                    }
                                    if (viewid == 4) {
                                        managerview = true
                                        callManagerDasboardApi()
                                    }
                                }
                            }
                        )
                        showProgressDialog(false)
                    }


                }

                is NetworkResult.Loading -> {

                }
            }

        }

        homeViewModel._getGraphFilter.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showFilterDialog(response.data)
                }

                is NetworkResult.Error -> {
                    binding.txtnorecord.visibility = View.VISIBLE
                }

                is NetworkResult.Loading -> {
                }
            }
        }

        homeViewModel._getStatisticsGraph.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {

                    response.data?.body?.ctoAndManagerModelresponceSummery?.let {
                        binding.piechartprogressbar.hide()
                        entriesList = it
                        if (!entriesList.isNullOrEmpty()) {
                            entriesList!![0].aIndex = 100
                            entriesList!![0].pIndex = 100
                            entriesList!![0].leaIndex = 100
                        }
                        initPieChart(it)

                    }

                    response.data?.body?.employeedetailModelresponces?.let {
                        employeedetailresponces = it
                    }
                }

                is NetworkResult.Error -> {
                    binding.piechartprogressbar.hide()
                    binding.totalgraphtext.visibility = View.VISIBLE
                    binding.totalgraphtext.text = "No data found"
                }
                is NetworkResult.Loading -> {
                }
            }
        }

        homeViewModel._getAttendanceGraph.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.linechartprogressbar.hide()
                    response.data?.body?.ctoAndManagerModelresponceSummery?.let {
                        if (it.isNotEmpty()) {
                            initBarGraph(it)
                        }
                        //    initlinegraph(it)
                    }
                    showProgressDialog(false)
                }

                is NetworkResult.Error -> {
                    binding.linechartprogressbar.hide()
                    //  binding.linechart.hide()
                    binding.txtatthendancegraphrecord.show()
                    binding.txtatthendancegraphrecord.text = "No data found"
                }

                is NetworkResult.Loading -> {
                    showProgressDialog(false)
                }
            }
        }
    }

    private fun callLateSittingCheckIn() {
        showProgressDialog(true)
        var request = sessionmanagement?.let { LateSittingCheckInRequest(employeeID = it.getEmpId(), latitude = latitude.toDouble(), longitude = longitude.toDouble()) }
        CoroutineScope(Dispatchers.Main).launch {
            if (request != null) {
                homeViewModel.getLateSittingCheckIn(request)
            }
        }
    }

    private fun initBarGraph(ctoAndManagerModelresponceSummeries: List<CtoAndManagerModelresponceSummery>) {
        val presentlist: ArrayList<BarEntry> = ArrayList()
        val absentlist: ArrayList<BarEntry> = ArrayList()
        val leavelist: ArrayList<BarEntry> = ArrayList()
        val latearrivallist: ArrayList<BarEntry> = ArrayList()
        if (presentlist.isNotEmpty()) {
            presentlist.clear()
        }
        if (absentlist.isNotEmpty()) {
            absentlist.clear()
        }
        if (leavelist.isNotEmpty()) {
            leavelist.clear()
        }
        if (latearrivallist.isNotEmpty()) {
            latearrivallist.clear()
        }
        if (xAxisValues.isNotEmpty()) {
            xAxisValues.clear()
        }
        if (ctoAndManagerModelresponceSummeries.isNullOrEmpty()) {
            binding.txtatthendancegraphrecord.show()
            binding.txtatthendancegraphrecord.text = "No data found"
        } else {
            try {
                binding.barChart.show()
                binding.txtatthendancegraphrecord.hide()
                for (i in ctoAndManagerModelresponceSummeries.indices) {
                    xAxisValues.add(
                        ctoAndManagerModelresponceSummeries[i].attendanceDate!!.substring(
                            8,
                            10
                        )
                    )
                    presentlist.add(
                        BarEntry(
                            i.toFloat(),
                            ctoAndManagerModelresponceSummeries[i].presentCount!!.toFloat()
                        )
                    )
                    absentlist.add(
                        BarEntry(
                            i.toFloat(),
                            ctoAndManagerModelresponceSummeries[i].absentCount!!.toFloat()
                        )
                    )
                    leavelist.add(
                        BarEntry(
                            i.toFloat(),
                            ctoAndManagerModelresponceSummeries[i].onLeaveCount!!.toFloat()
                        )
                    )
                    latearrivallist.add(
                        BarEntry(
                            i.toFloat(),
                            ctoAndManagerModelresponceSummeries[i].lateCount!!.toFloat()
                        )
                    )
                    val presentdataset = BarDataSet(presentlist, "Present:")
                    presentdataset.color =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].presentColor)
                    presentdataset.setDrawValues(false)
                    presentdataset.barBorderColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].presentColor)
                    presentdataset.highLightColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].presentColor)
                    presentdataset.valueTextSize = 10f
                    presentdataset.barBorderWidth = 1f
                    presentdataset.valueTextColor = Color.BLACK
                    val absentdataset = BarDataSet(absentlist, "Absent:")
                    absentdataset.color =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].absentColor)
                    absentdataset.setDrawValues(false)
                    absentdataset.barBorderColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].absentColor)
                    absentdataset.highLightColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].absentColor)
                    absentdataset.valueTextSize = 10f
                    absentdataset.barBorderWidth = 1f
                    absentdataset.valueTextColor = Color.BLACK
                    val leavedataset = BarDataSet(leavelist, "Leave:")
                    leavedataset.color =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].leaveColor)
                    leavedataset.setDrawValues(false)
                    leavedataset.barBorderColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].leaveColor)
                    leavedataset.highLightColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].leaveColor)
                    leavedataset.valueTextSize = 10f
                    leavedataset.barBorderWidth = 1f
                    leavedataset.valueTextColor = Color.BLACK
                    val latearrivaldataset = BarDataSet(latearrivallist, "Late:")
                    latearrivaldataset.color =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].lateColor)
                    latearrivaldataset.setDrawValues(false)
                    latearrivaldataset.barBorderColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].lateColor)
                    latearrivaldataset.highLightColor =
                        Color.parseColor(ctoAndManagerModelresponceSummeries[i].lateColor)
                    latearrivaldataset.valueTextSize = 10f
                    latearrivaldataset.valueTextColor = Color.WHITE
                    var barData = BarData(presentdataset, absentdataset, leavedataset, latearrivaldataset)
                    barData.setBarWidth(0.2F)
                    binding.barChart.data = barData
                    binding.barChart.invalidate()
                    binding.barChart.setExtraOffsets(0f, 0f, 0f, 15f)
                    binding.barChart.setDrawMarkers(true)
                    binding.barChart.setDrawBarShadow(false)
                    binding.barChart.description.isEnabled = false
                    binding.barChart.animateY(1200)
                    binding.barChart.setDrawGridBackground(false)
                    binding.barChart.getAxisRight().setEnabled(false)
                    binding.barChart.getDescription().setEnabled(false)
                    binding.barChart.getLegend().setEnabled(false)
                    val xAxis: XAxis = binding.barChart.xAxis
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.isEnabled = true
                    xAxis.spaceMin = barData.barWidth / 0.15f
                    xAxis.spaceMax = barData.barWidth / 0.15f
                    xAxis.setCenterAxisLabels(true)
                    xAxis.setDrawGridLines(false)
                    xAxis.setDrawAxisLine(true)
                    xAxis.setDrawGridLines(false)
                    xAxis.setDrawLabels(true)
                    val barSpace = 0.0f
                    val groupSpace = 0.2f
                    xAxis.setAxisMinimum(0f)
                    binding.barChart.animate()
                    binding.barChart.groupBars(0f, groupSpace, barSpace)
                    binding.barChart.axisLeft.isEnabled = true
                    binding.barChart.axisRight.isEnabled = false
                    binding.barChart.xAxis.yOffset = 7f
                    binding.barChart.axisLeft.setDrawAxisLine(true)
                    binding.barChart.axisLeft.setDrawGridLines(false)
                    binding.barChart.getAxisLeft().setAxisMinimum(0f)
                    binding.barChart.setTouchEnabled(true)
                    // enable scaling and dragging
                    binding.barChart.setScaleEnabled(true)
                    binding.barChart.xAxis.textColor = Color.WHITE
                    binding.barChart.axisLeft.textColor = Color.WHITE
                    binding.barChart.isDragEnabled = true
                    binding.barChart.animateX(700)
                    val myMarkerView =
                        MyMarkerView(context, barData.dataSetLabels, R.layout.custom_marker_view)
                    myMarkerView.chartView = binding.barChart
                    binding.barChart.marker = myMarkerView
                    binding.barChart.setFitBars(true)
                    binding.barChart.xAxis.labelCount = barData.entryCount // important
                    binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
                    binding.barChart.xAxis.isGranularityEnabled = true
                    binding.barChart.xAxis.granularity = 1f
                }
            } catch (e: Exception) {

            }
        }
    }

    /* private fun initlinegraph(body: List<CtoAndManagerModelresponceSummery>) {

    binding.linechart!!.xAxis.valueFormatter = null
    binding.linechart!!.invalidate()

    val statuslist: ArrayList<ILineDataSet> = ArrayList()
    val presentlist: ArrayList<Entry> = ArrayList()
    val absentlist: ArrayList<Entry> = ArrayList()
    val leaveList: ArrayList<Entry> = ArrayList()
    val latearrivallist: ArrayList<Entry> = ArrayList()
    val xAxisValues: ArrayList<String> = ArrayList()

    if(body.isNullOrEmpty())
    {
        binding.txtatthendancegraphrecord.show()
        binding.txtatthendancegraphrecord.text = "No data found"

    }


    try {
        binding.linechart.show()
        binding.txtatthendancegraphrecord.hide()
        for (i in body.indices) {
            xAxisValues.add(body[i]?.attendanceDate!!.substring(8, 10))
            val presententry =
                body[i]?.presentCount?.toFloat()!! - body[i]?.lateCount!!.toFloat()
            presentlist.add(Entry(i.toFloat(), presententry.toFloat()))
            absentlist.add(Entry(i.toFloat(), body[i]?.absentCount!!.toFloat()))
            leaveList.add(Entry(i.toFloat(), body[i]?.onLeaveCount!!.toFloat()))
            latearrivallist.add(Entry(i.toFloat(), body[i]?.lateCount!!.toFloat()))
            val presentdataset = LineDataSet(presentlist, "")
            presentdataset.circleRadius = 1f
            presentdataset.setDrawCircleHole(false)
            presentdataset.setDrawCircles(false)
            presentdataset.color = Color.parseColor(body[i]?.presentColor.toString())
            presentdataset.lineWidth = 2f
            presentdataset.valueTextSize = 10f
            presentdataset.valueTextColor = Color.BLACK
            presentdataset.setDrawHorizontalHighlightIndicator(false)
            presentdataset.setDrawVerticalHighlightIndicator(false);
            statuslist.add(presentdataset)
            val absentdataset = LineDataSet(absentlist, "")
            absentdataset.circleRadius = 1f
            absentdataset.setDrawCircleHole(false)
            absentdataset.setDrawCircles(false)
            absentdataset.color = Color.parseColor(body[i]?.absentColor.toString())
            absentdataset.lineWidth = 2f
            absentdataset.valueTextSize = 10f
            absentdataset.valueTextColor = Color.BLACK
            absentdataset.setDrawHorizontalHighlightIndicator(false)
            absentdataset.setDrawVerticalHighlightIndicator(false);
            statuslist.add(absentdataset)
            val leavedataset = LineDataSet(leaveList, "")
            leavedataset.circleRadius = 1f
            leavedataset.setDrawCircleHole(false)
            leavedataset.setDrawCircles(false)
            leavedataset.color = Color.parseColor(body[i]?.leaveColor.toString())
            leavedataset.lineWidth = 2f
            leavedataset.valueTextSize = 10f
            leavedataset.valueTextColor = Color.BLACK
            leavedataset.setDrawHorizontalHighlightIndicator(false)
            leavedataset.setDrawVerticalHighlightIndicator(false);
            statuslist.add(leavedataset)
            val latearrivaldataset = LineDataSet(latearrivallist, "")
            latearrivaldataset.circleRadius = 1f
            latearrivaldataset.setDrawCircleHole(false)
            latearrivaldataset.setDrawCircles(false)
            latearrivaldataset.color = Color.parseColor(body[i]?.lateColor.toString())
            latearrivaldataset.lineWidth = 2f
            latearrivaldataset.valueTextSize = 10f
            latearrivaldataset.valueTextColor = Color.BLACK
            latearrivaldataset.setDrawHorizontalHighlightIndicator(false)
            latearrivaldataset.setDrawVerticalHighlightIndicator(false);
            statuslist.add(latearrivaldataset)
            val data = LineData(statuslist)
            binding.linechart!!.setExtraOffsets(0f, 15f, 0f, 15f)
            data.setDrawValues(false)
            binding.linechart!!.setDrawMarkers(true)
            val myMarkerView =
                MyMarkerView(requireContext(), data.dataSetLabels, R.layout.custom_marker_view)
            myMarkerView.chartView = binding.linechart
            binding.linechart.marker = myMarkerView
            binding.linechart!!.data = data
            binding.linechart!!.xAxis.labelCount = data.entryCount
        }
    } catch (e: Exception) {

    }

    binding.linechart.xAxis.setAvoidFirstLastClipping(true)
    binding.linechart.getAxisRight().setEnabled(false)
    binding.linechart.getDescription().setEnabled(false)
    binding.linechart.getLegend().setEnabled(false)
    binding.linechart.axisLeft.setDrawLabels(true)
    binding.linechart!!.getAxisLeft().setAxisMinimum(0f)
    binding.linechart!!.xAxis.isGranularityEnabled = true
    if(isdaysFive){
        binding.linechart!!.xAxis.granularity = 1f
    }
    else{
        binding.linechart!!.xAxis.granularity = 2f
    }

    binding.linechart.axisLeft.granularity = 1f
    binding.linechart.axisLeft.axisMinimum = 0f
    binding.linechart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    binding.linechart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
    binding.linechart.xAxis.textColor = resources.getColor(R.color.white)
    binding.linechart.axisLeft.textColor = resources.getColor(R.color.white)
//       binding.linechart.axisLeft.valueFormatter=IndexAxisValueFormatter(yAxisValue)
    binding.linechart.animateY(1000)
//        binding.linechart.getAxisLeft().setAxisMinimum(0f)
    binding.linechart.xAxis.setDrawGridLines(false)
    binding.linechart.axisLeft.setDrawGridLines(false)
    binding.linechart.highlightValue(null)
    binding.linechart.setTouchEnabled(true)
    binding.linechart.setDrawMarkers(true)
    binding.linechart.setPinchZoom(true)
    binding.linechart.setScaleEnabled(true)
    binding.linechart.dragDecelerationFrictionCoef = 1f
    binding.linechart!!.invalidate()
}*/
    private fun initPieChart(body: List<CtoAndManagerModelresponceSummery>) {
        binding.pieChart.hide()
        binding.pieChart.invalidate()
        binding.pieChart.setUsePercentValues(false)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(25f, 0f, 25f, 0f)
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f)
        binding.pieChart.setDrawHoleEnabled(true)
        binding.pieChart.setHoleColor(activity?.resources!!.getColor(R.color.theme_wala_kala))
        binding.pieChart.setTransparentCircleAlpha(110)
        binding.pieChart.setHoleRadius(70f)
        binding.pieChart.setTransparentCircleRadius(70f)
        binding.pieChart.setDrawEntryLabels(true)
        binding.pieChart.setRotationAngle(0f)
        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.setRotationEnabled(false)
        binding.pieChart.setHighlightPerTapEnabled(true)
        binding.pieChart.animateY(1000, Easing.EaseOutBack)
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.setEntryLabelColor(R.color.white)
        binding.pieChart.setMinAngleForSlices(7f)
        val entries: ArrayList<PieEntry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()

        if (body.isNullOrEmpty()) {
            binding.totalgraphtext.visibility = View.VISIBLE
            binding.totalgraphtext.text = "No data found"
            binding.pieChart.setDrawCenterText(false)
        } else {
            binding.pieChart.show()
            body[0]?.let {

                if (it.absentCount!! > 0) {
                    entries.add(PieEntry(it.absentCount!!.toFloat()))
                    colors.add(Color.parseColor(it.absentColor.toString()))
                    entriesList?.get(0)?.aIndex = entries.size - 1
                }

                if (it.presentCount!! > 0 || it.lateCount!! > 0) {
                    entries.add(PieEntry(it.presentCount!!.toFloat() + it.lateCount!!.toFloat()))
                    colors.add(Color.parseColor(it.presentColor.toString()))
                    entriesList?.get(0)?.pIndex = entries.size - 1
                }

                if (it.onLeaveCount!! > 0) {
                    entries.add(PieEntry(it.onLeaveCount!!.toFloat()))
                    colors.add(Color.parseColor(it.leaveColor.toString()))
                    entriesList?.get(0)?.leaIndex = entries.size - 1
                }

                binding.totalgraphtext.visibility = View.GONE
                if (it.totalEmp != 0) {
                    binding.pieChart.centerText = it.totalEmp.toString()
                    binding.pieChart.setCenterTextColor(Color.WHITE)
                    binding.pieChart.setCenterTextSize(12f)
                    binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    binding.totalgraphtext.visibility = View.VISIBLE
                    binding.totalgraphtext.text = "No data found"
                    binding.pieChart.setDrawCenterText(false)
                }
            }

        }
//
//        val nonZeroEntries: MutableList<PieEntry> = ArrayList()
//
//        for (entry in entries) {
//            if (entry.value != 0f) {
//                nonZeroEntries.add(entry)
//            }
//        }


        val dataSet = PieDataSet(entries, "Mobile OS")
        // on below line we are setting colors.
        dataSet.colors = colors

        dataSet.sliceSpace = 1f  // Increase slice spacing
        dataSet.sliceSpace = 4f  // Increase slice spacing
        dataSet.selectionShift = 5f
        dataSet.valueLineWidth = 1f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.3f
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 85f
        dataSet.setValueLineColor(Color.WHITE)// Value line color

        val data = PieData(dataSet)
        dataSet.valueFormatter = IntValueFormatter()
//        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(12f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        data.isHighlightEnabled = true
        binding.pieChart.data = data
        binding.pieChart.highlightValues(null)
        binding.pieChart.notifyDataSetChanged()
        binding.pieChart.invalidate()
    }

    class IntValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }

    private fun hidepieChart() {
        binding.pieChart.clear()
        binding.pieChart.hide()
        binding.totalgraphtext.hide()
    }

    fun showFilterDialog(response: FilterResponse?) {
        if (filterDialog == null) {
            val binding = ShowfiltersdialogBinding.inflate(layoutInflater)
            filterDialog = Dialog(requireActivity(), androidx.transition.R.style.Theme_AppCompat_Dialog)
            filterDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            filterDialog!!.setContentView(binding.getRoot())
            with(binding) {
                val adapter = FilterAdapter(requireContext(), response!!.body) { list ->
                    clearAllIds()

                    for (i in list.indices) {
                        when (list[i].type) {
                            "Department" -> {
                                for (j in list[i].lov.indices)
                                    if (list[i].lov[j].check)
                                        departmentid.add(list[i].lov[j].id)
                            }

                            "Designation" -> {
                                for (j in list[i].lov.indices)
                                    if (list[i].lov[j].check)
                                        designationid.add(list[i].lov[j].id)
                            }

                            "Office" -> {
                                for (j in list[i].lov.indices)
                                    if (list[i].lov[j].check)
                                        officeid.add(list[i].lov[j].id)
                            }

                            "Vertical" -> {
                                for (j in list[i].lov.indices)
                                    if (list[i].lov[j].check)
                                        groupid.add(list[i].lov[j].id)
                            }

                            "AttendanceStatus" -> {
                                attendanceid = 0
                            }

                            "Gender" -> {
                                for (j in list[i].lov.indices)
                                    if (list[i].lov[j].check)
                                        genderid.add(list[i].lov[j].id)

                            }

                            else -> {}
                        }
                    }
                }

                tvApplyFilter.setSafeOnClickListener {
                    if (true) {
                        filterDialog!!.hide()
                        if (isStatistics) {
                            hidepieChart()
                            callStaticticsGraphApi()
                        } else {
                            hidebarchart()
                            callAttendanceGraphApi()
                        }
                    } else {

                    }
                }
                tvcancel.setSafeOnClickListener {
                    clearAllIds()
                    if (isStatistics) {
                        hidepieChart()
                        callStaticticsGraphApi()
                    } else {
                        // hidelinegraph()
                        callAttendanceGraphApi()
                    }
                    filterDialog!!.dismiss()
                    filterDialog = null
                }
                imgcancel.setSafeOnClickListener {
                    filterDialog!!.hide()
                }
                if (response.body.size >= 4) {
                    applyfilterrecyclerview.layoutParams.height = 1200
                }
                else {
                    applyfilterrecyclerview.layoutParams.height = 600
                }
                applyfilterrecyclerview.adapter = adapter
            }

            filterDialog!!.show()
        } else {
            filterDialog!!.show()
        }
    }

    private fun hidebarchart() {
        binding.barChart.hide()
        binding.barChart.clear()
    }

//    private fun hidelinegraph() {
//        binding.linechart.hide()
//        binding.linechart.clear()
//    }

    private fun clearAllIds() {
        departmentid.clear()
        designationid.clear()
        officeid.clear()
        groupid.clear()
        attendanceid = 0
        genderid.clear()
    }

    private fun displayDepartmentAttendance(response: NetworkResult.Success<CTOManagerDashboardResponse>) {
        deptdataresponselist = response.data?.body
        if (response != null) {
            var list: List<BodyXX>? = response.data?.body
            var result = list?.distinctBy { it.group }
            if (result != null) {
                if (departmentgrouplist.isEmpty()) {
                    for (i in result) {
                        departmentgrouplist?.add(DepartmentGroup(i.group))
                        departmentgrouplist.reverse()
                    }
                }
            }
            if (departmentgrouplist.size <= 1) {
                binding.dept.visibility = View.GONE
            } else {
                binding.dept.visibility = View.VISIBLE
            }
            var groupname = departmentgrouplist
            var string = ""
            val adapter = DepartmentGroupAdapter(requireContext(), groupname, this)
            binding.rvctodept.adapter = adapter
            binding.rvctodept.scrollToPosition(Constants.SELECTEDdpt)
            onGroupClick(Constants.SELECTEDdpt)
        } else {
            binding.txtnorecord.visibility = View.VISIBLE
        }
    }

    private fun markingManagerDepartmentAttendance(response: NetworkResult.Success<CTOManagerDashboardResponse>) {
        val list: ArrayList<BodyXX>? = response.data?.body as ArrayList<BodyXX>
        var attendancelist: ArrayList<BodyXX> = ArrayList()

        if (list != null) {
            attendancelist.add(
                BodyXX(
                    list.get(0).absentCount,
                    list.get(0).departmentName,
                    list.get(0).group,
                    list.get(0).lateCount,
                    list.get(0).leaveCount,
                    list.get(0).presentCount,
                    list.get(0).departmentId
                )
            )

            var departmentid = list?.get(0)?.departmentId
            sessionmanagement?.setDepartmentId(departmentid = departmentid!!)

            val adapter = ManagerAttendanceAdapter(requireContext(), attendancelist, this)
            binding.mcvemployeestrength.adapter = adapter

        } else {
            binding.txtnorecord.visibility = View.VISIBLE
        }
    }

    private fun markingWeeklyAttendance(response: NetworkResult.Success<WeeklyAttendanceResponseModel>) {
        try {
            presentcount = 0
            latearrivalcount = 0
            leavescount = 0
            absentcount = 0
            binding.txtpresentstatus.text = "Present: " + " " + presentcount
            binding.txtabsentstautus.text = "Absent: " + " " + absentcount
            binding.txtleavesstatus.text = "Leaves: " + " " + leavescount
            binding.txtlatestaus.text = "Lates: " + " " + latearrivalcount

            response.data?.body?.weeklyAttendanceresponce?.forEach {
                markingAttendance(it)
            }

//
//            response.data?.body?.attendanceSummary?.forEach {
//                markingTotalAttendanceStatus(it)
//            }
        } catch (e: Exception) {
            LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.toString())
        }
    }

    private fun markingAttendance(it: WeeklyAttendanceresponce) {
        if(it.days!=null)
        {
            when (it.days) {
                "Monday" -> {
                    if(it.remarks!=null)
                    {
                        binding.txtmondaypresent.text = it.remarks
                        settingAttStatusColour(it.remarks, binding.txtmondaypresent)
                    }
                    else{
                        binding.txtmondaypresent.text = "---"
                    }
                }

                "Tuesday" -> {
                    if(it.remarks!=null)
                    {
                        binding.txttuesdaystatus.text = it.remarks
                        settingAttStatusColour(it.remarks, binding.txttuesdaystatus)
                    }
                    else{
                        binding.txttuesdaystatus.text = "---"
                    }
                }

                "Wednesday" -> {
                    if(it.remarks!=null)
                    {
                        binding.txtwedstatus.text = it.remarks
                        settingAttStatusColour(it.remarks, binding.txtwedstatus)
                    }
                    else{
                        binding.txtwedstatus.text = "---"
                    }
                }

                "Thursday" -> {
                    if(it.remarks!=null)
                    {
                        binding.txtthursstatus.text = it.remarks
                        settingAttStatusColour(it.remarks, binding.txtthursstatus)
                    }
                    else{
                        binding.txtthursstatus.text = "---"
                    }
                }

                "Friday" -> {
                    if(it.remarks!=null)
                    {
                        binding.txtfristatus.text = it.remarks
                        settingAttStatusColour(it.remarks, binding.txtfristatus)
                    }
                    else{
                        binding.txtfristatus.text = "---"
                    }
                }

            }
        }

    }

    private fun settingAttStatusColour(remarks: String, text: TextView)
    {
        if(remarks!=null)
        {
            when (remarks.lowercase()) {
                "p" -> {
                    var presentCount = presentcount + 1
                    presentcount = presentCount
                    binding.txtpresentstatus.text = "Present: " + " " + presentCount
                    text.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.present_green
                        )
                    )
                }

                "a" -> {
                    var absentCount = absentcount + 1
                    absentcount = absentCount
                    binding.txtabsentstautus.text = "Absent: " + " " + absentCount
                    text.setTextColor(ContextCompat.getColor(requireContext(), R.color.absent_red))
                }

                "l" -> {
                    var leaveCount = leavescount + 1
                    leavescount = leaveCount
                    binding.txtleavesstatus.text = "Leaves: " + " " + leaveCount
                    text.setTextColor(ContextCompat.getColor(requireContext(), R.color.leaves_blue))
                }

                "la" -> {
                    var lateArrival = latearrivalcount + 1
                    latearrivalcount = lateArrival
                    binding.txtlatestaus.text = "Lates: " + " " + lateArrival

                    text.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.latearrival_yellow
                        )
                    )
                }
                "p.h" ->{
                    text.setTextColor(
                        Color.parseColor("#d4d4d4")
                    )
                }
                else ->{
                    text.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
        }
        else{

        }

    }
    private fun callDashboardApi() {
        sessionmanagement = SessionManagement(requireContext())
        empId = sessionmanagement!!.getEmpId()
        email = sessionmanagement!!.getEmail()
        val name = sessionmanagement!!.getName()
        binding.txtName.text = "Welcome, $name!"

        val dashboardrequest =
            DashboardDataRequestModel(employeeID = empId, Email = email.toString())
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getDashboardInformation(dashboardrequest)
        }
    }
    // stop handler function


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermissionForReadAndWrite(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val storagepermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        val camerapermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )

        return if (Build.VERSION.SDK_INT >= 33) {
            camerapermission == PackageManager.PERMISSION_GRANTED
        } else {
            !(readPermission != PackageManager.PERMISSION_GRANTED ||
                    writePermission != PackageManager.PERMISSION_GRANTED || storagepermission != PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun showForceUpdateDialog() {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(false)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.general_dialog_design)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val ok: ConstraintLayout = logoutDialog.findViewById(R.id.ok)
        val btntext: TextView = logoutDialog.findViewById(R.id.checkout)
        val text: TextView = logoutDialog.findViewById(R.id.txtsuccess)
        val popupIcon = logoutDialog.findViewById(R.id.imgPopIcon) as ImageView
        popupIcon.setImageResource(R.drawable.info)
        popupIcon.getLayoutParams().height = 100
        popupIcon.getLayoutParams().width = 100
        text.setTextSize(14f)
        btntext.text = "Update"
        text.text =
            "A new version is now available, please update to latest version by tapping on Update"
        text.text = Constants.DOWNLOAD_MSG
        ok.setSafeOnClickListener {
            logoutDialog.dismiss()
            sessionmanagement?.clearPreference()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().applicationContext.getPackageName())
            startActivity(i)
        }
        logoutDialog.show()
    }


    fun showDialogTextWithOnclick(
        message: String,
        btnTextString: String,
        onclick: (flag: Boolean) -> Unit
    ) {
        val dialog = Dialog(requireActivity())
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val view: View =
            (requireActivity()).layoutInflater.inflate(
                R.layout.newdialog,
                null
            )
        dialog.setContentView(view)

        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            false
        }
        dialog.window?.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )

        val Message: TextView
        val btnText: TextView
        val btnAllow: ConstraintLayout

        Message = view.findViewById<View>(com.appinsnap.aishrm.R.id.txtsuccess) as TextView
        btnText = view.findViewById<View>(com.appinsnap.aishrm.R.id.checkout) as TextView
        val imgview: ImageView = view.findViewById<View>(R.id.popupicon) as ImageView
        btnAllow = view.findViewById<View>(com.appinsnap.aishrm.R.id.ok) as ConstraintLayout

        imgview.setImageResource(R.drawable.erroricon)
        Message.setText(message)
        btnText.setText(btnTextString)


        btnAllow.setOnClickListener {
            dialog.dismiss()
            onclick(true)
        }
        dialog.show()
    }
    private var dashboarddata: GetDashboardResponseModelX?=null

    private fun getDashboardInformation(view: View) {
        sessionmanagement = SessionManagement(requireContext())
        empId = sessionmanagement!!.getEmpId()
        email = sessionmanagement!!.getEmail()
        val name = sessionmanagement!!.getName()
        binding.txtName.text = "Welcome, $name!"
        val dashboardrequest = DashboardDataRequestModel(employeeID = empId, Email = email!!)

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getDashboardInformation(dashboardrequest)
        }
        homeViewModel._getdashboardinformation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressDialog = true
                    showAttendanceProgressBar = false
                    binding.swipelayout.isRefreshing = false
                    if(response.data!=null)
                    {
                        dashboarddata = response.data
                        storingLeaveInformation(dashboarddata!!)
                    }
                    binding.checkInCheckOutBtn.visibility = View.VISIBLE
                    if(response.data?.body!=null)
                        if (response.data.body.getUserDash != null) {

                            if (response.data.body.getUserDash.lastActivity.lowercase()
                                    .contains("checkout") && response.data.body.getUserDash.islatecheckin == true
                            ) {
                                sessionmanagement!!.setLateSittingStatus(true)
                                binding.checkincheckout.text = "Check-out"
                            } else if (response.data.body.getUserDash.lastActivity.lowercase()
                                    .contains("checkin")
                            ) {
                                binding.checkincheckout.text = "Check-out"
                            }
                            else if(response.data.body.getUserDash.lastActivity.lowercase()
                                    .contains("checkout")&&response.data.body.getUserDash.islatecheckin == false) {
                                binding.checkincheckout.text = "Check-in"
                                sessionmanagement!!.setLateSittingStatus(false)
                            }
                            else{
                                binding.checkincheckout.text = "Check-in"
                                sessionmanagement!!.setLateSittingStatus(false)
                            }
                            if (response.data?.body?.getUserDash?.firstCheckIn != null) {
                                if(response.data.body.getUserDash.firstCheckIn.isNotEmpty())
                                {
                                    empStatusCheckIn = response.data?.body?.getUserDash.firstCheckIn.toString().split(" ")[1]
//                                var formattedCheckIn = MyHelperClass.convertToAMPM_24to12(empStatusCheckIn)
//                                formattedFirstCheckin = LocalTime.parse(formattedCheckIn, DateTimeFormatter.ofPattern("hh:mm a"))
                                }
                            }
                            else {
                                empStatusCheckIn = ""
                            }
                            if (response.data?.body?.getUserDash?.lastCheckOut != null) {
                                empStatusCheckOut = response.data?.body?.getUserDash.lastCheckOut.toString()
                                    .split(" ")[1]
                            } else {
                                empStatusCheckOut = ""
                            }
                        }
                        else{
                            binding.checkincheckout.text = "Check-in"
                            sessionmanagement!!.setLateSittingStatus(false)
                        }

                    LoggerGenratter.getInstance().printLog("data", dashboarddata.toString())
                    var latitude = response.data?.body?.getUserDash?.latitude
                    var longitude = response.data?.body?.getUserDash?.longitude
                    LoggerGenratter.getInstance()
                        .printLog(TAG, response.data?.body?.toString())
                    if (latitude != null && longitude != null) {

                        officeLat = latitude
                        officeLong = longitude
                    }
                    checkInOutStatus(response)
                    displayUserLeavesInformation(response)
                }

                is NetworkResult.Error -> {
                    binding.swipelayout.isRefreshing = false
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        true,
                        "no",
                        yourMessage = response.message.toString(),
                        yourTitle = "",
                        location = this,
                        icon = R.drawable.erroricon
                    )
                }

                is NetworkResult.Loading -> {

                }
            }

        }
    }

    private fun storingLeaveInformation(dashboarddata: GetDashboardResponseModelX) {
        if(dashboarddata!=null){
            if(dashboarddata.body!=null)
                if(dashboarddata.body.getLeaveCountss!=null)
                {
                    val gson = Gson()
                    val response = gson.toJson(dashboarddata.body.getLeaveCountss)
                    SessionManagement(requireContext()).setLeavesData(response)
                }

        }

    }


    private fun changingDashBoardView() {
        try {

            var viewId = sessionmanagement?.getViewId()
            if (viewId == 2) {//Manager
                displayingTotalTeamMembers()
                hidingCTODashboardView()
                callManagerDasboardApi()
                binding.statisticsattendancelayout.hide()
                binding.cardAnnualLeave.show()
                binding.cardSickLeave.show()
                binding.rvlayout.show()
                binding.materialCardView3.show()
                requireActivity().findViewById<ConstraintLayout>(R.id.history).show()
                binding.txtdesignation.visibility = View.VISIBLE
                binding.mcvdate.visibility = View.VISIBLE
                binding.mcvattendancestatus.visibility = View.GONE
                binding.mcvdepartmentattendance.visibility = View.GONE
                binding.dept.visibility = View.GONE
            }
            if (viewId == 4) {//TeamLead
                displayingTotalTeamMembers()
                binding.txtdesignation.visibility = View.VISIBLE
                hidingCTODashboardView()
                callManagerDasboardApi()
//                binding.approvallayout.show()
                binding.cardAnnualLeave.show()
                binding.cardSickLeave.show()
                binding.rvlayout.show()
                binding.materialCardView3.show()
                requireActivity().findViewById<ConstraintLayout>(R.id.history).show()
                binding.statisticsattendancelayout.hide()
                binding.mcvdate.visibility = View.VISIBLE
                binding.mcvattendancestatus.visibility = View.GONE
                binding.mcvdepartmentattendance.visibility = View.GONE
                binding.dept.visibility = View.GONE
            }
            if (viewId == 3) {//Employee
                presentcount = 0
                absentcount = 0
                latearrivalcount = 0
                leavescount = 0
                binding.cardAnnualLeave.show()
                binding.rvlayout.show()
                binding.cardAnnualLeave.show()
                binding.cardSickLeave.show()
                binding.materialCardView3.show()
                binding.cardSickLeave.show()
                binding.materialCardView3.show()
                requireActivity().findViewById<ConstraintLayout>(R.id.history).show()
                binding.statisticsattendancelayout.hide()
                hidingCTODashboardView()
                binding.txtdesignation.visibility = View.VISIBLE
                binding.txttotalteammembers.visibility = View.INVISIBLE
                binding.mcvdate.visibility = View.GONE
                binding.mcvdepartmentattendance.visibility = View.GONE
                binding.dept.visibility = View.GONE
                callWeekAttendanceApi()
            }
            if (viewId == 1) {//Cto
                isStatisticsGraph = true
                val formatteddate = formatDate(currentDateCto)
                binding.departmenttxtcheck.text = formatteddate.toString()
                displayingTotalTeamMembers()
                dateforstatisticsgraph = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
                callStaticticsGraphApi()
                isStatistics = true
                binding.cardAnnualLeave.hide()
                binding.cardSickLeave.hide()
                binding.materialCardView3.hide()
                binding.mcvStatistics.setCardBackgroundColor(resources.getColor(R.color.charcoal))
                binding.mcvStatistics.setStrokeColor(resources.getColor(R.color.yellow))
                binding.txtstatistics.setTextColor(resources.getColor(R.color.yellow))
                binding.mcvStatistics.strokeWidth = 1
                binding.mcvAttendnace.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
                binding.mcvAttendnace.strokeWidth = 0
                var date = formatteddateWithDashes
                var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
                var parsedate: Date = inputdateformat.parse(date)
                val outputDateFormat = SimpleDateFormat("MMM dd, yyyy")
                val formatteddatestring: String = outputDateFormat.format(parsedate)
                binding.txtdatestatistics.text = formatteddatestring
                binding.mcvStatisticsGraph.show()
                binding.rvlayout.hide()
                binding.mcvAttendanceGraph.hide()
                managerview = false
                callManagerDasboardApi()
                requireActivity().findViewById<ConstraintLayout>(R.id.history).hide()
                binding.txtdesignation.visibility = View.VISIBLE
                binding.mcvdate.visibility = View.GONE
                binding.statisticsattendancelayout.show()
                binding.cardAnnualLeave.hide()
                binding.cardSickLeave.hide()
                binding.materialCardView3.hide()
                binding.mcvattendancestatus.visibility = View.GONE
                binding.dept.visibility = View.VISIBLE
                binding.mcvdepartmentattendance.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.message)
        }

    }

    private fun displayingTotalTeamMembers() {
        binding.txttotalteammembers.visibility = View.VISIBLE
        var memberCount = sessionmanagement?.getMemberCount()
        var viewid = sessionmanagement?.getViewId()
        if (viewid == 1) {
            binding.txttotalteammembers.text = "Total Employees:" + " " + memberCount.toString()
        } else {
            binding.txttotalteammembers.text =
                "Total Team Members:" + " " + memberCount.toString()
        }
    }

    private fun callManagerDasboardApi() {
        var currentDate = getCurentDate()
        val formatteddate = formatDate(currentDate)
        binding.txtcheck.text = formatteddate
        var empId = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        try {

            val managerdashboardrequest =
                CTOManagerDashboardRequest(
                    employeeID = empId!!.toInt(),
                    viewID = viewid!!,
                    currentDate = currentDate.toString()
                )
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getManagerCTOAttendance(managerdashboardrequest)
            }
        } catch (e: Exception) {
            LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.toString())
        }
    }
    private fun hidingCTODashboardView()
    {

    }

    private fun callWeekAttendanceApi() {
        var currentdate = getCurentDate()
        var employeeid = sessionmanagement!!.getEmpId()
        val days = 7
        val weekAttendanceRequest = WeekAttendanceRequestModel(
            employeeID = employeeid,
            date = currentdate.toString(),
            days = days
        )
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getWeekAttendance(weekAttendanceRequest)
        }
    }

    private fun setupSlidShow(promotionalImages: List<SettingApiResponse.PromotionalImage?>) {
        val slideModels: ArrayList<SlideModel> = ArrayList()
        if (promotionalImages.isNotEmpty()) {
            val displaymetrics = requireContext().resources.displayMetrics
            var newwidth = displaymetrics.widthPixels
            binding.mcvimageslider.layoutParams.height = newwidth - 160
            binding.mcvimageslider.requestLayout()
            binding.mcvimageslider.visibility = View.VISIBLE
            promotionalImages.forEach {
                it?.let {
                    slideModels.add(
                        SlideModel(it.filePath.toString(), "")
                    )
                }
            }
            binding.slider.setImageList(slideModels, ScaleTypes.FIT)
        } else {
            binding.mcvimageslider.visibility = View.GONE
        }
    }

    private fun showpendingcount(text: Boolean, pendingcount: Int?) {
        binding.approvallayout.show()
        if (moreThanOne) {
            binding.pendingapprovalnumber.text = "$pendingcount" + " " + "approvals are pending"
        } else {
            binding.pendingapprovalnumber.text = "$pendingcount" + " " + "approval is pending"
        }
    }

    private fun displayUserLeavesInformation(response: NetworkResult.Success<GetDashboardResponseModelX>) {
        binding.rvleaves.visibility = View.VISIBLE
        if (leaveslist.size <= 3) {
            SPAN_COUNT = 1
        } else {
            SPAN_COUNT = 2
        }
        val layoutManager = GridLayoutManager(
            requireContext(), SPAN_COUNT,
            GridLayoutManager.HORIZONTAL, false
        )
        var viewid = sessionmanagement?.getViewId()
        var isPMO = sessionmanagement?.getPMOStatus()
        if (viewid == 3 && isPMO == true) {
            var pendingcount = response.data?.body?.employeePendingCountresponce?.employeePendingCount
            if (pendingcount == 0) {
                binding.approvallayout.hide()
            } else {
                if (pendingcount == 1) {
                    moreThanOne = false
                    showpendingcount(moreThanOne, pendingcount)
                } else {
                    moreThanOne = true
                    showpendingcount(moreThanOne, pendingcount)
                }

            }
        } else if(viewid == 4||viewid == 2|| viewid == 1) {
            var pendingcount =
                response.data?.body?.employeePendingCountresponce?.employeePendingCount
            if (pendingcount == 0) {
                binding.approvallayout.hide()
            } else {
                if (pendingcount == 1) {
                    moreThanOne = false
                    showpendingcount(moreThanOne, pendingcount)
                } else {
                    moreThanOne = true
                    showpendingcount(moreThanOne, pendingcount)
                }
            }
        }
        else{
            binding.approvallayout.hide()
        }
        var annualleaves: Int = 0
        var medicalleaves: Int = 0
        var casualleaves: Int = 0
        response.data?.body?.getLeaveCountss?.forEach {
            if (it.name.lowercase().contains("annual")) {
                annualleaves = it.availableLeaves
            }
            if (it.name.lowercase().contains("medical")) {
                medicalleaves = it.availableLeaves
            }
            if (it.name.lowercase().contains("casual")) {
                casualleaves = it.availableLeaves
            }
        }
        var count = response.data?.body?.countreasonModel?.count
        if (count != null) {
            sessionmanagement?.setCharacterLimit(count!!.toInt())
        }

        var totalHours = response.data?.body?.employeetotalhrs?.time
        if (totalHours != null) {
            if(totalHours.isNotEmpty())
            {
                var totalHoursFormatted = response.data?.body?.employeetotalhrs?.time?.toDouble()
                if (totalHoursFormatted != null) {
                    userWorkingHour = totalHoursFormatted
                }
            }
        }

        binding.txtTotalAnnualLeave.text = annualleaves.toString()
        binding.txtTotalCasualLeaves.text = casualleaves.toString()
        binding.txtTotalSickLeave.text = medicalleaves.toString()

    }


    private fun checkInOutStatus(response: NetworkResult.Success<GetDashboardResponseModelX>) {

        try {
            if (response.data != null) {
                if(response.data.body.getUserDash.fromDate!=null&&response.data.body.getUserDash.fromDate.isNotEmpty())
                {
                    var firstcheckin = response.data.body.getUserDash.fromDate
                    forgottocheckoutdate = firstcheckin
                }
                if(response.data.body.getUserDash!=null)
                {
                    if(response.data.body.getUserDash.firstCheckIn!=null&&response.data.body.getUserDash.firstCheckIn.isNotEmpty())
                    {
                        try {
                            firstCheckInStatus = response.data.body.getUserDash.firstCheckIn
                        }
                        catch (_: Exception) {
                        }
                    }
                    if(response.data.body.getUserDash.lastCheckOut!=null&&response.data.body.getUserDash.lastCheckOut.isNotEmpty())
                    {
                        try {
                            lastCheckOutStatus = response.data.body.getUserDash.lastCheckOut
                        }
                        catch (_: Exception) {
                        }
                    }


                    try{
                        lastActivty = response.data.body.getUserDash.lastActivity
                        appliedDaysStatus = response.data.body.getUserDash.appliedDaysStatus
                        employeeRecievedLocation = response.data.body.getUserDash.employeeLocation
                        recievedTodate = response.data.body.getUserDash.toDate
                        recievedFromdate = response.data.body.getUserDash.fromDate
                    }
                    catch (_: Exception)
                    {

                    }
                    try {
                        if (response.data.body.getUserDash.firstCheckIn.toString()
                                .isNullOrEmpty()
                        ) {

                            checkInTime = ""

                        } else {

                            checkInTime = response.data.body.getUserDash.firstCheckIn.toString()

                        }
                    } catch (_: Exception) {
                        checkInTime = ""
                    }
                    try {
                        if (response.data.body.getUserDash.lastCheckOut.toString()
                                .isNullOrEmpty()
                        ) {

                            checkOutTime = ""

                        } else {

                            checkOutTime = response.data.body.getUserDash.lastCheckOut.toString()
                        }
                    } catch (_: Exception) {
                        checkOutTime = ""
                    }

                    if (lastActivty.isNullOrEmpty()) {

                        status = "CheckIn"

                    } else if (lastActivty.contains("CheckIn")) {

                        status = "CheckOut"
                        employeeLocation = "Office"

                        if (checkInTime.isNullOrEmpty()) {
                        } else {
                            val time1 = checkInTime.split(" ")

                            checkInTime = time1[1]
                            val d1: Date = f1.parse(checkInTime)

                            checkInTime = f2.format(d1)
                        }

                    } else if (lastActivty.contains("CheckOut") && isLateSittingCheckIn==true) {


                        status = "CheckOut"
                        if (checkInTime.isNullOrEmpty() && checkOutTime.isNullOrEmpty()) {
                            //      binding.txtAttendanceDesc.text = "-----"
                        } else if (checkInTime.isNullOrEmpty()) {
                            val time = checkOutTime.split(" ")
                            checkOutTime = time[1]
                            val d2: Date = f1.parse(checkOutTime)
                            checkOutTime = f2.format(d2)
                        } else if (checkOutTime.isNullOrEmpty()) {
                            val time = checkInTime.split(" ")
                            checkInTime = time[1]

                            val d1: Date = f1.parse(checkInTime)

                            checkInTime = f2.format(d1)


                        } else {
                            try {

                                val time1 = checkInTime.split(" ")
                                val time2 = checkOutTime.split(" ")
                                checkInTime = time1[1]
                                checkOutTime = time2[1]

                                val d1: Date = f1.parse(checkInTime)
                                val d2: Date = f1.parse(checkOutTime)

                                checkInTime = f2.format(d1)
                                checkOutTime = f2.format(d2)
                                if (lastActivty.lowercase().contains("checkout")) {
                                    if (!checkInTime.isNullOrEmpty()) {

                                    }
                                } else {

                                    if (!checkOutTime.isNullOrEmpty()) {


                                    }

                                }
                            } catch (ex: Exception) {
                                LoggerGenratter.getInstance()
                                    .printLog("HOME FRAGMENT", "checkInOutStatus: $ex")
                            }
                        }
                    }
                    else if(lastActivty.contains("CheckOut") && isLateSittingCheckIn==false) {
                        status = "CheckIn"
                    }

                    else
                    {
                        status = "CheckOut"
                    }
                }
                else{
                    showLogoutAppDialog()
                }

//                callAttendanceGraphApi()

            }
        } catch (ex: Exception) {
            LoggerGenratter.getInstance().printLog("HOME FRAGMENT", "checkInOutStatus: $ex")
        }
    }

    private fun showLogoutAppDialog() {
        val permissiondialog = Dialog(requireContext())
        permissiondialog.setCancelable(false)
        permissiondialog.setCanceledOnTouchOutside(false)
        permissiondialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        permissiondialog.setContentView(R.layout.general_dialog_design)
        permissiondialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
        )
        val okbtn: ConstraintLayout = permissiondialog.findViewById(R.id.ok)
        val popicon: ImageView = permissiondialog.findViewById(R.id.imgPopIcon)
        val textmessage: TextView = permissiondialog.findViewById(R.id.txtsuccess)
        popicon.setImageResource(R.drawable.reject)
        popicon.layoutParams.height = 130
        popicon.layoutParams.width = 130
        textmessage.text = "Unable to process your request at this time, please try again later!"
        okbtn.setSafeOnClickListener {
            permissiondialog.dismiss()
            sessionmanagement?.clearPreference()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
        if (permissiondialog.isShowing) {
            permissiondialog.dismiss()
        }
        permissiondialog.show()
    }

    private fun callStaticticsGraphApi() {
        hidepieChart()
        if (Utils.hasInternetConnection()) {
            binding.piechartprogressbar.show()
        } else {
            binding.piechartprogressbar.hide()
        }
        CoroutineScope(Dispatchers.Main).launch {

            homeViewModel.getStatisticsDashboardGraph(
                AttendanceGraphRequest(
                    departmentid,
                    designationid,
                    dateforstatisticsgraph,
                    genderid,
                    groupid,
                    officeid,
                    dateforstatisticsgraph
                )
            )
        }
    }

    private fun callAttendanceGraphApi() {
        binding.txtatthendancegraphrecord.hide()
        hidebarchart()
        // hidelinegraph()
        if (Utils.hasInternetConnection()) {
            binding.linechartprogressbar.show()
        } else {
            binding.linechartprogressbar.hide()
        }
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getAttendanceDashboardGraph(
                AttendanceGraphRequest(
                    departmentid, designationid, enddateforattendancegraph,
                    genderid, groupid, officeid, startdateforattendancegraph
                )
            )
        }
    }

    private fun notificationPermissionCheckToStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notificationManagerCompat = NotificationManagerCompat.from(requireActivity())
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            if (areNotificationsEnabled) {
                startService()
            }
        } else {
            startService()
        }
    }

    private fun startService() {
        LoggerGenratter.getInstance()
            .printLog("serviceLogs:", "Service called")
        val serviceIntent = Intent(requireActivity(), LocationService::class.java)
        LoggerGenratter.getInstance().printLog("serviceLogs:", "Service started")
    }


    private fun showdatepickerdialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.YEAR, 1)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DAY_OF_MONTH, 31)
        val MaxDate = c.timeInMillis

        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day

                    val outputDateFormat = SimpleDateFormat("MMM dd, yyyy")
                    inputdateformat = SimpleDateFormat("yyyy-MM-dd")
                    var parsedate: Date = inputdateformat.parse(toDates)
                    outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
                    val formatteddates: String = outputDateFormats.format(parsedate)
                    binding.txtdatestatistics.text = formatteddates
                    dateforstatisticsgraph = toDates

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
                    var parsedate: Date = inputdateformat.parse(toDates)
                    val outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
                    val formatteddates: String = outputDateFormats.format(parsedate)
                    binding.txtdatestatistics.text = formatteddates
                    dateforstatisticsgraph = toDates

                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    val outputDateFormat = SimpleDateFormat("MMM dd, yyyy")
                    var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
                    var parsedate: Date = inputdateformat.parse(toDates)
                    val outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
                    val formatteddates: String = outputDateFormats.format(parsedate)
                    binding.txtdatestatistics.text = formatteddates
                    dateforstatisticsgraph = toDates

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    val outputDateFormat = SimpleDateFormat("MMM dd, yyyy")
                    var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
                    var parsedate: Date = inputdateformat.parse(toDates)
                    val outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
                    val formatteddates: String = outputDateFormats.format(parsedate)
                    binding.txtdatestatistics.text = formatteddates
                    dateforstatisticsgraph = toDates
                }
                binding.pieChart.hide()
                binding.totalgraphtext.hide()
                binding.pieChart.clear()
                callStaticticsGraphApi()
            },
            year,
            month,
            day
        )

        dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
        dpd.show()
    }
    private fun onClickListeners()
    {
        binding.leftarrow.setSafeOnClickListener {
            callManagerDailyAttendanceApiWithPrevious()
        }
        binding.rightarrow.setOnClickListener {
            callManagerDailyAttendanceApiWithNext()
        }
        binding.statisticsdate.setOnClickListener {
            showdatepickerdialog()
        }
        binding.approvallayout.setOnClickListener {
            try {
                findNavController().navigate(HomeFragmentDirections.actionHomefragmentToApprovalAndNews())
            }
            catch (e:Exception)
            {

            }
        }
        binding.departmentrightarrow.setOnClickListener {
            ctoDashboardApiRecordWithNew()
        }
        binding.departmentleftarrow.setOnClickListener {
            ctoDashboardApiRecordOnPrevious()
        }
        binding.mcvStatistics.setOnClickListener {
            isStatisticsGraph = true
            isStatistics = true
            var inputdateformat = SimpleDateFormat("yyyy-MM-dd")
            dateforstatisticsgraph = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
            var parsedate: Date = inputdateformat.parse(dateforstatisticsgraph)
            val outputDateFormats = SimpleDateFormat("MMM dd, yyyy")
            val formatteddates: String = outputDateFormats.format(parsedate)
            binding.txtdatestatistics.text = formatteddates
            binding.mcvStatistics.setCardBackgroundColor(resources.getColor(R.color.charcoal))
            binding.txtstatistics.setTextColor(resources.getColor(R.color.yellow))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.white))
            binding.mcvStatistics.setStrokeColor(resources.getColor(R.color.yellow))
            binding.mcvStatistics.strokeWidth = 1
            binding.mcvAttendnace.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
            binding.mcvAttendnace.strokeWidth = 0
            binding.mcvStatisticsGraph.show()
            binding.mcvAttendanceGraph.hide()
            callStaticticsGraphApi()
        }

        binding.cardSickLeave.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homefragment_to_leavemanagement)
            }
            catch (e:Exception)
            {

            }

        }

        binding.cardAnnualLeave.setOnClickListener {
            try{
                findNavController().navigate(R.id.action_homefragment_to_leavemanagement)
            }
            catch (e:java.lang.Exception)
            {

            }

        }

        binding.materialCardView3.setOnClickListener {
            try{
                findNavController().navigate(R.id.action_homefragment_to_leavemanagement)
            }
            catch (e:java.lang.Exception)
            {

            }

        }


        binding.mcvAttendnace.setOnClickListener {
            isStatisticsGraph = false
            isStatistics = false
            isdaysFive = true
            binding.txtstatistics.setTextColor(resources.getColor(R.color.white))
            binding.txtAttendance.setTextColor(resources.getColor(R.color.yellow))
            binding.txtdateattendance.text = "Date"
            getCurrentWeekDate()
            binding.mcvAttendnace.setCardBackgroundColor(resources.getColor(R.color.charcoal))
            binding.mcvAttendnace.setStrokeColor(resources.getColor(R.color.yellow))
            binding.mcvAttendnace.strokeWidth = 1
            binding.mcvStatistics.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
            binding.mcvStatistics.strokeWidth = 0
            binding.mcvStatisticsGraph.hide()
            binding.mcvAttendanceGraph.show()
            callAttendanceGraphApi()
        }
        binding.attendnacefilter.setOnClickListener {
            homeViewModel.getGraphFilters()
        }
        binding.statisticsfilter.setOnClickListener {
            homeViewModel.getGraphFilters()
        }
        binding.attendancedate.setOnClickListener {
            showAttendanceDateDialog()
        }

        binding.mcvattendanceweek.setSafeOnClickListener {
            var dptid: Int = 0
            if (binding.mcvattendancestatus.visibility == View.VISIBLE) {
                dptid = sessionmanagement!!.getDepartmentId()
            } else {
                dptid = sessionmanagement!!.getDptidTemp()
            }
            if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                empstatus = "Absent"
            } else {
                empstatus = "Present"
            }
            val employeeinfo = EmployeeAttendanceInfo(
                name = sessionmanagement?.getName(),
                employeeID = sessionmanagement!!.getEmpId().toInt(),
                designation = sessionmanagement?.getDesignation(),
                firstCheckIn = empStatusCheckIn,
                lastCheckOut = empStatusCheckOut,
                remarks = status,
                depID = dptid,
                status = empstatus,
                from = "home",
                date = currentDateCto
                /*add employee status to show individual employee status on employee details */
            )
            try {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomefragmentToEmployeeDetail(
                        employeeinfo
                    )
                )
            }
            catch (e:java.lang.Exception)
            {

            }

//            findNavController().navigate(HomeFragmentDirections.actionHomefragmentToEmployeeDetail(empStatus))

        }


        binding.pieLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Clear highlighting of values
                    binding.pieChart.highlightValues(null)
//                    binding.pieChart.isHighlightPerTapEnabled = false
                }
            }
            true
        }

        binding.pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) return

                val sliceIndex = h?.x?.toInt() ?: -1 // Get the index of the selected slice
                when (sliceIndex) {
                    entriesList?.get(0)?.pIndex -> {
                        statisticsList("Present")
                    }

                    entriesList?.get(0)?.aIndex -> {
                        statisticsList("Absent")
                    }

                    entriesList?.get(0)?.leaIndex -> {
                        statisticsList("Leave")
                    }

                    else -> {
                    }
                }
            }

            override fun onNothingSelected() {
            }
        })


//        binding.textmonth.setSafeOnClickListener {
//            isdaysFive = false
//            binding.dateoptions.hide()
//            getCurrentMonthDate()
//            hidebarchart()
//            callAttendanceGraphApi()
//        }
//        binding.textweek.setSafeOnClickListener {
//            isdaysFive = true
//            binding.dateoptions.hide()
//            getCurrentWeekDate()
//            hidebarchart()
//            callAttendanceGraphApi()
//        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAttendanceDateDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val from = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)


                val to = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        val selectedToDate = Calendar.getInstance()
                        selectedToDate.set(year, monthOfYear, dayOfMonth)


                        fromDate = mDateFormat.format(selectedDate.time)
                        startdateforattendancegraph = fromDate
                        var parsedatefromdate = inputdateformat.parse(fromDate)
                        var formateddatefromdate = outputDateFormats.format(parsedatefromdate)
                        binding.txtselectedfromattendancedate.text = formateddatefromdate
                        toDate = mDateFormat.format(selectedToDate.time)
                        enddateforattendancegraph = toDate
                        var parsedate = inputdateformat.parse(toDate)
                        var formateddate = outputDateFormats.format(parsedate)
                        binding.txtselectedtoattendancedate.text = formateddate
                        hidebarchart()
                        callAttendanceGraphApi()
                    },
                    year,
                    month,
                    day
                )

                // Set minimum and maximum date for "To" DatePickerDialog
                val minDate = Calendar.getInstance()
                minDate.timeInMillis = selectedDate.timeInMillis

                val currentDate = java.time.LocalDate.now().minusDays(2)
                val selectedLocalDate = calendarToLocalDate(selectedDate)
                if (currentDate == selectedLocalDate) {
                    // If the selected date is the current date, set both minimum and maximum to the same date
                    to.datePicker.minDate = System.currentTimeMillis() - 1000
                    to.datePicker.maxDate = System.currentTimeMillis() - 1000
                } else {
                    val maxDate = Calendar.getInstance()
                    var currentselecteddate = java.time.LocalDate.now()
                    // Calculate the difference in days between selected date and current date
                    val daysDifference =
                        ChronoUnit.DAYS.between(selectedLocalDate, currentDate).toInt()
                    if (daysDifference < 6) {
                        // If selected date is less than 6 days from the current date
                        // Set minDate to selected date + 2 days
                        minDate.timeInMillis = selectedDate.timeInMillis
                        minDate.add(Calendar.DAY_OF_MONTH, 2)

                        // Set maxDate to selected date + 6 days
                        to.datePicker.minDate =
                            selectedDate.timeInMillis + (2 * 24 * 60 * 60 * 1000)
                        to.datePicker.maxDate = System.currentTimeMillis() - 1000
                    }
                    else {
                        // Set maximum date for "To" DatePickerDialog (6 days from the selected date)
                        maxDate.timeInMillis = selectedDate.timeInMillis
                        maxDate.add(Calendar.DAY_OF_MONTH, 6)
                        to?.datePicker?.minDate =
                            selectedDate.timeInMillis + (2 * 24 * 60 * 60 * 1000)
                        // Set the minDate and maxDate in the DatePicker
                        to?.datePicker?.maxDate = maxDate.timeInMillis
                    }

                }

                to.setTitle("To")
                to.show()
            },
            year,
            month,
            day
        )

        // Set maximum date for "From" DatePickerDialog
        val maxDateForFrom = Calendar.getInstance()
        maxDateForFrom.timeInMillis =
            System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000) // Subtract 2 days
        from.datePicker.maxDate = maxDateForFrom.timeInMillis
        from.setTitle("From")
        from.show()
    }

    fun calendarToLocalDate(calendar: Calendar): java.time.LocalDate? {
        val date = calendar.time // Convert Calendar to Date
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }


    private fun callManagerDailyAttendanceApiWithNext() {
        if (checkdate == currentDateCto) {
            binding.rightarrow.isEnabled = false
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray
                ), PorterDuff.Mode.SRC_IN
            )// = true
        }
        else {
            managerview = true
            val newdate = currentDateCto.plusDays(1)
            currentDateCto = newdate
            if (checkdate == currentDateCto) {
                binding.rightarrow.isEnabled = false
                binding.rightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_gray
                    ), PorterDuff.Mode.SRC_IN
                )// = true
            }
            val formatteddate = formatDate(currentDateCto)
            binding.txtcheck.text = formatteddate.toString()
            var empId = sessionmanagement?.getEmpId()
            var viewid = sessionmanagement?.getViewId()
            try {
                val managerdashboardrequest =
                    CTOManagerDashboardRequest(
                        employeeID = empId!!.toInt(),
                        viewID = viewid!!,
                        currentDate = currentDateCto.toString()
                    )
                CoroutineScope(Dispatchers.Main).launch {
                    homeViewModel.getManagerCTOAttendance(managerdashboardrequest)
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.toString())
            }
        }


    }

    private fun callManagerDailyAttendanceApiWithPrevious() {
        managerview = true
        binding.rightarrow.isEnabled = true
        binding.rightarrow.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.yellow),
            PorterDuff.Mode.SRC_IN
        )// = true
        val newdate = currentDateCto.plusDays(-1)
        currentDateCto = newdate
        val formatteddate = formatDate(currentDateCto)
        binding.txtcheck.text = formatteddate.toString()
        var empId = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        try {
            val managerdashboardrequest =
                CTOManagerDashboardRequest(
                    employeeID = empId!!.toInt(),
                    viewID = viewid!!,
                    currentDate = currentDateCto.toString()
                )
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getManagerCTOAttendance(managerdashboardrequest)
            }
        } catch (e: Exception) {
            LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.toString())
        }
    }

    private fun ctoDashboardApiRecordWithNew() {
        if (currentDateCto == checkdate) {
            binding.departmentrightarrow.isEnabled = false
            binding.departmentrightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray
                ), PorterDuff.Mode.SRC_IN
            )// = true

        } else {

            managerview = false
            val newdate = currentDateCto.plusDays(1)
            currentDateCto = newdate
            if (currentDateCto == checkdate) {
                binding.departmentrightarrow.isEnabled = false
                binding.departmentrightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_gray
                    ), PorterDuff.Mode.SRC_IN
                )// = true
            }
            val formatteddate = formatDate(currentDateCto)
            binding.departmenttxtcheck.text = formatteddate.toString()
            var empId = sessionmanagement?.getEmpId()
            var viewid = sessionmanagement?.getViewId()

            try {
                val managerdashboardrequest =
                    CTOManagerDashboardRequest(
                        employeeID = empId!!.toInt(),
                        viewID = viewid!!,
                        currentDate = currentDateCto.toString()
                    )
                CoroutineScope(Dispatchers.Main).launch {
                    homeViewModel.getManagerCTOAttendance(managerdashboardrequest)
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("HOME FRAGMENT", e.toString())
            }
        }
    }

    private fun ctoDashboardApiRecordOnPrevious() {
        binding.departmentrightarrow.isEnabled = true
        binding.departmentrightarrow.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            ), PorterDuff.Mode.SRC_IN
        )// = true

        val newdate = currentDateCto.plusDays(-1)
        currentDateCto = newdate
        val formatteddate = formatDate(currentDateCto)
        binding.departmenttxtcheck.text = formatteddate.toString()
        managerview = false
        var empId = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        val managerdashboardrequest =
            CTOManagerDashboardRequest(
                employeeID = empId!!.toInt(),
                viewID = viewid!!,
                currentDate = currentDateCto.toString()
            )
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getManagerCTOAttendance(managerdashboardrequest)
        }
    }

    fun formatDate(date: LocalDate): String {

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return date.format(formatter)// formatter.format(date)
    }


    private fun checkIn() {
        showProgressDialog(true)
        if (Utils.hasInternetConnection()) {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
                        val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
                        if (areNotificationsEnabled) {
                            getUserCurrentLocation()
                        }
                        else
                        {
                            showProgressDialog(false)
                            showNotificationDialog()
                        }
                    }
                    else{
                        getUserCurrentLocation()
                    }
                }
                else
                {
                    showProgressDialog(false)
                    showGeneralDialogFragment(
                        context = requireContext(),
                        bottom = true,
                        click = this,
                        setCancelAble = false,
                        yourTitle = "",
                        yourMessage = "Please turn on your location",
                        value = "",
                        location = this,
                        icon = R.drawable.reject
                    )
                }

            } else {
                showProgressDialog(false)
                requestPermissions()
            }
        } else {
            showProgressDialog(false)
            showGeneralDialogFragment(
                requireContext(),
                false,
                this,
                true,
                "no",
                yourMessage = "No Internet Connection",
                yourTitle = "",
                location = this,
                icon = R.drawable.reject
            )

        }
    }
    private fun getUserCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(
                requireContext()
            )
        fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location == null)
                    Toast.makeText(
                        context,
                        "Cannot get location",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                else
                {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    callLateSittingStatusApi()
//                                var saddarlatitude = 33.5969
//                                var saddarlongitude = 73.0528
//                                latitude = saddarlatitude.toString()
//                                longitude = saddarlongitude.toString()

                }
            }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkOut() {
        showProgressDialog(true)
        if (Utils.hasInternetConnection()) {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                        requireContext()
                    )
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    fusedLocationClient.getCurrentLocation(
                        PRIORITY_HIGH_ACCURACY,
                        object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                CancellationTokenSource().token

                            override fun isCancellationRequested() = false
                        })
                        .addOnSuccessListener { location: Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location == null)
                                Toast.makeText(
                                    context,
                                    "Cannot get location.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            else
                            {

                                latitude = location.latitude.toString()
                                longitude = location.longitude.toString()

                                if (appliedDaysStatus) {

                                    if (employeeRecievedLocation == null) {
                                        employeeLocation = "Work from home"

                                    }
                                    else {
                                        employeeLocation = employeeRecievedLocation
                                        toDate = recievedTodate
                                        fromDate = recievedFromdate
                                    }
                                }
                                else {
                                    employeeLocation = "Office"
                                }
                                hanldeLateSittingCheckout()
                            }
                        }
                } else {
                    showProgressDialog(false)
                    showGeneralDialogFragment(
                        context = requireContext(),
                        bottom = true,
                        click = this,
                        setCancelAble = true,
                        yourTitle = "",
                        yourMessage = "Please turn on your location",
                        value = "",
                        location = this,
                        icon = R.drawable.reject
                    )
                }

            } else {
                showProgressDialog(false)
                requestPermissions()
            }


        } else {

            showGeneralDialogFragment(
                requireContext(),
                false,
                this,
                true,
                "yes",
                yourMessage = "No internet Connection",
                yourTitle = "",
                location = this,
                icon = R.drawable.reject
            )
        }
    }

    private fun hanldeLateSittingCheckout() {
        if(isUserLateSitting)
        {
            currentTime = MyHelperClass.getCurrentTimeAMPM()
            // formattedLastCheckout =  LocalTime.parse(currentTime, DateTimeFormatter.ofPattern("hh:mm a"))
            showProgressDialog(false)
            forgotToCheckout = true
            showCheckoutLateSittingPopUp()
        }
        else
        {
            userSelectedTime = ""
            forgottocheckout = true
            forgotToCheckout = false
            sendUserAttendanceInformation()
        }

    }

    private fun sendUserAttendanceInformation() {
        sessionmanagement = SessionManagement(requireContext())
        empId = sessionmanagement!!.getEmpId()
        userComment = comment?.text.toString()
        if (userComment.isEmpty()) {
            userComment = "--"
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month =
            calendar.get(Calendar.MONTH) + 1  // Note: Calendar.MONTH is zero-based
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        val f: NumberFormat = DecimalFormat("00")
        val datecur = "$year-${f.format(month)}-${f.format(day)}";
        if (!appliedDaysStatus && forgottocheckout) {
            if (employeeLocation.lowercase().contains("office")) {
                toDate = datecur
                fromDate = datecur
            }
            val attendancerequest = AddAttendanceRequestModel(
                employeeID = empId,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble(),
                status = status,
                comment = userComment,
                employeeLocation = employeeLocation,
                fromDate = fromDate,
                toDate = toDate,
                noofDays = noOfDays,
                selectedcheckouttime=userSelectedTime,
                isforgotcheckout = forgotToCheckout
            )
            LoggerGenratter.getInstance().printLog(TAG, attendancerequest.toString())
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getAttendanceResponse(attendancerequest)
            }
        }
        else if(!appliedDaysStatus && !forgottocheckout){
            if(employeeLocation.lowercase().contains("office")&&!isLateSittingEnabled)
            {
                val attendancerequest = AddAttendanceRequestModel(
                    employeeID = empId,
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                    status = status,
                    comment = userComment,
                    employeeLocation = employeeLocation,
                    fromDate = forgottocheckoutdate,
                    toDate = forgottocheckoutdate,
                    noofDays = noOfDays,
                    selectedcheckouttime=userSelectedTime,
                    isforgotcheckout = forgotToCheckout
                )
                LoggerGenratter.getInstance().printLog(TAG, attendancerequest.toString())

                CoroutineScope(Dispatchers.Main).launch {
                    homeViewModel.getAttendanceResponse(attendancerequest)
                }
            }

        }
        else{
            val attendancerequest = AddAttendanceRequestModel(
                employeeID = empId,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble(),
                status = status,
                comment = userComment,
                employeeLocation = employeeLocation,
                fromDate = fromDate,
                toDate = toDate,
                noofDays = noOfDays,
                selectedcheckouttime = userSelectedTime,
                isforgotcheckout = forgotToCheckout
            )
            LoggerGenratter.getInstance()
                .printLog(TAG, attendancerequest.toString())
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getAttendanceResponse(attendancerequest)
            }
        }
    }

    private fun checkLocation() {

        lifecycleScope.launch(Dispatchers.IO) {
            var isOffice = false
            kotlin.run Out@{
                officesLocationResponse.forEach { office ->
                    office?.let {
                        val distance = getOfficeDistance(
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                        distance?.let { dis ->
                            officesLocationResponse.forEach { item ->
                                item?.radius?.let { radius ->
                                    if (dis <= radius) {
                                        isOffice = true
                                        return@Out
                                    }
                                }
                            }
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                if (isOffice){
                    if (appliedDaysStatus) {
                        if(recievedTodate!=null&&recievedFromdate!=null)
                        {
                            toDate = recievedTodate
                            fromDate = recievedFromdate
                        }

                    }
                    employeeLocation = "Office"
                    sendUserAttendanceInformation()
                }else {
                    if (appliedDaysStatus) {
                        if(recievedTodate!=null&&recievedFromdate!=null)
                        {
                            toDate = recievedTodate
                            fromDate = recievedFromdate
                        }
                        employeeLocation = employeeRecievedLocation.ifEmpty {
                            "other"
                        }
                        sendUserAttendanceInformation()
                    } else {

                        showProgressDialog(false)
                        initUserStatusBottomSheet()
                    }
                }
            }
        }


    }


    private fun initUserStatusBottomSheet() {
        val userstatusDialog = Dialog(requireContext())
        userstatusDialog.setCancelable(true)
        userstatusDialog.setCanceledOnTouchOutside(true)
        userstatusDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        userstatusDialog.setContentView(R.layout.userstatusbottomsheet)
        userstatusDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        //val radiogroup: RadioGroupPlus = userstatusDialog.findViewById(R.id.radio_group)
        val radioGroupDays: RadioGroupPlus =
            userstatusDialog.findViewById(R.id.radioGroupDays)
        val submit: ConstraintLayout = userstatusDialog.findViewById(R.id.submitt)
        val cardComment: MaterialCardView =
            userstatusDialog.findViewById(R.id.cardComment)
        comment = userstatusDialog.findViewById(R.id.edtComments) as EditText
        val cardViewDays: MaterialCardView =
            userstatusDialog.findViewById(R.id.cardViewDays)
        val cardViewFrom: MaterialCardView =
            userstatusDialog.findViewById(R.id.cardViewFrom)
        val cardViewTo: MaterialCardView =
            userstatusDialog.findViewById(R.id.cardViewTo)
        val tvFromDate: TextView = userstatusDialog.findViewById(R.id.tvFromDate)
        val tvToDate: TextView = userstatusDialog.findViewById(R.id.tvToDate)
        val recyclerRadio =
            userstatusDialog.findViewById<RecyclerView>(R.id.title_recycler)
        val multiDayRadioLayout =
            userstatusDialog.findViewById<ConstraintLayout>(R.id.secondlayout1)
        val dateLayout = userstatusDialog.findViewById<ConstraintLayout>(R.id.layoutDays)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month =
            calendar.get(Calendar.MONTH) + 1  // Note: Calendar.MONTH is zero-based
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        val f: NumberFormat = DecimalFormat("00")
        val datecur = "$year-${f.format(month)}-${f.format(day)}";
        noOfDays = "1"
        userComment = ""
        val maxLength = sessionmanagement!!.getCharacterLimit()
        characterLimit = maxLength
        val inputFilter = InputFilter.LengthFilter(maxLength)
        comment!!.filters = arrayOf(inputFilter)
        toDate = datecur
        fromDate = datecur
        tvFromDate.text = datecur
        tvToDate.text = datecur
        val layoutManager = GridLayoutManager(
            requireContext(), 2,
        )
        if (attendanceTypeOffice.isNotEmpty()) {
            dateLayout.hide()
            attendanceTypeOffice.forEach { it?.isCheck = false }
            attendanceTypeOffice[0]?.let {
                it.isCheck = true
                if (it.hasComments == true) cardComment.show() else cardComment.hide()
                if (it.hasMultipleDays == true) {
                    multiDayRadioLayout.show()
                } else multiDayRadioLayout.hide()
                employeeLocation = it.title.toString()
            }
        }

        val radioAdapter = AttendanceTypeRadioAdapter(
            attendanceTypeOffice,
            object : AttendanceTypeRadioAdapter.OnClickedItem {
                override fun onClick(position: Int, ischeck: Boolean) {
                    val item = attendanceTypeOffice[position]
                    item?.let { item ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            attendanceTypeOffice.forEach {
                                it?.let {
                                    if (item.id == it.id) {
                                        it.isCheck = ischeck
                                    } else {
                                        it.isCheck = false
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                recyclerRadio.adapter?.notifyDataSetChanged()
                                if (item.hasComments == true) cardComment.show() else cardComment.hide()
                                if (item.hasMultipleDays == true) {
                                    multiDayRadioLayout.show()
                                } else multiDayRadioLayout.hide()
                                radioGroupDays.check(R.id.radioOneDay)
                                employeeLocation = item.title.toString()
                            }
                        }
                    }
                }
            }
        )
        recyclerRadio.layoutManager = layoutManager
        recyclerRadio.adapter = radioAdapter
        submit.setSafeOnClickListener {
            showProgressDialog(true)
            if (radioGroupDays.checkedRadioButtonId == R.id.radioMDay) {
                val validationresult = validateDatesInput()
                if (validationresult.first) {
                    var usercomment = comment!!.text.toString()
                    if (usercomment.isNotEmpty()) {
                        sendUserAttendanceInformation()
                        userstatusDialog.dismiss()
                    }
                    else
                    {

                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "Please enter reason",
                            location = this,
                            icon = R.drawable.reject
                        )
                        showProgressDialog(false)

                    }
                } else {
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        false,
                        "yes",
                        yourTitle = "",
                        yourMessage = "${validationresult.second}",
                        location = this,
                        icon = R.drawable.reject
                    )

                }
            } else {
                var usercomment = comment!!.text.toString()
                if (usercomment.isNotEmpty()) {
                    sendUserAttendanceInformation()
                    userstatusDialog.dismiss()
                }
                else {
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        false,
                        "yes",
                        yourTitle = "",
                        yourMessage = "Please enter reason",
                        location = this,
                        icon = R.drawable.reject
                    )
                    showProgressDialog(false)
                }
            }


        }
        radioGroupDays.check(R.id.radioOneDay)
        radioGroupDays.setOnCheckedChangeListener(RadioGroupPlus.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioOneDay -> {
                    noOfDays = "1"
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month =
                        calendar.get(Calendar.MONTH) + 1  // Note: Calendar.MONTH is zero-based
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val f: NumberFormat = DecimalFormat("00")
                    val datecur = "$year-${f.format(month)}-${f.format(day)}";
                    toDate = datecur
                    fromDate = datecur
                    dateLayout.hide()
                }

                R.id.radioMDay -> {
                    dateLayout.show()
                }
            }
        })
        cardViewFrom.setSafeOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val from = activity?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        // Display Selected date in textbox
                        val day = dayOfMonth
                        val months = monthOfYear + 1
                        fromDate = if (months < 10 && day < 10) {
                            "$year-0$months-0$day"
                        } else if (day < 10) {
                            "$year-$months-0$day"
                        } else if (months < 10) {
                            "$year-0$months-$day"
                        } else {

                            "$year-$months-$day"
                        }

                        val to = activity?.let {
                            DatePickerDialog(
                                it,
                                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                                    // Display Selected date in textbox
                                    val dayss = dayOfMonth
                                    val month = monthOfYear + 1

                                    toDate = if (month < 10 && dayss < 10) {
                                        "$year-0$month-0$dayss"
                                    } else if (dayss < 10) {
                                        "$year-$month-0$dayss"
                                    } else if (month < 10) {
                                        "$year-0$month-$dayss"
                                    } else {

                                        "$year-$month-$dayss"
                                    }

                                    tvFromDate.text = fromDate
                                    tvToDate.text = toDate

                                    val mDateFormat = SimpleDateFormat("yyyy-MM-dd")

                                    // Converting the dates
                                    // from string to date format
                                    val mDate11 = mDateFormat.parse(fromDate)
                                    val mDate22 = mDateFormat.parse(toDate)

                                    val mDifference =
                                        kotlin.math.abs(mDate11.time - mDate22.time)

                                    // Converting milli seconds to dates
                                    val mDifferenceDates =
                                        mDifference / (24 * 60 * 60 * 1000)

                                    // Converting the above integer to string
                                    noOfDays = (mDifferenceDates + 1).toString()
                                },
                                year,
                                month,
                                day
                            )
                        }

                        to?.datePicker?.minDate = System.currentTimeMillis() - 1000
                        to?.setTitle("To")

                        to?.show()
                    },
                    year,
                    month,
                    day
                )
            }

            from?.datePicker?.minDate = System.currentTimeMillis() - 1000
            from?.datePicker?.maxDate = System.currentTimeMillis() - 1000
            from?.setTitle("From")

            from?.show()

        }

        cardViewTo.setSafeOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val to = activity?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        // Display Selected date in textbox
                        val dayss = dayOfMonth
                        val month = monthOfYear + 1

                        toDate = if (month < 10 && dayss < 10) {
                            "$year-0$month-0$dayss"
                        } else if (dayss < 10) {
                            "$year-$month-0$dayss"
                        } else if (month < 10) {
                            "$year-0$month-$dayss"
                        } else {

                            "$year-$month-$dayss"
                        }

                        tvFromDate.text = fromDate
                        tvToDate.text = toDate

                        val mDateFormat = SimpleDateFormat("yyyy-MM-dd")

                        // Converting the dates
                        // from string to date format
                        val mDate11 = mDateFormat.parse(fromDate)
                        val mDate22 = mDateFormat.parse(toDate)

                        val mDifference =
                            kotlin.math.abs(mDate11.time - mDate22.time)

                        // Converting milli seconds to dates
                        val mDifferenceDates =
                            mDifference / (24 * 60 * 60 * 1000)

                        // Converting the above integer to string
                        noOfDays = (mDifferenceDates + 1).toString()
                    },
                    year,
                    month,
                    day
                )
            }

            to?.datePicker?.minDate = System.currentTimeMillis() - 1000
            to?.setTitle("To")

            to?.show()

        }
        userstatusDialog.show()
    }
    private fun getOfficeDistance(
        latitudeOffice: String,
        longitudeOffice: String
    ): Double? {
        try {
            val latLngA = LatLng(latitude.toDouble(), longitude.toDouble())
            val latLngB =
                LatLng(latitudeOffice.toDouble(), longitudeOffice.toDouble())
            val locationA = Location("point A")
            locationA.latitude = latLngA.latitude
            locationA.longitude = latLngA.longitude
            val locationB = Location("point B")
            locationB.latitude = latLngB.latitude
            locationB.longitude = latLngB.longitude

            val distance = locationA.distanceTo(locationB).toDouble()
            //                val miles = distance * 0.000621371
            //                val meters = milesToMeters(miles)
            return distance
        } catch (exception: Exception) {
            return null
        }

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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (isLocationEnabled()) {
                    notificationPermissionCheckToStartService()
                    if(lastActivty.contains("CheckIn"))
                    {
                        checkOut()
                    }
                    else{
                        checkIn()
                    }

                } else {
                    showGeneralDialogFragment(
                        context = requireContext(),
                        bottom = true,
                        click = this,
                        setCancelAble = true,
                        yourTitle = "",
                        yourMessage = "Please turn on your location",
                        value = "",
                        location = this,
                        icon = R.drawable.reject
                    )
                }
            } else {
                goToSettings()
            }
        }
    }


    private fun goToSettings() {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context?.packageName)
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(myAppSettings)
    }

    override fun onclick(value: Boolean)
    {

    }
    override fun onDestroyView() {
        try{
            super.onDestroyView()
            homeViewModel._attendanceresponse.postValue(null)
            homeViewModel._getdashboardinformation.postValue(null)
            homeViewModel._dashboardSettingResponse.postValue(null)
            homeViewModel._getGraphFilter.postValue(null)
            homeViewModel._getStatisticsGraph.postValue(null)
            homeViewModel._latesittingstatusresponse.postValue(null)
            homeViewModel._latesittingcheckin.postValue(null)
            filterDialog=null
        }
        catch (e:java.lang.Exception)
        {
        }

    }

    override fun onDetach() {
        try{
            super.onDetach()
            if(receiver!=null)
            {
                LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver!!);
            }
            if (filterDialog != null)
                filterDialog!!.dismiss()
        }
        catch (e:Exception)
        {

        }

    }

    private fun validateDatesInput(): Pair<Boolean, String> {
        var inputtodate = toDate
        var inputfromdate = fromDate

        return homeViewModel.validateDateCredentials(inputtodate, inputfromdate)
    }

    override fun onLocationPermission(locationvalue: Boolean) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun showCheckInCheckOutDialog(isCheckIn: Boolean, text: String) {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.checkin_out_dialog)
//        logoutDialog.window?.setWindowAnimations(R.style.DialogNoAnimation)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val tvMessage: TextView = logoutDialog.findViewById(R.id.txtsuccess)
        tvMessage.text = "Are you sure you want to $text?"

        yes.setSafeOnClickListener {

            if (isCheckIn) {
                checkIn()
            } else {
                callLateSittingStatusApi()
            }

            logoutDialog.dismiss()
        }

        no.setSafeOnClickListener {

            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

    private fun callLateSittingStatusApi() {
        showProgressDialog(true)
        var request = LateSittingStatusRequest(employee_ID = sessionmanagement!!.getEmpId())
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getLateSittingStatus(request)
        }
    }

    override fun onGroupClick(position: Int) {
        addeddataresponselist?.clear()
        var department = departmentgrouplist?.get(position)?.name
        if (deptdataresponselist != null) {
            for (i in deptdataresponselist!!) {
                if (i.group.contains(department.toString())) {
                    addeddataresponselist?.add(
                        BodyXX(
                            i.absentCount,
                            i.departmentName,
                            i.group,
                            i.lateCount,
                            i.leaveCount,
                            i.presentCount,
                            i.departmentId
                        )
                    )
                }
            }
        }

        if (addeddataresponselist?.size != 0) {
            if (addeddataresponselist?.size == 1) {
                binding.rvdepartmentattendance.layoutParams.height = 222
            } else {
                binding.rvdepartmentattendance.layoutParams.height = 445
            }
            val adapter =
                DepartmentEmployeeAdapter(
                    requireContext(),
                    addeddataresponselist,
                    this,
                    department,
                    "home"
                )
            binding.rvdepartmentattendance.adapter = adapter

        }
    }
    override fun ondeptitemclick(position: Int, from: String) {
        if (from == "home") {
            var departmentid = 0
            sessionmanagement?.setSelectedDeptId(position)

            var departmentname = addeddataresponselist?.get(position)?.departmentName
            var string = ""
            deptdataresponselist?.forEach {
                if (departmentname == it.departmentName) {
                    departmentid = it.departmentId
                }
            }
            sessionmanagement?.setDepartmentId(departmentid)
            var status = ""
            if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                status = "Absent"
            } else {
                status = "Present"
            }
            val employeeinfo = EmployeeAttendanceInfo(
                name = sessionmanagement?.getName(),
                employeeID = sessionmanagement?.getEmpId()?.toInt(),
                designation = sessionmanagement?.getDesignation(),
                firstCheckIn = empStatusCheckIn,
                lastCheckOut = empStatusCheckOut,
                depID = position,
                remarks = status,
                status = status,
                department = departmentname,
                from = "home",
                date = currentDateCto,
                verticalsList = departmentgrouplist,
                alldeptlist = deptdataresponselist
            )
            try{
                findNavController().navigate(
                    HomeFragmentDirections.actionHomefragmentToEmployeeAttendance(
                        employeeinfo
                    )
                )}catch (_: Exception){}

        }
    }

    private fun showCheckoutLateSittingPopUp() {
        val userstatusDialog = Dialog(requireContext())
        userstatusDialog.setCancelable(true)
        userstatusDialog.setCanceledOnTouchOutside(true)
        userstatusDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        userstatusDialog.setContentView(R.layout.latesittingpopup)
        userstatusDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        val radioGroupDays: RadioGroupPlus = userstatusDialog.findViewById(R.id.radioGroupDaysLateSitting)
        val submit:ConstraintLayout= userstatusDialog.findViewById(R.id.submittLateSitting)
        var forgottoCheckoutLayout:ConstraintLayout= userstatusDialog.findViewById(R.id.firstlayout1)
        val checkouttimelayout: MaterialCardView = userstatusDialog.findViewById(R.id.mcvcheckouttimelatesitting)
        var checkouttext:TextView = userstatusDialog.findViewById(R.id.txtcheckouttimelatesitting)
        val radioLateSitting:RadioButton = userstatusDialog.findViewById(R.id.radioLateSitting)
        val radioForgottoCheckOut:RadioButton = userstatusDialog.findViewById(R.id.radioforgottocheckout)
        selectedCheckOutTime = userstatusDialog.findViewById(R.id.txtselectedcheckouttime) as TextView
        selectedCheckOutTime!!.text = currentTime
        var date =  MyHelperClass.getCurrentDateYYYYMMDD()
        var time = MyHelperClass.convertTimeFormatCurrent(currentTime)
        userSelectedTime = "$forgottocheckoutdate $time"
        var isLateSittingCheckIn =   sessionmanagement?.getLateSittingStatus()
        if(isLateSittingCheckIn == true)
        {
            forgottoCheckoutLayout.hide()
            forgottocheckout = true
            isLateSittingEnabled = true
            radioGroupDays.check(R.id.radioLateSitting)
        }
        else{
            forgottoCheckoutLayout.show()
            forgottocheckout = false
            isLateSittingEnabled = false
            radioGroupDays.check(R.id.radioforgottocheckout)
        }

        radioGroupDays.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioforgottocheckout ->
                {
                    forgottocheckout = false
                    radioLateSitting.isChecked = false
                    isLateSittingEnabled= false
                    checkouttimelayout.show()
                }
                R.id.radioLateSitting->
                {
                    forgottocheckout = true
                    radioForgottoCheckOut.isChecked = false
                    radioLateSitting.isChecked = true
                    isLateSittingEnabled = true
                    checkouttimelayout.hide()
                }
            }
        }
        checkouttimelayout.setOnClickListener {
            showLateSittingTimePicker()
        }
        submit.setOnClickListener {
            if(isLateSittingEnabled)
            {
                try {
                    userstatusDialog.dismiss()
                    sessionmanagement?.setLatitude(latitude)
                    sessionmanagement?.setLongitude(longitude)
                    sessionmanagement?.setEmployeeLoc(employeeLocation)
                    sessionmanagement?.setStatus(status)
                    sessionmanagement?.setNoOfDays(noOfDays)
                    sessionmanagement?.setIsApprovalLayout("home")
                    if(employeeLocation.lowercase().contains("work from home")||employeeLocation.lowercase().contains("client visit")||employeeLocation.lowercase().contains("work from home"))
                    {
                        sessionmanagement?.setCheckInDate("$recievedFromdate")
                      //  sessionmanagement?.setCheckOutDate("$recievedTodate")

                    }
                    else{
                        sessionmanagement?.setCheckInDate("$forgottocheckoutdate")
                     //   sessionmanagement?.setCheckOutDate("$forgottocheckoutdate")
                    }


                    findNavController().navigate(R.id.action_homefragment_to_lateSittingDetails)
                }
                catch (e:java.lang.Exception)
                {
                }
            }
            else{
//                var validationresult = validatsTimeInput()
//                if(validationresult.first)
//                {
//                    userstatusDialog.dismiss()
//                    showProgressDialog(true)
//                    sendUserAttendanceInformation()
//                }
//
//                else
//                {
//                    showGeneralDialogFragment(
//                        requireContext(),
//                        false,
//                        this,
//                        false,
//                        "yes",
//                        yourTitle = "",
//                        yourMessage = validationresult.second,
//                        location = this,
//                        icon = R.drawable.reject
//                    )
//                }
                userstatusDialog.dismiss()
                showProgressDialog(true)
                sendUserAttendanceInformation()
            }
        }
        userstatusDialog.show()
    }
    private fun showLateSittingTimePicker()
    {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Check if the selected time is greater than the current time
                var selectedTime = formatTime(selectedHour, selectedMinute)
                //  formattedLastCheckout =  LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("hh:mm a"))
                var selectedTime24Format = MyHelperClass.formatTimeIn24Format(selectedHour, selectedMinute)
                selectedCheckOutTime?.text = selectedTime
                userSelectedTime = "$forgottocheckoutdate $selectedTime24Format"
            },
            currentHour,
            currentMinute,
            false // Set true if you want to use 24-hour format
        )
        timePickerDialog.show()
    }

//    private fun validatsTimeInput(): Pair<Boolean, String> {
//        var inputcheckintime = formattedFirstCheckin
//        var inputcheckouttime = formattedLastCheckout
//        return homeViewModel.validateTimeCredentials(inputcheckintime, inputcheckouttime)
//    }

    fun onclick(itemid: Int?) {
        var viewid = sessionmanagement?.getViewId()

        if (itemid == 1) {
            if (viewid == 1) {
                var status = ""
                if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                    status = "Absent"
                } else {
                    status = "Present"
                }
                val employeeinfo = EmployeeAttendanceInfo(
                    name = sessionmanagement?.getName(),
                    employeeID = sessionmanagement?.getEmpId()?.toInt(),
                    designation = sessionmanagement?.getDesignation(),
                    firstCheckIn = empStatusCheckIn,
                    lastCheckOut = empStatusCheckOut,
                    depID = sessionmanagement?.getDepartmentId(),
                    remarks = status,
                    status = status,
                    from = "home",
                    date = currentDateCto,
                    verticalsList = departmentgrouplist,
                    alldeptlist = deptdataresponselist
                )
                try {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomefragmentToEmployeeDetail(
                            employeeinfo
                        )
                    )
                }
                catch (e:java.lang.Exception)
                {

                }

            }
            if (viewid == 2) {
                var status = ""
                if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                    status = "Absent"
                } else {
                    status = "Present"
                }
                val employeeinfo = EmployeeAttendanceInfo(
                    name = sessionmanagement?.getName(),
                    employeeID = sessionmanagement?.getEmpId()?.toInt(),
                    designation = sessionmanagement?.getDesignation(),
                    firstCheckIn = empStatusCheckIn,
                    lastCheckOut = empStatusCheckOut,
                    depID = sessionmanagement?.getDepartmentId(),
                    remarks = status,
                    status = status,
                    from = "home",
                    date = currentDateCto,
                    verticalsList = departmentgrouplist,
                    alldeptlist = deptdataresponselist

                )
                try {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomefragmentToEmployeeDetail(
                            employeeinfo
                        )
                    )
                }
                catch (e:java.lang.Exception)
                {

                }

            }
            if (viewid == 3) {
                var status = ""
                if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                    status = "Absent"
                } else {
                    status = "Present"
                }
                val employeeinfo = EmployeeAttendanceInfo(
                    name = sessionmanagement?.getName(),
                    employeeID = sessionmanagement?.getEmpId()?.toInt(),
                    designation = sessionmanagement?.getDesignation(),
                    firstCheckIn = empStatusCheckIn,
                    lastCheckOut = empStatusCheckOut,
                    depID = sessionmanagement?.getDepartmentId(),
                    remarks = status,
                    status = status,
                    from = "home",
                    date = currentDateCto,
                    verticalsList = departmentgrouplist,
                    alldeptlist = deptdataresponselist
                )
                try {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomefragmentToEmployeeDetail(
                            employeeinfo
                        )
                    )
                }
                catch (e:Exception)
                {

                }
            }
            if (viewid == 4) {
                var status = ""
                if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
                    status = "Absent"
                } else {
                    status = "Present"
                }
                val employeeinfo = EmployeeAttendanceInfo(
                    name = sessionmanagement?.getName(),
                    employeeID = sessionmanagement?.getEmpId()?.toInt(),
                    designation = sessionmanagement?.getDesignation(),
                    firstCheckIn = empStatusCheckIn,
                    lastCheckOut = empStatusCheckOut,
                    depID = sessionmanagement?.getDepartmentId(),
                    remarks = status,
                    status = status,
                    from = "home",
                    date = currentDateCto,
                    verticalsList = departmentgrouplist,
                    alldeptlist = deptdataresponselist
                )
                try {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomefragmentToEmployeeDetail(
                            employeeinfo
                        )
                    )
                }
                catch (e:java.lang.Exception)
                {

                }

            }
        }
        else if (itemid == 3) {
            try {
                findNavController().navigate(R.id.action_homefragment_to_leavemanagement)

            }
            catch (e:Exception)
            {
            }

        }
        else if(itemid == 5)
        {
            try {
                findNavController().navigate(R.id.action_homefragment_to_policiesfragment)

            }
            catch (e:Exception)
            {
            }
        }
        else if(itemid == 8)
        {
            try {
                findNavController().navigate(R.id.action_homefragment_to_policiesfragment)

            }
            catch(e:Exception)
            {

            }
        }


        else{

            showGeneralDialogFragment(
                requireContext(),
                false,
                this,
                true,
                "no",
                yourMessage = "Coming Soon",
                yourTitle = "",
                location = this,
                icon = R.drawable.reject,
                code = ""
            )
        }
    }

    override fun onAttendanceItemClick(position: Int) {

        var status = ""
        if (dashboarddata?.body?.getUserDash?.firstCheckIn == null) {
            status = "Absent"
        } else {
            status = "Present"
        }

        val employeeinfo = EmployeeAttendanceInfo(
            name = sessionmanagement?.getName(),
            employeeID = sessionmanagement?.getEmpId()?.toInt(),
            designation = sessionmanagement?.getDesignation(),
            firstCheckIn = empStatusCheckIn,
            lastCheckOut = empStatusCheckOut,
            depID = sessionmanagement?.getDepartmentId(),
            remarks = status,
            status = status,
            from = "home",
            date = currentDateCto,
            verticalsList = departmentgrouplist,
            alldeptlist = deptdataresponselist

        )
        try {
            findNavController().navigate(
                HomeFragmentDirections.actionHomefragmentToEmployeeAttendance(
                    employeeinfo
                )
            )
        }
        catch (e:java.lang.Exception)
        {

        }
    }
    override fun onDataReceived(data: String?) {
        var viewId = sessionmanagement?.getViewId()
        when (viewId) {
            2, 4 -> {
                managerview = true
                callManagerDasboardApi()
            }

            1 -> {
                managerview = false
                callManagerDasboardApi()
            }

            3 -> {
                callWeekAttendanceApi()
            }

            else -> {

            }
        }

        if (data != null) {
            when (data.lowercase()) {
                "checkout" -> {
                    binding.checkincheckout.setText("Check-in")
                }

                "checkin" -> {
                    binding.checkincheckout.setText("Check-out")
                }

                else -> {}
            }
        }
    }

    fun statisticsList(from: String) {
        currentClick = ""
        var From = from
        val dateFormatPattern = "yyyy-MM-dd"
        val dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern)
        val localDate = LocalDate.parse(dateforstatisticsgraph, dateFormatter)

        val statslist = StatisticAttendanceInfo(
            from = From,
            ctoAndManagerModelresponceSummery = entriesList,
            employeedetailModelresponces = employeedetailresponces,
            genderlist = genderid,
            designationlist = designationid,
            departmentlist = departmentid,
            officelist = officeid,
            date = localDate,
            grouplist = groupid
        )
        statslist?.let {
            try
            {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomefragmentToStatisticsFragment(statslist)
                )}
            catch (_: Exception){}
        }
    }
    private fun formatTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(cal.time)
    }
    private fun showNotificationDialog() {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(false)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.newdialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val txt: TextView = logoutDialog.findViewById(R.id.txtsuccess)
        val ok: ConstraintLayout = logoutDialog.findViewById(R.id.ok)
        //   val img:ImageView=logoutDialog.findViewById()
        txt.text = "Please allow notification permission from settings"

        ok.setSafeOnClickListener {
            logoutDialog.dismiss()
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", requireActivity().packageName)

//        startActivity(intent)
            startActivityForResult(intent, NOTIFICATION_SETTINGS_REQUEST_CODE)
        }
        logoutDialog.show()
    }

}


