package com.appinsnap.aishrm.ui.fragments.EmployeeHistory

import android.app.ActionBar
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentRequestResubmissionBinding
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.adapter.LeaveAdapter
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.CategoryAdapter
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.RequestResubmissionRequest
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo
import com.appinsnap.aishrm.ui.fragments.home.models.GetLeaveCounts
import com.appinsnap.aishrm.ui.fragments.home.models.categories
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class RequestResubmission : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission, CategoryAdapter.onCategorySelect,
    LeaveAdapter.onItemClick {

    private var _binding: FragmentRequestResubmissionBinding? = null
    private val binding get() = _binding!!
    var outputDateStr: String = ""
    var categoryid: Int = -1
    var reasonLengthValid: Boolean = false
    private var characterLimit:Int = 0
    var dummyCheckInTime = "09:00 AM"
    var dummyCheckOutTime = "06:00 PM"
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    var updatedUserStaus: String = ""
    var checkintimetoformat: LocalTime? = null
    var checkouttimetoformat: LocalTime? = null
    var selectedCheckInHourTime: Int = 9
    var selectedCheckInMinTime: Int = 0
    var selectedCheckOutHourTime: Int = 18
    var selectedCheckOutMinTime: Int = 0
    var currentDate = LocalDate.now()
    var isTimeShown: Boolean = false
    var categorieslist: ArrayList<categories> = ArrayList()
    var updatedCheckInTime: String = ""
    var updateCheckOuTtIME: String = ""
    var selectedLeaveId: Int = 0
    private var empDetails: EmployeeAttendanceInfo? = null
    var leavelist: ArrayList<GetLeaveCounts> = ArrayList()
    var leavedata: ArrayList<GetLeaveCounts> = ArrayList()
    private var sessionmanagement: SessionManagement? = null
    private var requestdate: String = ""
    var inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    var outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    private var categorylist: ArrayList<String> = ArrayList()
    val navArg: RequestResubmissionArgs by navArgs()
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_request_resubmission,
            container,
            false
        )
        sessionmanagement = SessionManagement(requireContext())
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        if (navArg.resubmissiondata != null) {
            val requestdetails = navArg.resubmissiondata
            val userattendnacestatus = requestdetails?.attendancestatus
            leavelist = sessionmanagement!!.getLeaveData()
            categorieslist = sessionmanagement?.getCategories() as ArrayList<categories>
            leavelist.forEach {
                leavedata.add(GetLeaveCounts(it.availableLeaves, it.name, it.id))
            }
            requestdate = navArg!!.resubmissiondata?.date.toString()
            empDetails = navArg!!.resubmissiondata?.empdetails
            val userdate = convertdate(requestdate)
            binding.txtselecteddate.text = userdate
            binding.edtReason.imeOptions = EditorInfo.IME_ACTION_DONE
            binding.mcvdatelayout.isClickable = false
            changingLayoutWithStatus(userattendnacestatus)
        }
         settingCharacterLimit()
        return binding.root
    }

    private fun settingCharacterLimit() {
        characterLimit =  sessionmanagement!!.getCharacterLimit()
        val maxLength = characterLimit
        val inputFilter = InputFilter.LengthFilter(maxLength)
        binding.edtReason.filters = arrayOf(inputFilter)
    }

    private fun convertdate(requestdate: String?): String {
        val date: Date = inputDateFormat.parse(requestdate)
        outputDateStr = outputDateFormat.format(date)
        return outputDateStr
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        livedataObservers()
    }

    private fun livedataObservers() {


        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(it)
        })
        homeViewModel._requestattendanceemployee.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        sessionmanagement?.setAttendanceStatus("")
                        showSuccessRequestResubmissionDialog(response.data?.statusMessage)
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
    private fun showSuccessRequestResubmissionDialog(statusMessage: String?) {
        val permissiondialog = Dialog(requireContext())
        permissiondialog.setCancelable(false)
        permissiondialog.setCanceledOnTouchOutside(true)
        permissiondialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        permissiondialog.setContentView(R.layout.general_dialog_design)
        permissiondialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
        )
        val okbtn: ConstraintLayout = permissiondialog.findViewById(R.id.ok)
        val popicon: ImageView = permissiondialog.findViewById(R.id.imgPopIcon)
        val textmessage: TextView = permissiondialog.findViewById(R.id.txtsuccess)
        popicon.setImageResource(R.drawable.tickicon)
        popicon.layoutParams.height = 130
        popicon.layoutParams.width = 130
        textmessage.text = "$statusMessage"

        okbtn.setSafeOnClickListener {
            try {
                findNavController().navigate(
                    RequestResubmissionDirections.actionRequestResubmissionfragmentToEmployeeDetail(
                        empDetails
                    )
                )
            }
            catch (e:Exception)
            {

            }

            permissiondialog.dismiss()
        }
        if (permissiondialog.isShowing) {
            permissiondialog.dismiss()
        }
        permissiondialog.show()
    }

    private fun onClickListeners() {
        binding.mcvcategorylayout.setOnClickListener {
            if (binding.mcvcategory.visibility == View.GONE) {
                binding.mcvcategory.show()
                binding.categorydownarrow.setImageResource(R.drawable.uparrow)
            } else {
                binding.categorydownarrow.setImageResource(R.drawable.downarrow)
                binding.mcvcategory.hide()
            }
        }
        binding.mcvcheckintimelayout.setOnClickListener {
            showCheckInTimePickerDialog()
        }
        binding.submit.setOnClickListener {
            submitRequestAttendanceRequest()
        }
        binding.mcvcheckouttimelayout.setOnClickListener {
            showCheckOutTimePickerDialog()
        }
        binding.mcvchooseleavetypelayout.setOnClickListener {
            if(leavedata.isNotEmpty()||leavedata!=null)
            {
                binding.imgleavedoenarrowr.isEnabled=true
                if (binding.mcvleave.visibility == View.VISIBLE) {
                    binding.mcvleave.hide()
                    binding.imgleavedoenarrowr.setImageResource(R.drawable.downarrow)
                } else {
                    binding.mcvleave.show()
                    binding.imgleavedoenarrowr.setImageResource(R.drawable.uparrow)
                }
            }
            else{
                binding.imgleavedoenarrowr.isEnabled=false
            }
        }
    }

    private fun submitRequestAttendanceRequest() {
        if (isTimeShown) {
            val reason = binding.edtReason.text.trim()
            reasonLengthValid = validateThreeWords(reason.toString())
            val validationresult = validatsTimeInput()
            if (validationresult.first) {
                if (reason.isEmpty()) {
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        false,
                        "yes",
                        yourTitle = "",
                        yourMessage = "Please provide reason",
                        location = this,
                        icon = R.drawable.reject
                    )
                } else {
                    if (reasonLengthValid) {
                        callRequestResubmissionApi()
                    } else {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "Please provide reason of atleast three words",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }
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
        }
        else {
            val reason = binding.edtReason.text.trim()
            reasonLengthValid = validateThreeWords(reason.toString())
            if (binding.edtReason.text.isEmpty()) {
                showGeneralDialogFragment(
                    requireContext(),
                    false,
                    this,
                    false,
                    "yes",
                    yourTitle = "",
                    yourMessage = "Please provide reason",
                    location = this,
                    icon = R.drawable.reject
                )
            } else {
                if (reasonLengthValid) {
                    callRequestResubmissionApi()
                } else {
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        false,
                        "yes",
                        yourTitle = "",
                        yourMessage = "Please provide reason of atleast three words.",
                        location = this,
                        icon = R.drawable.reject
                    )
                }
            }
        }
    }

    private fun showCheckOutTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = 18
        val minute = 0

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Adjust the selected time if it's greater than the maximum allowed time
                var adjustedHour:Int=0
                var adjustedMinute:Int=0
                if (convertDateToString()==requestdate)
                {
                    if(selectedHour>LocalTime.now().hour)
                    {
                        adjustedHour = LocalTime.now().hour
                    }
                    else {
                       adjustedHour = selectedHour
                    }
                } else {
                   adjustedHour =  selectedHour
                }
                if (convertDateToString()==requestdate)
                {
                    if(selectedMinute>LocalTime.now().minute)
                    {
                        adjustedMinute = LocalTime.now().minute
                    }
                    else {
                        adjustedMinute = selectedMinute
                    }
                } else {
                    adjustedMinute =  selectedMinute
                }

                // Handle the selected time
                val selectedTime = MyHelperClass.formatTime(adjustedHour, adjustedMinute)
                try {
                    checkouttimetoformat =
                        LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("hh:mm a"))
                } catch (_: Exception) {
                }
                updateCheckOuTtIME = requestdate + " " + selectedTime
                updateCheckOuTtIME = convertDateFormatt(updateCheckOuTtIME)
                binding.txtcheckoutselectedtime.text = "$selectedTime"
            },
            hour,
            minute,
            false // Set true if you want to use 24-hour format
        )
        timePickerDialog.show()
    }


    fun convertDateToString():String
    {
        val currentDate = LocalDate.now()
        // Convert LocalDate to String using toString()

        // Convert LocalDate to String using toString()
        val dateString = currentDate.toString()
        return dateString
    }

    private fun showCheckInTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = 9
        val minute = 0



        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Handle the selected time
                var adjustedHour:Int=0
                var adjustedMinute:Int=0
                if (convertDateToString()==requestdate)
                {
                    if(selectedHour>LocalTime.now().hour)
                    {
                        adjustedHour = LocalTime.now().hour
                    }
                    else {
                        adjustedHour = selectedHour
                    }
                } else {
                    adjustedHour =  selectedHour
                }
                if (convertDateToString()==requestdate)
                {
                    if(selectedMinute>LocalTime.now().minute)
                    {
                        adjustedMinute = LocalTime.now().minute
                    }
                    else {
                        adjustedMinute = selectedMinute
                    }
                } else {
                    adjustedMinute =  selectedMinute
                }
                val selectedTime =MyHelperClass.formatTime(adjustedHour, adjustedMinute)
                try {
                    checkintimetoformat =
                        LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("hh:mm a"))

                } catch (_: Exception) {
                }
                updatedCheckInTime = requestdate + " " + selectedTime
                updatedCheckInTime = convertDateFormatt(updatedCheckInTime)
                //    updatedCheckInTime = formatCheckInTime(selectedTime)


                binding.txtcheckinselectedtime.text = "$selectedTime"
            },
            hour,
            minute,
            false // Set true if you want to use 24-hour format
        )
        timePickerDialog.show()
    }

    private fun convertDateFormatt(updatedCheckInTime: String): String {
        return try {
            val originalFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            originalFormatter.timeZone = TimeZone.getTimeZone("UTC")

            // Parse the original string into a Date object
            val date = originalFormatter.parse(updatedCheckInTime)

            // Create a new SimpleDateFormat for ISO 8601 format
            val iso8601Formatter =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            iso8601Formatter.timeZone = TimeZone.getTimeZone("UTC")

            // Format the Date object to ISO 8601 string
            iso8601Formatter.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "00:00" // Handle the exception appropriately, this is just an example
        }
    }



    private fun changingLayoutWithStatus(userattendnacestatus: String?) {
        when (userattendnacestatus) {

            "absent" -> {
                if (categorieslist.isNotEmpty()) {
                    categorylist.clear()
                }
                if (categorieslist != null) {
                    categorieslist = categorieslist.filter {
                        it.name?.lowercase()?.contains("absent to") == true
                    } as ArrayList<categories>
                }

            }

            "leave" -> {
                if (categorieslist.isNotEmpty()) {
                    categorylist.clear()
                }
                if (categorieslist != null) {
                    categorieslist = categorieslist.filter {
                        it.name?.lowercase()?.contains("leave to") == true
                    } as ArrayList<categories>
                }
            }
            "p.h","holiday" ->
            {
                if (categorieslist.isNotEmpty()) {
                    categorylist.clear()
                }
                if (categorieslist != null) {
                    categorieslist = categorieslist.filter {
                        it.name?.lowercase()?.contains("holiday to") == true
                    } as ArrayList<categories>
                }
            }

            "late arrival" -> {
                if (categorieslist.isNotEmpty()) {
                    categorylist.clear()
                }
                if (categorieslist != null) {
                    categorieslist = categorieslist.filter {
                        it.name?.lowercase()?.contains("late arrival to") == true
                    } as ArrayList<categories>
                }
            }
        }
        setLeaveAdapter()
        setCategoryAdapter(categorieslist)
    }

    private fun setLeaveAdapter() {
        if (leavedata.isNotEmpty()) {
            val leavetype = leavedata.get(0).name
            selectedLeaveId = leavedata.get(0).id
            binding.txtselectedleave.text = leavetype
            val adapter = LeaveAdapter(leavedata, this)
            binding.rvleavetype.adapter = adapter
        }
        else{
            binding.txtselectedleave.text = "Select leave type"
        }
    }
    private fun setCategoryAdapter(categoryList: ArrayList<categories>) {
        if(requestdate==convertDateToString())
        {

            updatedCheckInTime = "$requestdate $dummyCheckInTime"
            dummyCheckOutTime = getCurrentTimeFormatted()
            updateCheckOuTtIME = "$requestdate $dummyCheckOutTime"
            selectedCheckOutMinTime = LocalTime.now().minute
            selectedCheckOutHourTime = LocalTime.now().hour
        }
        else{
            updatedCheckInTime = "$requestdate $dummyCheckInTime"
            updateCheckOuTtIME = "$requestdate $dummyCheckOutTime"
             selectedCheckOutHourTime= 18
             selectedCheckOutMinTime = 0
        }


        try {
            var selectedCheckInTime =MyHelperClass.formatTime(selectedCheckInHourTime, selectedCheckInMinTime)
            var selectedCheckOutTime = MyHelperClass.formatTime(selectedCheckOutHourTime, selectedCheckOutMinTime)
            checkintimetoformat = LocalTime.parse(selectedCheckInTime, DateTimeFormatter.ofPattern("hh:mm a"))
            checkouttimetoformat = LocalTime.parse(selectedCheckOutTime, DateTimeFormatter.ofPattern("hh:mm a"))
        } catch (e: Exception) {
            Log.d("errortime", "${e.message}")
        }
        updatedCheckInTime = convertDateFormatt(updatedCheckInTime)
        updateCheckOuTtIME = convertDateFormatt(updateCheckOuTtIME)
        binding.txtcheckinselectedtime.text = dummyCheckInTime
        binding.txtcheckoutselectedtime.text = dummyCheckOutTime
        var category:String = ""
        if(categoryList.isNotEmpty())
        {
             category = categoryList.get(0).name.toString()
        }

        categoryid = categorieslist.get(0).id!!
        binding.txtcategorytype.text = category
        if (category != null) {
            changinglayoutsWithType(category)
        }
        val adapter = CategoryAdapter(categorieslist, this)
        binding.rvcategorytype.adapter = adapter
    }

    override fun onclick(value: Boolean)
    {
    }


    override fun onLocationPermission(locationvalue: Boolean) {
    }

    override fun onCategoryItem(position: Int) {
        updatedCheckInTime = "$requestdate $dummyCheckInTime"
        updateCheckOuTtIME = "$requestdate $dummyCheckOutTime"
        try {
            var selectedCheckInTime = MyHelperClass.formatTime(selectedCheckInHourTime, selectedCheckInMinTime)
            var selectedCheckOutTime = MyHelperClass.formatTime(selectedCheckOutHourTime, selectedCheckOutMinTime)
            checkintimetoformat =
                LocalTime.parse(selectedCheckInTime, DateTimeFormatter.ofPattern("hh:mm a"))
            checkouttimetoformat =
                LocalTime.parse(selectedCheckOutTime, DateTimeFormatter.ofPattern("hh:mm a"))
        } catch (_: Exception) {

        }
        updatedCheckInTime = convertDateFormatt(updatedCheckInTime)
        updateCheckOuTtIME = convertDateFormatt(updateCheckOuTtIME)
        binding.txtcheckinselectedtime.text = dummyCheckInTime
        binding.txtcheckoutselectedtime.text = dummyCheckOutTime
        super.onCategoryItem(position)
        updatedUserStaus = categorieslist.get(position).name.toString()
        categoryid = categorieslist.get(position).id!!
        binding.txtcategorytype.text = updatedUserStaus
        binding.mcvcategory.hide()
        binding.categorydownarrow.setImageResource(R.drawable.downarrow)
        changinglayoutsWithType(updatedUserStaus)
    }

    private fun changinglayoutsWithType(categorytype: String) {

        categorieslist.forEach {
            if (categorytype == it.name) {
                if (it.hascomment == true) {
                    binding.txtReason.show()
                    binding.cardReason.show()
                } else {
                    binding.txtReason.hide()
                    binding.cardReason.hide()
                }
                if (it.hasdate == true) {
                    binding.txtdate.show()
                    binding.mcvdatelayout.show()
                } else {
                    binding.txtdate.hide()
                    binding.mcvdatelayout.hide()
                }
                if (it.hasleavetype == true) {
                    if(leavedata.isNotEmpty())
                    {
                        selectedLeaveId = leavedata.get(0).id
                        binding.txtchooseleavetype.show()
                        binding.mcvchooseleavetypelayout.show()
                    }
                    else{
                        selectedLeaveId = 0
                        binding.txtchooseleavetype.show()
                        binding.mcvchooseleavetypelayout.show()
                    }

                } else {
                    selectedLeaveId = 0
                    binding.mcvchooseleavetypelayout.hide()
                    binding.txtchooseleavetype.hide()
                }
                if (it.hastime == true) {
                    isTimeShown = true
                    binding.txtcheckouttime.show()
                    binding.txtcheckintime.show()
                    binding.mcvcheckouttimelayout.show()
                    binding.mcvcheckintimelayout.show()
                } else {
                    isTimeShown = false
                    binding.txtcheckouttime.hide()
                    binding.txtcheckintime.hide()
                    binding.mcvcheckouttimelayout.hide()
                    binding.mcvcheckintimelayout.hide()
                }

            }
        }
    }

    override fun onLeaveItemClick(position: Int) {
        super.onLeaveItemClick(position)
        if (leavedata.isNotEmpty()) {
            val leave = leavedata.get(position).name
            selectedLeaveId = leavedata.get(position).id
            binding.txtselectedleave.text = leave
            binding.mcvleave.hide()
        }
    }


    private fun validatsTimeInput(): Pair<Boolean, String> {
        var inputcheckintime = checkintimetoformat
        var inputcheckouttime = checkouttimetoformat
        return homeViewModel.validateTimeCredentials(inputcheckintime, inputcheckouttime)
    }

    private fun callRequestResubmissionApi() {
        var reason = binding.edtReason.text.toString()
        var empid = sessionmanagement?.getEmpId()
        val requestResubmission = RequestResubmissionRequest(
            category = categoryid,
            comment = reason,
            employeeID = empid!!.toInt(),
            fromDate = requestdate,
            leavetype = selectedLeaveId,
            time = updatedCheckInTime,
            time2 = updateCheckOuTtIME,
            toDate = requestdate
        )
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getRequestAttendance(requestResubmission)
        }
    }

    fun validateThreeWords(input: String): Boolean {
        val words = input.split(" ")

        // Check if the number of words is exactly three
        return words.size == 3 || words.size > 3
    }
    fun getCurrentTimeFormatted(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        return currentTime.format(formatter)
    }

}