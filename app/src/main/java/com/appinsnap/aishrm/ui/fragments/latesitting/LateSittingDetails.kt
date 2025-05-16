package com.appinsnap.aishrm.ui.fragments.latesit

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentLateSittingDetailsBinding
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.latesitting.ProjectData
import com.appinsnap.aishrm.ui.fragments.latesitting.adapter.ProjectAdapter
import com.appinsnap.aishrm.ui.fragments.latesitting.adapter.SelectedProjectAdapter
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingDetailRequestModel
import com.appinsnap.aishrm.ui.fragments.latesitting.models.LateSittingDetailsResponseModel
import com.appinsnap.aishrm.ui.fragments.latesitting.models.Project
import com.appinsnap.aishrm.ui.fragments.latesitting.models.SelectedProjectData
import com.appinsnap.aishrm.ui.fragments.latesitting.models.SubmitLateSittingDetailsRequest
import com.appinsnap.aishrm.ui.fragments.latesitting.models.UpdateLateSittingRequest
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
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import worker8.com.github.radiogroupplus.RadioGroupPlus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class LateSittingDetails : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission,ProjectAdapter.onProjectItemClick,SelectedProjectAdapter.onProjectCancel {
    private var _binding: FragmentLateSittingDetailsBinding? = null
    private var projectlist: ArrayList<ProjectData> = ArrayList()
    private var userCheckOutLateSitting:String = ""
    private var allprojectsPosition:ArrayList<Int> = ArrayList()
    private var lateSittingFromDate:String=""
    private var isDescriptionRequired:Boolean=false
    private var lateSittingToDate:String=""
    private var hoursToCheck:String = ""
    private var selectedProjects:ArrayList<Int> = ArrayList()
    private var selectedlist: ArrayList<SelectedProjectData> = ArrayList()
    private var toCalculateStartTime: String = ""
    private var totalHoursLateSitting:String=""
    private var formattedCheckInTime:String = ""
    private var allprojectlist:ArrayList<Project>?=null
    private var toCalculateEndTime: String = ""
   private  var notificationid:Int = 0
    private var userLateSittingCheckOutTime:String = ""
    private var dateFormat = SimpleDateFormat("dd-MM-yy hh:mm a", Locale.getDefault())
    private var startTimeToUpdate: String = ""
    private var totalHours: String = ""
    private var endTimeToUpdate: String = ""
    private var minutesToModifyAdd:Int=30
    private var minutesToModifySubtract:Int=-30
    private var formattedendTimeToUpdate: Date? = null
    private var formattedstartTimeToUpdate: Date? = null
    private var latitude: String = ""
    private var fromdate:String=""
    private var longitude: String = ""
    private var noOfDays: String = ""
    private var status: String = ""
    val calendar = Calendar.getInstance()
    private var isApprovalLayout: String = ""
    private var location: String = ""
    private var employeeLocation: String = ""
    private val binding get() = _binding!!
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private var sessionmanagement: SessionManagement? = null
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
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_late_sitting_details,
            container,
            false
        )
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        binding.starttimelayout.isEnabled = false
        callLateSittingDetailsApi()
        return view
    }

    private fun callLateSittingDetailsApi() {
        isApprovalLayout = sessionmanagement!!.getIsApprovalLayout()
        if(isApprovalLayout.contains("approvallayout"))
        {
            notificationid = sessionmanagement!!.getLateSittingNotID()
        }
        else{
            notificationid = 0
        }
        val lateSittingRequest = LateSittingDetailRequestModel(employeeid = sessionmanagement?.getEmpId()!!.toInt(), notiid = notificationid)
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getLateSittingDetails(lateSittingRequest)
        }
    }


    private fun handlingUpdateHours() {
        employeeLocation = sessionmanagement!!.getEmployeeLoc()
        latitude = sessionmanagement!!.getLatitude()
        longitude = sessionmanagement!!.getLongitude()
        noOfDays = sessionmanagement!!.getNoOfDays()
        status = sessionmanagement!!.getStatus()
        fromdate = sessionmanagement!!.getCheckInDate()
        var currentdate = MyHelperClass.getCurrentTimeInISO8601Format()
        lateSittingFromDate = "${fromdate}T${currentdate}"
        lateSittingToDate =     "${fromdate}T${currentdate}"
        var currentdatetime = MyHelperClass.getCurrentDateDDMMYYYY()
        var currentdatetime2 = MyHelperClass.getCurrentDateYYYYMMDDFormat()
        var currenttime = MyHelperClass.getCurrentTimeAMPM()
        var currentTimeHHmmssSSS = MyHelperClass.getCurrentTimeHHmmssSSS()
        userCheckOutLateSitting = "$currentdatetime $currenttime"
        userLateSittingCheckOutTime = "$currentdatetime2  $currentTimeHHmmssSSS"
        binding.txtselectedendtime.text = "$currentdatetime  $currenttime"
       //handlingChangeTimeField()
    }
    private fun setProjectAdapter(
        projectlist: ArrayList<ProjectData>,
        selectedlist: ArrayList<SelectedProjectData>
    ) {

        if(projectlist.isNotEmpty()) {
            val adapter = ProjectAdapter(requireContext(), projectlist, this)
            binding.rvprojects.adapter = adapter
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        liveDataObserver()
    }

    private fun liveDataObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(it)
        })

        homeViewModel._updatelatesittingdetailresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        showSuccessLateSittingDialog("updatelatesitting",response.data?.statusMessage)
                    }
                    is NetworkResult.Error -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })

        homeViewModel._latesittingdetailresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        if(isApprovalLayout.contains("approval")&&response.data?.body?.firstCheckIn!=null&&response.data?.body?.checkout!=null)
                        {
                            binding.radiogrouplayout.hide()
                            binding.starttimelayout.isEnabled = false
                            binding.mcvendtimelayout.isEnabled = false
                            handlingLateSittingDetails(response)
                        }
                        else{
                            binding.radiogrouplayout.show()
                            binding.txtchangetime.hide()
                            binding.mcvchangetime.hide()
                            handlingUpdateHours()
                            calculatingLateSittingHours(response)

                        }


                    }

                    is NetworkResult.Error -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })


        homeViewModel._submitlatesittingdetailresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                    showSuccessLateSittingDialog(
                        "",
                        response.data?.statusMessage
                    )

                    }
                    is NetworkResult.Error -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })
    }

    private fun handlingLateSittingDetails(response: NetworkResult.Success<LateSittingDetailsResponseModel>) {
        binding.txtchangetime.show()
        binding.mcvchangetime.show()
        binding.mcvendtimelayout.isEnabled = false
        binding.starttimelayout.isEnabled = false
        binding.edtDescription.isEnabled = false
        if (response.data?.body?.description != null) {
            if (response.data.body.description.isNotEmpty()) {
                binding.edtDescription.setText(response.data.body.description)
                binding.edtDescription.setTextColor(Color.parseColor("#B9B7B7"))

            }
        }
        if (response.data?.body?.project != null) {
            if (response.data.body.project.isNotEmpty()) {
                var projects = convertToList(response.data.body.project)

                if (response.data.body.projects != null) {
                    if (response.data.body.projects.isNotEmpty()) {
                        selectedProjects.clear()
                        selectedlist.clear()
                        var projectsList = response.data.body.projects
                        projectsList.forEach {
                            if (projects.contains(it.name)) {
                                selectedlist.add(SelectedProjectData(it.name, it.id))
                                binding.txtselectedproject.hide()
                                binding.recyclerSelectedProjects.show()
                                binding.projectdownarrow.isEnabled = false
                                val adapter = SelectedProjectAdapter(
                                    requireContext(),
                                    selectedlist,
                                    this,
                                    false
                                )
                                binding.recyclerSelectedProjects.adapter = adapter
                                selectedProjects.add(it.id)
                            } else {

                            }
                        }
                    }
                }
            }
        }
        if (response.data?.body?.totalhrs != null) {
            if (response.data.body.totalhrs.isNotEmpty()) {
                var totalhoursUser =
                    response.data.body.totalhrs
                totalHours  = totalhoursUser
                hoursToCheck = totalHours
                totalHoursLateSitting = response.data.body.totalhrs
                var mins = totalHoursLateSitting
                var convertedmins = MyHelperClass.convertStringToMinutes(mins)
                if(convertedmins<30)
                {
                    minutesToModifySubtract = -convertedmins.toInt()
                }
                else{
                    minutesToModifySubtract = -30
                }
                binding.selectedhours.text = totalhoursUser
                binding.txtselectedchangetime.text = totalHours
            }
        }
        if(response.data?.body?.firstCheckIn!=null)
        {
            if(response.data.body.firstCheckIn.isNotEmpty())
            {
                var splitcheckout = response.data.body.firstCheckIn.split(" ")
                var date = splitcheckout.get(0)
                var time = splitcheckout.get(1)
                var convertedtime = MyHelperClass.convertTimeFormatToAMPM(time)
                var convertedcheckin = MyHelperClass.convertDateFormatDDMMYYYY(date)
                startTimeToUpdate = "$convertedcheckin $convertedtime"
                    binding.selectedstarttime.text = "$convertedcheckin $convertedtime"
            }
        }
        if(response.data?.body?.checkout!=null)
        {
            if(response.data.body.checkout.isNotEmpty())
            {
                var splitcheckout = response.data.body.checkout.split(" ")
                var date = splitcheckout.get(0)
                var time = splitcheckout.get(1)
                var convertedtime = MyHelperClass.convertTimeFormatToAMPM(time)
                var formattedcheckout = MyHelperClass.convertDateFormatYYYYMMDD3(date)
                var convertedcheckout = MyHelperClass.convertDateFormatDDMMYYYY(date)
                formattedendTimeToUpdate =dateFormat.parse("$convertedcheckout $convertedtime")
                binding.txtselectedendtime.text = "$convertedcheckout $convertedtime"
                userLateSittingCheckOutTime = "$formattedcheckout $time"
                fromdate = "$formattedcheckout"
                endTimeToUpdate = "$formattedcheckout $time"

            }
        }
    }

    private fun calculatingLateSittingHours(response: NetworkResult.Success<LateSittingDetailsResponseModel>) {

         allprojectlist = response.data?.body?.projects as ArrayList<Project>

        populationProjectData(allprojectlist)
        var checkintime = response.data?.body?.checkin
        if(checkintime!=null)
        {
            if(checkintime.toString().isNotEmpty())
            {
               formattedCheckInTime =  MyHelperClass.getDateTimeDDMMYYYY(checkintime.toString())
                toCalculateStartTime = formattedCheckInTime
                binding.selectedstarttime.text = formattedCheckInTime
                calculationHours(formattedCheckInTime)
            }
        }
    }



    private fun calculationHours(formattedCheckInTime: String) {
        if(formattedCheckInTime.isNotEmpty()&&userCheckOutLateSitting.isNotEmpty())
        {
          calculateHours(formattedCheckInTime,userCheckOutLateSitting)

        }
    }

    private fun populationProjectData(allprojectlist: List<Project>?) {
        if(allprojectlist!=null)
        {
            if(allprojectlist.isNotEmpty())
            {
                if(projectlist.isNotEmpty())
                {
                    projectlist.clear()
                }
                allprojectlist.forEach {
                    if(it.id==0)
                    {

                    }
                    else{
                        projectlist.add(ProjectData(it.name,it.id,false,0))
                    }
                }
                setProjectAdapter(projectlist, selectedlist)
            }
        }
    }

    private fun onClickListeners() {
         handlingListWithRadioOptions()

        binding.submit.setOnClickListener {
            validatingInformationOfLateSitting()
        }
        binding.projectdownarrow.setOnClickListener {
            handleProjectLayoutVisibility()
        }
        binding.increasechangetime.setOnClickListener {
            increaseHoursUpdate()
        }
        binding.decreasechangetime.setOnClickListener {
            decreaseHourUpdate()
        }
        binding.starttimelayout.setOnClickListener {
            showStartTimeDateTimeDialog()
        }
        binding.mcvendtimelayout.setOnClickListener {
            showEndTimeDateTimeDialog()
        }
    }

    private fun handlingListWithRadioOptions() {
        binding.radioGroupProjects.check(R.id.radioProjects)
        binding.radioGroupProjects.setOnCheckedChangeListener(RadioGroupPlus.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioOthers -> {
                  if(selectedProjects.isNotEmpty())
                  {
                      selectedProjects.clear()
                  }
                    if(projectlist.isNotEmpty())
                    {
                        projectlist.clear()
                    }
                    if(selectedlist.isNotEmpty())
                    {
                        selectedlist.clear()
                    }
                   binding.mcvprojectlayout.hide()
                    binding.mcvprojects.hide()
                    binding.txtprojectname.hide()
                    updateprojectlist(true)
                    isDescriptionRequired = true
                }

                R.id.radioProjects -> {
                    if(selectedProjects.isNotEmpty())
                    {
                        selectedProjects.clear()
                    }
                    if(selectedlist.isNotEmpty())
                    {
                        selectedlist.clear()
                    }
                    binding.recyclerSelectedProjects.hide()
                    binding.mcvprojectlayout.show()
                    binding.txtprojectname.show()
                    binding.mcvprojects.show()
                    binding.rvprojects.hide()
                    val adapter = SelectedProjectAdapter(requireContext(), selectedlist, this,true)
                    binding.recyclerSelectedProjects.adapter = adapter
                    binding.txtselectedproject.visibility = View.GONE
                    binding.recyclerSelectedProjects.visibility = View.VISIBLE
                    if(projectlist.isNotEmpty())
                    {
                        projectlist.clear()
                    }
                    updateprojectlist(false)
                    binding.txtselectedproject.show()
                    binding.txtselectedproject.text = "Select project"
                 isDescriptionRequired = false
                }
            }
        })
    }

    private fun updateprojectlist(others:Boolean) {
        if(others)
        {
            allprojectlist?.forEach {
                if(it.id==0)
                {
                    projectlist.add(ProjectData(it.name,it.id,false,0))
                    selectedProjects.add(it.id)
                }
                else{
                    projectlist.add(ProjectData(it.name,it.id,false,0))

                }
            }
            setProjectAdapter(projectlist,selectedlist)
        }
        else{
            allprojectlist?.forEach {
                if(it.id==0)
                {

                }
                else{
                    projectlist.add(ProjectData(it.name,it.id,false,0))
                }

            }
            setProjectAdapter(projectlist,selectedlist)
        }


    }
    private fun showSuccessLateSittingDialog(layout: String?, statusMessage1: String?) {
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
        textmessage.text = "$statusMessage1"

        okbtn.setSafeOnClickListener {
            if(layout?.contains("updatelatesitting") == true)
            {
                try
                {
                    findNavController().navigate(R.id.action_latesittingFragment_to_approvalAndNews)
                }
                catch (e:Exception)
                {

                }
                permissiondialog.dismiss()
            }
            else{
                try {
                    findNavController().navigate(R.id.action_latesittingFragment_to_homefragment)
                }
                catch (e:Exception)
                {
                }
                permissiondialog.dismiss()
            }

        }
        if (permissiondialog.isShowing) {
            permissiondialog.dismiss()
        }
        permissiondialog.show()
    }
    private fun validatingInformationOfLateSitting() {
        if (isApprovalLayout.contains("approvallayout")) {
            var request = UpdateLateSittingRequest(
                checkouttime = userLateSittingCheckOutTime,
                notiid = notificationid,
                totalhrs = totalHoursLateSitting
            )
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.updateLateSittingDetails(request)
            }
        } else {
            var validation: Pair<Boolean, String>? = null
            var description = binding.edtDescription.text.toString()
            if (selectedProjects.isNotEmpty()) {
                    if (description.isNotEmpty()) {
                      var isValid =   validateThreeWords(description)
                        if(isValid){
                            if(formattedCheckInTime.isNotEmpty()&&userCheckOutLateSitting.isNotEmpty())
                            {
                                validation = validatsTimeInputLateSitting()
                                if (validation != null) {
                                    if (validation.first) {
                                        var request = SubmitLateSittingDetailsRequest(
                                            checkoutime = userLateSittingCheckOutTime,
                                            comment = description,
                                            employeeID = sessionmanagement!!.getEmpId(),
                                            employeeLocation = employeeLocation,
                                            fromDate = lateSittingFromDate,
                                            toDate = lateSittingToDate,
                                            latitude = latitude.toDouble(),
                                            longitude = longitude.toDouble(),
                                            noofDays = noOfDays,
                                            project = selectedProjects,
                                            status = status,
                                            totalHrs = totalHoursLateSitting
                                        )
                                        callSubmittLateSittingDetails(request)
                                    } else {
                                        showGeneralDialogFragment(
                                            requireContext(),
                                            false,
                                            this,
                                            false,
                                            "yes",
                                            yourTitle = "",
                                            yourMessage = "${validation.second}",
                                            location = this,
                                            icon = R.drawable.reject
                                        )
                                    }
                                }
                            }
                            else{
                                showGeneralDialogFragment(
                                    requireContext(),
                                    false,
                                    this,
                                    false,
                                    "yes",
                                    yourTitle = "",
                                    yourMessage = "Check-in time is empty.",
                                    location = this,
                                    icon = R.drawable.reject
                                )
                            }
                        }
                        else{
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

                    } else {
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            false,
                            "yes",
                            yourTitle = "",
                            yourMessage = "Please provide reason.",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

            } else {

                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        false,
                        "yes",
                        yourTitle = "",
                        yourMessage = "Please select project.",
                        location = this,
                        icon = R.drawable.reject
                    )
                }
                }

            }

    private fun callSubmittLateSittingDetails(request: SubmitLateSittingDetailsRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getLateSittingSubmittInfo(request)
        }
    }

    private fun decreaseHourUpdate() {
        if(endTimeToUpdate.isNotEmpty())
        {
            if(endTimeToUpdate == startTimeToUpdate||(hoursToCheck=="0 min"||hoursToCheck == "0 hour"||hoursToCheck=="0 mins"||hoursToCheck=="0 hours"))
            {

            }

            else{
                val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                val pattern = "dd-MM-yyyy hh:mm a"
                val modifyTime:Int=0
                calendar.time = formattedendTimeToUpdate
                calendar.add(Calendar.MINUTE, minutesToModifySubtract)
                formattedendTimeToUpdate = calendar.time
                val date = LocalDateTime.parse(formattedendTimeToUpdate.toString(), formatter)
                val outputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                val finaloutputFormatter = outputFormatter.format(date)
                endTimeToUpdate = finaloutputFormatter
                binding.txtselectedendtime.text = finaloutputFormatter
                var datetime = endTimeToUpdate.split(" ")
                var datesplit = datetime.get(0)
                var timesplit = datetime.get(1)+" "+datetime.get(2)
                var converteddate = MyHelperClass.convertDateFormatyyyymmdd(datesplit)
                var convertedtime = MyHelperClass.convertTimeFormatFromAmPm(timesplit)
                userLateSittingCheckOutTime = "$converteddate $convertedtime"
                val updatedIncreaseTime = modifyTime(totalHours, 30, false)
                totalHours = updatedIncreaseTime
                hoursToCheck = totalHours
                totalHoursLateSitting = totalHours
                val regex = Regex("^\\d+\\s*min$")
                var correct = regex.containsMatchIn(totalHours)
                if(correct)
                {
                    var value =   totalHours.split(" ").get(0).toInt()
                    if(value == 0)
                    {
                        minutesToModifySubtract = -30
                    }
                    else if(value >30)
                    {
                        minutesToModifySubtract = -30
                    }
                    else if(value == 30)
                    {
                        minutesToModifySubtract = -30
                    }
                    else{
                        minutesToModifySubtract = -value
                    }
                }
                else{
                    minutesToModifySubtract = -30
                }
                binding.selectedhours.text = totalHours
                binding.txtselectedchangetime.text = totalHours
            }
        }
    }
    private fun increaseHoursUpdate() {
        updatingIncreaseEndTime()
    }

    private fun updatingIncreaseEndTime() {
        if(endTimeToUpdate.isNotEmpty())
        {
            minutesToModifySubtract = -30
            val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val pattern = "dd-MM-yyyy hh:mm a"
            calendar.time = formattedendTimeToUpdate
            calendar.add(Calendar.MINUTE, minutesToModifyAdd)
            formattedendTimeToUpdate = calendar.time
            val date = LocalDateTime.parse(formattedendTimeToUpdate.toString(), formatter)
            val outputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            val finaloutputFormatter = outputFormatter.format(date)
            endTimeToUpdate = finaloutputFormatter
            var datetime = endTimeToUpdate.split(" ")
            var datesplit = datetime.get(0)
            var timesplit = datetime.get(1)+" "+datetime.get(2)
            var converteddate = MyHelperClass.convertDateFormatyyyymmdd(datesplit)
            var convertedtime = MyHelperClass.convertTimeFormatFromAmPm(timesplit)
            userLateSittingCheckOutTime = "$converteddate $convertedtime"
            binding.txtselectedendtime.text = finaloutputFormatter.toString()
            val updatedIncreaseTime = modifyTime(totalHours, 30, true)
            totalHours = updatedIncreaseTime
            hoursToCheck = totalHours
            totalHoursLateSitting = totalHours
            binding.selectedhours.text = totalHours
            binding.txtselectedchangetime.text = totalHours
        }

    }

    private fun showEndTimeDateTimeDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val endFormattedDate = formatSelectedDate(selectedYear, selectedMonth + 1, selectedDay)

                // Create a new instance of TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        // Format the selected time
                        var value = "T"
                        val endTimeFormattedTime = MyHelperClass.formatTime(selectedHour, selectedMinute)
                        var endTimeFormaTHHmmssSSS = MyHelperClass.formatTimeHHmmssSSS(selectedHour,selectedMinute)

                        // Combine the formatted date and time as needed
                        var endDateTime = "$endFormattedDate at $endTimeFormattedTime"
                        endTimeToUpdate = "$endFormattedDate $endTimeFormattedTime"
                        var converteddateyyyymmdd = MyHelperClass.convertDateFormatyyyymmdd("$endFormattedDate")
                        userCheckOutLateSitting = "$endFormattedDate $endTimeFormattedTime"
                        userLateSittingCheckOutTime = "$converteddateyyyymmdd $endTimeFormaTHHmmssSSS"
                        var date = MyHelperClass.convertDateFormatYYYYMMDD2("$endFormattedDate")
//                        lateSittingFromDate = "$date$value$endTimeFormaTHHmmssSSS"
//                        lateSittingToDate = "$date$value$endTimeFormaTHHmmssSSS"
                        formattedendTimeToUpdate = dateFormat.parse(endTimeToUpdate)
                        toCalculateEndTime = "$endFormattedDate $endTimeFormattedTime"
                        calculateHours(toCalculateStartTime, toCalculateEndTime)
                        binding.txtselectedendtime.text = endDateTime
                        // Do something with the combined date and time
                        // For example, display it in a TextView
                        // textView.text = dateTime
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false // set to false for 12-hour format, true for 24-hour format
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showStartTimeDateTimeDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val startFormattedDate =
                    formatSelectedDate(selectedYear, selectedMonth + 1, selectedDay)

                // Create a new instance of TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        // Format the selected time
                        var startTimeFormattedTime = MyHelperClass.formatTime(selectedHour, selectedMinute)
                        // Combine the formatted date and time as needed
                        var startDateTime = "$startFormattedDate at $startTimeFormattedTime"
                        startTimeToUpdate = "$startFormattedDate $startTimeFormattedTime"
                        toCalculateStartTime = "$startFormattedDate $startTimeFormattedTime"
                        formattedstartTimeToUpdate = dateFormat.parse(toCalculateStartTime)
                        calculateHours(toCalculateStartTime, toCalculateEndTime)
                        binding.selectedstarttime.text = startDateTime
                        // Do something with the combined date and time
                        // For example, display it in a TextView
                        // textView.text = dateTime
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false // set to false for 12-hour format, true for 24-hour format
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun calculateHours(toCalculateStartTime: String, toCalculateEndTime: String) {
        if (toCalculateStartTime.isNotEmpty() && toCalculateEndTime.isNotEmpty()) {
            // Parse the time strings to Date objects
            val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
            val startDateTime: Date = dateFormat.parse(toCalculateStartTime)
            val endDateTime: Date = dateFormat.parse(toCalculateEndTime)

            // Calculate the difference in milliseconds
            val differenceMillis: Long = endDateTime.time - startDateTime.time
            var displayDifference: String = ""
            // Convert milliseconds to minutes and hours
            val differenceMinutes: Long = TimeUnit.MILLISECONDS.toMinutes(differenceMillis)
            val hours: Long = differenceMinutes / 60
            if (hours != 0L) {
                if (hours < 0L && differenceMinutes < 0L) {
                    displayDifference = "0 hour"
                } else if (hours < 0L && differenceMinutes > 0L) {
                    displayDifference = "$differenceMinutes mins"
                } else if (hours > 0L && differenceMinutes > 0L) {
                    var differencemin = differenceMinutes % 60
                    if (differencemin > 0) {
                        if(differencemin==1L && hours ==1L)
                        {
                            displayDifference = "$hours hour $differencemin min"
                        }
                        else{
                            displayDifference = "$hours hours $differencemin mins"
                        }

                    }
                        else {
                            if(hours==1L)
                            {
                                displayDifference = "$hours hour"
                            }
                        else{
                                displayDifference = "$hours hours"
                            }
                    }

                } else {
                    displayDifference = "$hours hour"
                }

                totalHours = displayDifference
              //  totalHoursLateSitting = MyHelperClass.convertToDecimalHours(totalHours)
                totalHoursLateSitting = totalHours
            } else {
                if (differenceMinutes == 0L && hours == 0L) {
                    displayDifference = "0 hour"
                }
                else if(differenceMinutes < 0L && hours == 0L)
                {
                    displayDifference = "0 mins"
                }
                else if(differenceMinutes == 1L && hours == 0L)
                {
                    displayDifference = "1 min"
                }
                else {
                    var differencemin = differenceMinutes % 60
                    displayDifference = "$differencemin mins"
                }

                totalHours = displayDifference
               // totalHoursLateSitting = MyHelperClass.convertToDecimalHours(totalHours)
                totalHoursLateSitting = totalHours
            }

            binding.selectedhours.text = displayDifference
            binding.txtselectedchangetime.text = displayDifference

        }
    }

    private fun handleProjectLayoutVisibility() {
        if (binding.rvprojects.visibility == View.GONE) {
            binding.projectdownarrow.setImageResource(R.drawable.uparrow)
            binding.text.hide()
            binding.rvprojects.show()
        } else {
            binding.projectdownarrow.setImageResource(R.drawable.downarrow)
            binding.text.show()
            binding.rvprojects.hide()
        }
    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }

    override fun onprojectclick(position: Int, name: String, check: Boolean) {
        projectlist.get(position).check = check
        var selectedid = projectlist.get(position).id
        selectedlist.clear()
        selectedProjects.clear()
        for (i in projectlist.indices) {
            if (projectlist.get(i).check) {
                var name = projectlist.get(i).name
                var id = projectlist.get(i).id
                selectedlist.add(SelectedProjectData(name, id))
                selectedProjects.add( projectlist.get(i).id)
            }
        }
        if (selectedlist.isEmpty()) {
            binding.txtselectedproject.visibility = View.VISIBLE
            binding.recyclerSelectedProjects.visibility = View.GONE
        } else {
            val adapter = SelectedProjectAdapter(requireContext(), selectedlist, this,true)
            binding.recyclerSelectedProjects.adapter = adapter
            binding.txtselectedproject.visibility = View.GONE
            binding.recyclerSelectedProjects.visibility = View.VISIBLE
        }
    }

    override fun onSelectedProjectCancel(
        list: ArrayList<SelectedProjectData>
    ) {
        selectedProjects.clear()
        for (i in projectlist.indices) {
            projectlist.get(i).check = false
            for (j in list.indices) {
                if (list.get(j).name == projectlist.get(i).name) {
                    projectlist.get(i).check = true
                    selectedProjects.add(projectlist.get(i).id)
                }
            }
        }
        if (list.isEmpty()) {
            binding.txtselectedproject.visibility = View.VISIBLE
            binding.recyclerSelectedProjects.visibility = View.GONE
        } else {
            binding.txtselectedproject.visibility = View.GONE
            binding.recyclerSelectedProjects.visibility = View.VISIBLE
        }
        setProjectAdapter(projectlist, list)
    }

    private fun formatSelectedDate(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Month is zero-based in Calendar
        return dateFormat.format(calendar.time)
    }

    fun modifyTime(originalTime: String, minutesToModify: Long, addMinutes: Boolean): String {
        val regex = Regex("(\\d+)\\s*hours?(?:\\s*(\\d+)\\s*mins?)?")
        val matchResult = regex.find(originalTime)

        return if (matchResult != null) {
            val (hours, mins) = matchResult.destructured

            val originalHours = hours.takeIf { it.isNotEmpty() }?.toLong() ?: 0
            val originalMinutes = mins.takeIf { it.isNotEmpty() }?.toLong() ?: 0

            // Convert hours to minutes and add original minutes
            val totalOriginalMinutes = originalHours * 60 + originalMinutes

            // Add or subtract minutes based on the flag
            val modifiedTotalMinutes = if (addMinutes) {
                totalOriginalMinutes + minutesToModify
            } else {
                (totalOriginalMinutes - minutesToModify).coerceAtLeast(0)
            }

            // Calculate modified hours and minutes
            val modifiedHours = modifiedTotalMinutes / 60
            val modifiedMinutes = modifiedTotalMinutes % 60

            // Build the result string
            val hoursString =
                if (modifiedHours > 0) "$modifiedHours hour" + if (modifiedHours > 1) "s" else "" else ""
            val minutesString = if (modifiedMinutes > 0) "$modifiedMinutes min" else ""

            if (hoursString.isNotEmpty() && minutesString.isNotEmpty()) {
                "$hoursString $minutesString"
            }
            else if(hoursString.isNotEmpty()){
                "$hoursString"
            }
                else if (minutesString.isNotEmpty()) {
                minutesString
            } else {
                "0 min"
            }
        } else {
            // Handle the case where only minutes are provided
            val modifiedMinutes = if (addMinutes) {
                // Add the provided minutes
                (originalTime.replace(Regex("[^0-9]"), "").toLong() + minutesToModify).coerceAtLeast(30)
            } else {
                // Subtract the provided minutes
                (originalTime.replace(Regex("[^0-9]"), "").toLong() - minutesToModify).coerceAtLeast(0)
            }

            // Convert total minutes to hours and remaining minutes
            val modifiedHours = modifiedMinutes / 60
            val remainingMinutes = modifiedMinutes % 60
            // Build the result string for hours and minutes
            val hoursString = if (modifiedHours > 0) "$modifiedHours hour" + if (modifiedHours > 1) "s" else "" else ""
            val minutesString = if (remainingMinutes > 0) "$remainingMinutes min" else ""
            if (hoursString.isNotEmpty() && minutesString.isNotEmpty()) {
                "$hoursString $minutesString"
            } else if (hoursString.isNotEmpty()) {
                "$hoursString"
            } else if (minutesString.isNotEmpty()) {
                minutesString
            } else {
                "0 min"
            }

        }
    }
    fun validateThreeWords(input: String): Boolean {
        val words = input.split(" ")

        // Check if the number of words is exactly three
        return words.size == 3 || words.size > 3
    }
    private fun validatsTimeInputLateSitting(): Pair<Boolean, String> {
        var inputcheckintime = MyHelperClass.convertToTargetFormatZoneOffset(formattedCheckInTime)
        var inputcheckouttime = MyHelperClass.convertToTargetFormatZoneOffset(userCheckOutLateSitting)
        return homeViewModel.validateTimeCredentialsLateSitting(inputcheckintime, inputcheckouttime)
    }
    fun convertToList(input: String): ArrayList<String> {
        // Split the input string based on the comma
        val parts = input.split(",")

        // Convert the List to ArrayList
        return ArrayList(parts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._submitlatesittingdetailresponse.postValue(null)
        homeViewModel._latesittingdetailresponse.postValue(null)
        homeViewModel._updatelatesittingdetailresponse.postValue(null)
    }
}
