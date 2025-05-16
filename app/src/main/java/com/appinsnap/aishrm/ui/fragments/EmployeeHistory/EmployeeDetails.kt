package com.appinsnap.aishrm.ui.fragments.EmployeeHistory

import android.Manifest
import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentEmployeeDetailsBinding
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.adapter.CalendarAdapter
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.DownloadAttendanceReq
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestAttendanceInformation
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXXX
import com.appinsnap.aishrm.ui.fragments.home.models.WeekAttendanceRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.WeeklyAttendanceresponce
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.AppUtils
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.google.android.material.card.MaterialCardView
import com.wallet.zindigi.Utils.PermissionsUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
@AndroidEntryPoint
class EmployeeDetails : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission, CalendarAdapter.onCalendarDate {
    private var sessionmanagement: SessionManagement? = null
    private var toDate: String = ""
    private var requesttodate: String = ""
    private var requestfromdate: String = ""
    var comment: String = ""
    private var lateSittingMinutes:String=""
    var from: String = ""
    private var reqResubmissionAllow:Boolean = false
    private var userAttendanceStatus:String=""
    private var userRequestDate:String = ""
    private var fromDate: String = ""
    var current =  LocalDate.now()
    var calendar = Calendar.getInstance()
    var daysInMonth =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    var datecurrent = LocalDate.now()
    private var tvToDate: TextView? = null
    private var tvFromDate: TextView? = null
    var noOfDays: String = "1"
    var monthLetters: String = ""


    var month:Int = 0
    private var presentcount: Int = 0
    private var leavescount: Int = 0
    var edittextcomments: EditText? = null
    var count = 0
    var datechecklist: List<WeeklyAttendanceresponce>? = null
    var yearletters: String = ""
    private var latearrivalcount: Int = 0
    private var absentcount: Int = 0
    var employeeid: String = ""
    private var employeedata: EmployeeAttendanceInfo? = null
    var checkinoutlist: BodyXXX? = null
    var Mycalendar = Calendar.getInstance()

    fun getdayinnt(name: String): Int {
        var boolean: Int = 0

        when (name) {
            "sunday" -> {
                boolean = 1
            }
            "monday" -> {
                boolean = 2
            }
            "tuesday" -> {
                boolean = 3
            }
            "wednesday" -> {
                boolean = 4
            }
            "thursday" -> {
                boolean = 5
            }
            "friday" -> {
                boolean = 6
            }
            "saturday" -> {
                boolean = 7
            }
        }
        return boolean
    }

    var currentDay = getdayinnt(
        getFirstDateDayName(
            Mycalendar.get(Calendar.YEAR),
            Mycalendar.get(Calendar.MONTH) + 1)
    )

    val navArg: EmployeeDetailsArgs by navArgs()
    private val days: ArrayList<Int> = ArrayList()
    var requestdate: String = ""
    private var _binding: FragmentEmployeeDetailsBinding? = null

    //use binding instance to avoid null check in every where in statement
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_employee_details, container, false)
        val view = binding.root
        //  return inflater.inflate(R.layout.fragment_employee_details, container, false)
        sessionmanagement = SessionManagement(requireContext())
        //    calendarHandling(responselist)
        if (arguments != null) {
            settingEmployeeStatusInfo()
        }
        settingCurrentDate()
        callApiForMonthlyAttendnace()
        hideDownloadButton()
        LiveDataObserver()
        //  initProjectDetailsAdapter()

//        if (navArg.empAtDetail?.status != null) {
//            settingAttendanceStatusColor(navArg.empAtDetail?.status)
//        }
        return view
    }

    private fun hideDownloadButton()
    {
        var attendanceRemarks =  sessionmanagement.let { it?.getAttendanceStatus() }
        var checkintime = sessionmanagement.let { it?.getCheckInTime() }
        var checkouttime = sessionmanagement.let { it?.getCheckoutTime() }
        if (attendanceRemarks != null ) {
            if (attendanceRemarks.isNotEmpty()) {
                settingAttendanceStatusColor(attendanceRemarks)
            } else {

            }
        }
        if (checkintime != null ) {
            if (checkintime.isNotEmpty()) {
                binding.txtcheckintime.show()
                binding.txtcheckintime.text = "Check-in" + " " + checkintime
            } else {
               binding.txtcheckintime.hide()
            }
        }
        if (checkouttime != null ) {
            if (checkouttime.isNotEmpty()) {
                binding.txtcheckouttime.show()
                binding.txtcheckouttime.text = "- " + " Check-out" + " " + checkouttime
            } else {
                binding.txtcheckouttime.hide()
            }
        }


        var viewid = sessionmanagement?.getViewId()
        if(viewid == 1)
        {
            if(navArg.empAtDetail?.employeeID.toString() == sessionmanagement?.getEmpId() )
            {
                binding.btndownload.show()
            }
            else{
                binding.btndownload.visibility = View.INVISIBLE
            }
        }
        else if(viewid == 2)
        {
            if(navArg.empAtDetail?.employeeID.toString() == sessionmanagement?.getEmpId() )
            {
                binding.btndownload.show()
            }
            else{
                binding.btndownload.visibility = View.INVISIBLE
            }
        }
        else if(viewid == 4)
        {
            if(navArg.empAtDetail?.employeeID.toString() == sessionmanagement?.getEmpId() )
            {
                binding.btndownload.show()
            }
            else
            {
                binding.btndownload.visibility = View.INVISIBLE
            }
        }
        else{
            binding.btndownload.show()
        }
    }

    private fun settingCurrentDate()
    {
        if (datecurrent == current) {
            binding.rightarrow.isEnabled = false
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_font
                ), PorterDuff.Mode.SRC_IN
            )
        }
        else{
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow
                ), PorterDuff.Mode.SRC_IN
            )
            binding.rightarrow.isEnabled = true
        }
        binding.mcvrequest.visibility = View.GONE


       var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        val monthText = DateFormatSymbols().months[month]
        val updatedMonth = calendar.get(Calendar.MONTH)
        monthLetters = monthText
        var convertedyear = year.toString()
        yearletters = convertedyear.takeLast(2)
        binding.txtcheckdate.text = "${monthText}" + " " + "${year}"
        binding.txtcalendarmonths.text = monthLetters.uppercase() + "'" + yearletters
    }

    private fun settingEmployeeStatusInfo()
    {
        binding.txtcheckouttime.visibility = View.VISIBLE
        binding.txtname.text = employeedata?.name
        binding.txtdesignation.text = employeedata?.designation
        binding.txtcheckintime.text = employeedata?.firstCheckIn.toString()
        binding.txtcheckouttime.text = employeedata?.lastCheckOut.toString()
        settingAttendanceStatusColor(employeedata?.remarks)
    }

    private fun settingAttendanceStatusColor(remarks: String?) {
        when (remarks?.lowercase()) {
            "p" -> {
                binding.txtattendancestatus.text = "Present"
                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.present_green
                    )
                )
                binding.txtcheckintime.show()
                binding.txtcheckouttime.show()
                binding.txtcheckintime.text = employeedata?.firstCheckIn.toString()
                binding.txtcheckouttime.text = employeedata?.lastCheckOut.toString()

            }

            "a" -> {
                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.absent_red
                    )
                )
                binding.txtcheckouttime.hide()
                binding.txtcheckintime.hide()
                binding.txtattendancestatus.text = "Absent"
            }

            "l" -> {
                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.leaves_blue
                    )
                )
                binding.txtcheckouttime.hide()
                binding.txtcheckintime.hide()
                binding.txtattendancestatus.text = "Leave"
            }

            "la" ->
            {
                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.latearrival_yellow
                    )
                )
                binding.txtcheckintime.show()
                binding.txtcheckouttime.show()
                binding.txtattendancestatus.text = "Late Arrival"
                binding.txtcheckintime.text = employeedata?.firstCheckIn.toString()
                binding.txtcheckouttime.text = employeedata?.lastCheckOut.toString()
            }

            "Work from Home" -> {
                sessionmanagement?.setAttendanceStatus("Work from Home")

                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.present_green
                    )
                )
                binding.txtcheckouttime.show()
                binding.txtcheckintime.show()
                binding.txtattendancestatus.text = "Work from Home"
                binding.txtcheckintime.text = employeedata?.firstCheckIn.toString()
                binding.txtcheckouttime.text = employeedata?.lastCheckOut.toString()

            }

            else ->{
                binding.txtattendancestatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.txtcheckouttime.hide()
                binding.txtcheckintime.hide()
                binding.txtattendancestatus.text = ""
            }
        }
    }

    private fun LiveDataObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //  showProgressDialog(it)
            showProgressDialog(it)
        }
        )

        homeViewModel._weekattendanceresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        days.clear()
                        calendarHandling(response.data?.body)
                    }

                    is NetworkResult.Error -> {
                        days.clear()
                        calendarHandling(response.data?.body)
                    }

                    is NetworkResult.Loading -> {
                    }
                }

            })
        homeViewModel._downloadAttendence.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        try {
//                            val fileDir = requireContext().getExternalFilesDir(null)
                            val sdf = SimpleDateFormat("ddMMyyyyHHHmmss")
                            val cd = sdf.format(Date())
                            val filenametitle =
                                "AIS_${navArg.empAtDetail!!.employeeID}_${requestfromdate}_${requesttodate}"
                            val filename = filenametitle + "_.xlsx"

                            val file = File(requireActivity().getExternalFilesDir(null), filename)
                            val excel =
                                file.writeBytes(response.data!!) //  dont delete or update it , it create file from bytes
                            openfileIntent(file)

                        } catch (e: Exception) {
                            LoggerGenratter.getInstance().printLog("ATTENDANCE FILE", e.message)
                        }
                    }

                    is NetworkResult.Error -> {
                        LoggerGenratter.getInstance()
                            .printLog("ATTENDANCE FILE", "Something went wrong")
                    }

                    is NetworkResult.Loading -> {
                    }
                }
            })
        homeViewModel._requestattendanceemployee.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            true,
                            "yes",
                            yourMessage = response.data?.statusMessage.toString(),
                            yourTitle = "",
                            location = this,
                            icon = R.drawable.tickicon
                        )
                    }

                    is NetworkResult.Error -> {

                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            true,
                            "yes",
                            yourMessage = response.message.toString(),
                            yourTitle = "",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })

    }

    private fun openfileIntent(file: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireActivity().packageName + ".fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            requireActivity().startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            LoggerGenratter.getInstance().showToast(requireContext(), "No application found to open Excel files.")
        }
    }
    private fun callApiForMonthlyAttendnace() {
            if (navArg.empAtDetail?.employeeID.toString().length > 6) {
                employeeid = sessionmanagement?.getEmpId().toString()
            }

        else{
            employeeid = navArg.empAtDetail?.employeeID.toString()
        }
            var requestdays = daysInMonth
            var requestmonth = month
            var currentdate = LocalDate.now()
            val weekAttendanceRequest = WeekAttendanceRequestModel(
                employeeID = employeeid,
                date = datecurrent.toString(),
                days = requestdays
            )
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getWeekAttendance(weekAttendanceRequest)
            }
        }

    fun calendarHandling(responselist: BodyXXX?) {
        datechecklist = responselist?.weeklyAttendanceresponce
        checkinoutlist = responselist
        responselist?.weeklyAttendanceresponce?.forEach {
            try {
                var splitdatewithspace = it.date.split(" ")[0]
                var currentdate = MyHelperClass.getCurrentDateMMDDYYYY()

                    if (splitdatewithspace == currentdate) {
                        settingAttendanceStatusColor(it.remarks)
                        if (it.remarks != null) {
                            sessionmanagement?.setAttendanceStatus(it.remarks.lowercase())
                        }

                        if(it.firstCheckIn!=null)
                        {

                            var firstcheckin = MyHelperClass.convertToAMPM_24to12(it.firstCheckIn.split(" ").get(1))
                            sessionmanagement?.setCheckInTime(firstcheckin)
                            binding.txtcheckintime.text = "Check-in" + " " + firstcheckin
                            binding.txtcheckintime.show()
                        }
                        else{
                            binding.txtcheckintime.text = ""
                            binding.txtcheckintime.hide()
                        }
                        if(it.lastCheckOut!=null)
                        {

                            var lastcheckout = MyHelperClass.convertToAMPM_24to12(it.lastCheckOut.split(" ").get(1))
                            sessionmanagement?.setCheckoutTime(lastcheckout)
                            binding.txtcheckintime.show()
                            binding.txtcheckouttime.text = "- " + " Check-out" + " " + lastcheckout
                        }
                        else{
                            binding.txtcheckouttime.text = ""
                            binding.txtcheckouttime.hide()
                        }
                    }
                var splitdate = it.date.split("/")
                var date = splitdate[1]
                if (date < 10.toString()) {
                    var dates = date.get(1)
                    it.date = dates.toString()
                } else {
                    var dates = date
                    it.date = dates.toString()
                }
            } catch (e: Exception) {
                LoggerGenratter.getInstance().printLog("EMPLOYEE DETAILS", e.toString())
            }
        }
        dayHandle(currentDay)

        for (i in 1..daysInMonth) {
            days.add(i)
        }
        var empid:Int=0
        if(navArg?.empAtDetail?.employeeID!=null)
        {
            empid = navArg?.empAtDetail?.employeeID!!
        }

        val adapter = CalendarAdapter(
                requireContext(),
                currentDay,
                days,
                responselist,
                this,
                navArg?.empAtDetail?.employeeID!!,
                (calendar.get(Calendar.MONTH).toInt() + 1).toString(),
                calendar.get(Calendar.YEAR).toString()
            )
            binding.calendarGrid.adapter = adapter
        var statuscount = responselist?.weeklyAttendanceresponce
        if (responselist != null) {

            if (statuscount != null) {
                for (i in statuscount) {
                    calculatingemployeeAttendance(i.remarks)
                }
            }
        }
        binding.txtlatecalendar.text = "Late: " + latearrivalcount.toString()
        binding.txtabsentcalendar.text = "Absent: " + absentcount.toString()
        binding.txtleavecalendar.text = "Leaves: " + leavescount.toString()
        binding.txtpresentcalendar.text = "Present: " + presentcount.toString()
        latearrivalcount = 0
        absentcount = 0
        leavescount = 0
        presentcount = 0
        count = 0
    }

    private fun calculatingemployeeAttendance(remarks: String) {
        if(remarks!=null)
        {
            when (remarks.lowercase()) {
                "p" -> {
                    count = presentcount + 1
                    presentcount = count
                }

                "a" -> {
                    count = absentcount + 1
                    absentcount = count
                }

                "l" -> {
                    count = leavescount + 1
                    leavescount = count
                }

                "la" -> {
                    count = latearrivalcount + 1
                    latearrivalcount = count
                }

            }
        }

    }


    public fun dayHandle(currentday: Int) {
        if (currentday == 2) {

            days.add(0)

        }

        if (currentday == 3) {

            days.add(0)

            days.add(0)

        }

        if (currentday == 4) {

            days.add(0)

            days.add(0)

            days.add(0)

        }

        if (currentday == 5) {

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

        }

        if (currentday == 6) {

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

        }

        if (currentday == 7) {

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

            days.add(0)

        }

    }


    var status = "Check-in 00:00 Check-out 00:00"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        from = navArg.empAtDetail?.from.toString()
        if (navArg.empAtDetail != null) {
            binding.txtname.text = navArg.empAtDetail?.name.toString()
            binding.txtdesignation.text = navArg.empAtDetail?.designation

        } else {
            employeeid = sessionmanagement!!.getEmpId()
        }
        onClickListener()
    }

    private fun onClickListener() {
        binding.mcvrequest.setSafeOnClickListener {
           // showRequestDetailsPopup()
            if(reqResubmissionAllow)
            {
                showGeneralDialogFragment(
                    requireContext(),
                    false,
                    this,
                    true,
                    "yes",
                    yourMessage = "Request is already submitted on this date",
                    yourTitle = "",
                    location = this,
                    icon = R.drawable.reject
                )

            }

            else{
                val requestResubmission = RequestAttendanceInformation(attendancestatus = userAttendanceStatus,date=userRequestDate,empdetails = navArg.empAtDetail)
                try {
                    findNavController().navigate(EmployeeDetailsDirections.actionEmployeeDetailToRequestResubmission(requestResubmission))
                }
                catch(e:Exception)
                {

                }
            }

        }

        binding.rightarrow.setSafeOnClickListener {
            callMonthlyAttendanceApiForNext()
        }

        binding.leftarrow.setSafeOnClickListener {
            callMonthlyAttendanceApiForPrevious()
        }

        binding.btndownload.setSafeOnClickListener {
            checkPermission()
        }

    }


    private fun checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Camera Permission",
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
                            displayDownloadAttendancePopUp()
                        }

                        override fun onPermissionCancel() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Not Granted")
                        }
                    })
            }
        } else {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Storage Permission",
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
                            displayDownloadAttendancePopUp()
                        }

                        override fun onPermissionCancel() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Not Granted")
                        }
                    })
            }

        }

    }

    fun saveByteArrayAsExcel(byteArray: ByteArray, filePath: String) {
        try {
            val workbook = WorkbookFactory.create(byteArray.inputStream())
            val file = File(filePath)
            val outputStream = FileOutputStream(file)
            workbook.write(outputStream)

            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun makeApiCall(todate: String, fromedate: String) {
        var employeeid: String
        if (navArg.empAtDetail?.employeeID != null) {
            employeeid = navArg.empAtDetail?.employeeID.toString()
        } else {
            employeeid = sessionmanagement!!.getEmpId()
        }


        val downloadAttendanceReq = DownloadAttendanceReq(
            employeeID = employeeid,
            fromDate = fromedate,
            toDate = todate,
            dID = navArg.empAtDetail?.depID,
            IsSelf = true
        )

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.downloadAttendence(downloadAttendanceReq)
        }

    }

    private var txttodate: TextView? = null
    private var txtfromdate: TextView? = null
    private fun displayDownloadAttendancePopUp() {

        val downloadattendancedialog = Dialog(requireContext())
        downloadattendancedialog.setCancelable(true)
        downloadattendancedialog.setCanceledOnTouchOutside(true)
        downloadattendancedialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        downloadattendancedialog.setContentView(R.layout.downloadattendancedialog)
        downloadattendancedialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )

        val cancelimg: ImageView = downloadattendancedialog.findViewById(R.id.imgcancel)
        val mcvtodate: MaterialCardView = downloadattendancedialog.findViewById(R.id.mcvtoDate)
        val mcvfromdate: MaterialCardView = downloadattendancedialog.findViewById(R.id.mcvfromDate)
        val submitt: ConstraintLayout = downloadattendancedialog.findViewById(R.id.download)
        txttodate = downloadattendancedialog.findViewById(R.id.txttodatess)
        txtfromdate = downloadattendancedialog.findViewById(R.id.txtfromdate)


        cancelimg.setSafeOnClickListener {
            downloadattendancedialog.dismiss()
        }
        mcvtodate.setSafeOnClickListener {
            showToDateCalendar()
        }
        mcvfromdate.setSafeOnClickListener {
            showFromDateCalendar()
        }
        submitt.setSafeOnClickListener {

            val validationresult = validateDatesInput()
            if (validationresult.first) {

                toDate = ""
                fromDate = ""
                makeApiCall(txttodate?.text.toString(), txtfromdate?.text.toString())
                downloadattendancedialog.dismiss()

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

        }
        downloadattendancedialog.show()
    }

    var permissionsUtil: PermissionsUtil? = null
    fun getFirstDateDayName(year: Int, month: Int): String {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val dayName = firstDayOfMonth.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayName.lowercase()
    }

    private fun callMonthlyAttendanceApiForPrevious() {
        binding.currentdate.visibility = View.GONE
        binding.mcvrequest.visibility = View.GONE
        binding.extraHours.visibility = View.GONE
        binding.rightarrow.isEnabled = true
        binding.rightarrow.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.yellow),
            PorterDuff.Mode.SRC_IN
        )// = true
        val plus = datecurrent?.minusMonths(1)
        datecurrent = plus
        calendar.add(Calendar.MONTH, -1)
        val updatedMonth = calendar.get(Calendar.MONTH)
        val monthText = DateFormatSymbols().months[updatedMonth]
        monthLetters = monthText
        var year = calendar.get(Calendar.YEAR)
        var convertedyear = year.toString()
        yearletters = convertedyear.takeLast(2)
        binding.txtcalendarmonths.text = monthLetters.uppercase() + "'" + yearletters
        currentDay = getdayinnt(
            getFirstDateDayName(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1
            )
        )
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        binding.txtcheckdate.text = "${monthText}" + " " + "${year}"
        callApiForMonthlyAttendnace()
    }

    private fun callMonthlyAttendanceApiForNext() {
        binding.currentdate.visibility = View.GONE
        binding.extraHours.visibility = View.GONE
        binding.mcvrequest.visibility = View.GONE
        if (datecurrent == current) {
            binding.rightarrow.isEnabled = false
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_font
                ), PorterDuff.Mode.SRC_IN
            )
        } else {
            val plus = datecurrent?.plusMonths(1)
            datecurrent = plus
            if (datecurrent == current) {
                binding.rightarrow.isEnabled = false
                binding.rightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_font
                    ), PorterDuff.Mode.SRC_IN
                )
            }
            // = true

            var monthss = calendar.add(Calendar.MONTH, 1)
            val updatedMonth = calendar.get(Calendar.MONTH)
            var monthText = DateFormatSymbols().months[updatedMonth]
            monthLetters = monthText
            var year = calendar.get(Calendar.YEAR)
            var convertedyear = year.toString()
            yearletters = convertedyear.takeLast(2)
            binding.txtcalendarmonths.text = monthLetters.uppercase() + "'" + yearletters
            daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            binding.txtcheckdate.text = "${monthText}" + " " + "${year}"
            currentDay = getdayinnt(
                getFirstDateDayName(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1
                )
            )

            callApiForMonthlyAttendnace()

        }
    }

    private fun showToDateCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    toDate = toDates
                    tvToDate?.text = toDate
                    requesttodate = toDate
                    txttodate?.text = toDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    toDate = toDates
                    tvToDate?.text = toDate
                    requesttodate = toDate
                    txttodate?.text = toDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    toDate = toDates
                    requesttodate = toDate
                    tvToDate?.text = toDate
                    txttodate?.text = toDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    toDate = toDates
                    requesttodate = toDate
                    tvToDate?.text = toDate
                    txttodate?.text = toDate

                }

            },
            year,
            month,
            day
        )
        dpd?.datePicker?.maxDate = System.currentTimeMillis() - 1000

        dpd.show()

    }

    private fun showFromDateCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    fromDate = toDates
                    requestfromdate = fromDate
                    tvFromDate?.text = fromDate
                    txtfromdate?.text = fromDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    fromDate = toDates
                    requestfromdate = fromDate
                    tvFromDate?.text = fromDate
                    txtfromdate?.text = fromDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    fromDate = toDates
                    tvFromDate?.text = fromDate
                    txtfromdate?.text = fromDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    fromDate = toDates
                    requestfromdate = fromDate
                    tvFromDate?.text = fromDate
                    txtfromdate?.text = fromDate

                }

            },
            year,
            month,
            day
        )
        dpd?.datePicker?.maxDate = System.currentTimeMillis() - 1000
        dpd.show()

    }

    private fun validateDatesInput(): Pair<Boolean, String> {
        var inputtodate = toDate
        var inputfromdate = fromDate

        return homeViewModel.validateDateCredentials(inputtodate, inputfromdate)
    }

    private fun validateCommentsInput(): Pair<Boolean, String> {
        var comment = edittextcomments?.text.toString()

        return homeViewModel.validateComments(comment)
    }


    override fun onclick(value: Boolean) {
    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }

    override fun onDateClick(position: Int, datefromadapter: String, showbtn: Boolean) {
        binding.currentdate.visibility = View.VISIBLE
        binding.currentdate.text = ""
        var date = days[position]

        var month = calendar.get(Calendar.MONTH) + 1
        var year = calendar.get(Calendar.YEAR)

        var tempdate = ""
        var showReqButton = showbtn


        if (month < 10 && date < 10) {
            requestdate = "$year-0$month-0$date"
            tempdate = "0$month/0$date/$year"

        } else if (date < 10) {
            requestdate = "$year-$month-0$date"
            tempdate = "$month/0$date/$year"
        } else if (month < 10) {
            requestdate = "$year-0$month-$date"
            tempdate = "0$month/$date/$year"
        } else {
            requestdate = "$year-$month-$date"
            tempdate = "$month/$date/$year"

        }
        lateSittingMinutes = ""
        userRequestDate = requestdate
        if(datechecklist!=null)
            if(datechecklist?.let { it.isNotEmpty() } == true)
            {
                for (i in datechecklist!!.indices) {

                    var temstr = ""

                    if (datechecklist!!.get(i).date.toString().equals(date.toString())) {
                        reqResubmissionAllow = datechecklist!!.get(i).isReqSubmittedd
                        when (datechecklist?.get((i))?.remarks?.lowercase()) {

                            null -> {
                                binding.currentdate.text = binding.txtcheckdate.text.toString() + " n/a"
                            }
                            "p" -> {
                                if(datechecklist!!.get(i).lateSittingMinutes!=null&&datechecklist!!.get(i).lateSittingMinutes.isNotEmpty() )
                                {
                                    lateSittingMinutes = datechecklist!!.get(i).lateSittingMinutes
                                    binding.extraHours.text = "Extra working hours: " + "${lateSittingMinutes}"
                                    binding.extraHours.show()
                                }
                                else{
                                    binding.extraHours.hide()
                                }
                                userAttendanceStatus = "present"
                                showReqButton = false
                                binding.mcvrequest.visibility = View.GONE
                                var cout = date.toString() + " " + binding.txtcheckdate.text.toString()
                                var checkin = ""
                                var checkout = ""
                                if (datechecklist != null) {
                                    if (datechecklist!!.get((i)).firstCheckIn != null && datechecklist!!.get(
                                            (i)
                                        ).firstCheckIn.toString() != ""
                                    ) {
                                        checkin =
                                            " Check-in " + MyHelperClass.convertToAMPM_24to12(
                                                datechecklist!!.get(
                                                    (i)
                                                ).firstCheckIn.split(" ")[1]
                                            )
                                    }
                                    if (datechecklist!!.get((i)).lastCheckOut != null && datechecklist!!.get(
                                            (i)
                                        ).lastCheckOut.toString() != ""
                                    ) {
                                        checkout =
                                            " Check-out " + MyHelperClass.convertToAMPM_24to12(
                                                datechecklist!!.get(
                                                    (i)
                                                ).lastCheckOut.split(" ")[1]
                                            )
                                    }




                                    try {
                                        binding.currentdate.text =  checkin + checkout

                                    } catch (e: Exception) {
                                        LoggerGenratter.getInstance()
                                            .printLog("EMPLOYEE DETAILS", e.toString())
                                    }
                                }

                            }

                            "a" -> {
                                binding.extraHours.hide()
                                userAttendanceStatus = "absent"
                                showReqButton = true
                                binding.currentdate.text = "Absent"
                            }

                            "l" -> {
                                binding.extraHours.hide()
                                userAttendanceStatus = "leave"
                                showReqButton = true
                                binding.currentdate.text = "Leave"
                            }

                            "la" -> {
                                if(datechecklist!!.get(i).lateSittingMinutes!=null&&datechecklist!!.get(i).lateSittingMinutes.isNotEmpty() )
                                {
                                    lateSittingMinutes = datechecklist!!.get(i).lateSittingMinutes
                                    binding.extraHours.text = "Extra working hours: " + "${lateSittingMinutes}"
                                    binding.extraHours.show()
                                }
                                else{
                                    binding.extraHours.hide()
                                }

                                userAttendanceStatus = "late arrival"
                                showReqButton = true
                                var cout = date.toString() + " " + binding.txtcheckdate.text.toString()
                                var checkin = ""
                                var checkout = ""

                                if (datechecklist!!.get((i)).firstCheckIn != null && datechecklist!!.get(
                                        (i)
                                    ).firstCheckIn != ""
                                ) {
                                    checkin =
                                        " Check-in " + MyHelperClass.convertToAMPM_24to12(
                                            datechecklist!!.get(
                                                (i)
                                            ).firstCheckIn.split(" ")[1]
                                        )
                                }
                                if (datechecklist!!.get((i)).lastCheckOut != null && datechecklist!!.get((i)).lastCheckOut!="") {
                                    checkout =
                                        " Check-out " + MyHelperClass.convertToAMPM_24to12(
                                            datechecklist!!.get(
                                                (i)
                                            ).lastCheckOut.split(" ")[1]
                                        )
                                }


                                try {
                                    binding.currentdate.text = checkin + checkout


                                } catch (e: Exception) {
                                    LoggerGenratter.getInstance().printLog("EMPLOYEE DETAILS", e.toString())
                                }


                            }
                            "p.h"-> {
                                binding.extraHours.hide()
                                showReqButton = true
                                userAttendanceStatus = "holiday"

                            }
                            "holiday"->{
                                binding.extraHours.hide()
                                showReqButton = true
                                userAttendanceStatus = "holiday"
                                var cout = date.toString() + " " + binding.txtcheckdate.text.toString()
                                var checkin = ""
                                var checkout = ""

                                if (datechecklist!!.get((i)).firstCheckIn != null && datechecklist!!.get(
                                        (i)
                                    ).firstCheckIn != ""
                                ) {
                                    checkin =
                                        " Check-in " + MyHelperClass.convertToAMPM_24to12(
                                            datechecklist!!.get(
                                                (i)
                                            ).firstCheckIn.split(" ")[1]
                                        )
                                }
                                if (datechecklist!!.get((i)).lastCheckOut != null && datechecklist!!.get((i)).lastCheckOut!="") {
                                    checkout =
                                        " Check-out " + MyHelperClass.convertToAMPM_24to12(
                                            datechecklist!!.get(
                                                (i)
                                            ).lastCheckOut.split(" ")[1]
                                        )
                                }


                                try {
                                    if(checkin!=""||checkout!="")
                                    {
                                        binding.currentdate.text =  checkin + checkout
                                    }
                                } catch (e: Exception) {
                                    LoggerGenratter.getInstance().printLog("EMPLOYEE DETAILS", e.toString())
                                }
                            }

                            else -> {
                                showReqButton = false
                            }

                        }
                    }
                }
            }


        if (showReqButton) {
            binding.mcvrequest.visibility = View.VISIBLE
        } else {
            binding.mcvrequest.visibility = View.GONE
        }
        if(!showbtn){
            binding.mcvrequest.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._requestattendanceemployee.postValue(null)
        homeViewModel._weekattendanceresponse.postValue(null)
        homeViewModel._downloadAttendence.postValue(null)
    }
}

