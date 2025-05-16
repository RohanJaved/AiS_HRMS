package com.appinsnap.aishrm.ui.fragments.statics

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentStatisticsBinding
import com.appinsnap.aishrm.databinding.ShowfiltersdialogBinding
import com.appinsnap.aishrm.ui.fragments.employeeattendance.EmployeeAttendanceDirections
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.EmployeeAttendanceInfo
import com.appinsnap.aishrm.ui.fragments.home.adapter.FilterAdapter
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.AttendanceGraphRequest
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.CtoAndManagerModelresponceSummery
import com.appinsnap.aishrm.ui.fragments.home.models.attendancegraph.EmployeedetailModelresponces
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.FilterResponse
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.statics.adapter.StatisticsEmployeeAdapter
import com.appinsnap.aishrm.ui.fragments.statics.model.DownloadGraphDataRequest
import com.appinsnap.aishrm.util.*
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.wallet.zindigi.Utils.PermissionsUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StatisticsFragment : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission, StatisticsEmployeeAdapter.onItemClick {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    var from: String = ""
    private var searchEmployeeattedancelist: MutableList<EmployeedetailModelresponces>? =
        mutableListOf()
    var sortedList :List<EmployeedetailModelresponces>?=null
    var StatsEmployeeList: List<EmployeedetailModelresponces>? = null
    var StatsCountList: List<CtoAndManagerModelresponceSummery>? = null
    private var searchviewemployeeenable = false
    private var filteredEmployeeAttendanceList: List<EmployeedetailModelresponces>? = null
    private var sessionmanagement: SessionManagement? = null
    var checkdate = LocalDate.now()
    var attendanceStatus: String = ""
    private var toDate: String = ""
    var department: String = ""
    private var fromDate: String = ""
    lateinit var currentDate: LocalDate
    lateinit var currentDateCto: org.threeten.bp.LocalDate
    var filterDialog: Dialog? = null
    var departmentid: ArrayList<Int> = ArrayList()
    var designationid: ArrayList<Int> = ArrayList()
    var employeeID: Int = 0
    var genderid: ArrayList<Int> = ArrayList()
    var groupid: ArrayList<Int> = ArrayList()
    var attendanceid: Int = 0
    var officeid: ArrayList<Int> = ArrayList()
    val calendar = Calendar.getInstance()
    var currentView: String = "All"
    private var currentFilteredList: List<EmployeedetailModelresponces>? = null

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val navArg: StatisticsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        from = navArg.statsList?.from.toString()
        currentView = from
        when (currentView) {
            "Present" -> {
                attendanceStatus = "PresentCount"
            }

            "Absent" -> {
                attendanceStatus = "AbsentCount"
            }

            "Leave" -> {
                attendanceStatus = "OnLeaveCount"
            }

            else -> {
                attendanceStatus = "All"
            }
        }
        currentDate = navArg.statsList!!.date!!

        if(sortedList?.isNotEmpty() == true)
        {
            sortedList = listOf()
        }
        if(filteredEmployeeAttendanceList?.isNotEmpty() == true || filteredEmployeeAttendanceList!=null)
        {
            filteredEmployeeAttendanceList = listOf()
        }
        StatsEmployeeList =
            navArg.statsList?.employeedetailModelresponces as List<EmployeedetailModelresponces>
        StatsCountList =
            navArg.statsList?.ctoAndManagerModelresponceSummery as List<CtoAndManagerModelresponceSummery>
        groupid = navArg.statsList?.grouplist as ArrayList<Int>
        officeid = navArg.statsList?.officelist as ArrayList<Int>
        departmentid = navArg.statsList?.departmentlist as ArrayList<Int>
        genderid = navArg.statsList?.genderlist as ArrayList<Int>
        designationid = navArg.statsList?.designationlist as ArrayList<Int>
        searchviewemployeeenable = false

        newListEmployee(StatsEmployeeList!!, true)
        searchHandler()

        if (checkdate != currentDate) {
            binding.rightarrowcto.isEnabled = true
            binding.rightarrowcto.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow
                ), PorterDuff.Mode.SRC_IN
            )//
        } else {
            binding.rightarrowcto.isEnabled = false
            binding.rightarrowcto.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_font
                ), PorterDuff.Mode.SRC_IN
            )//

        }

        /*  settingCurrentDate()*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListeners()
        livedataObserver()

        val toolbarImage = requireActivity().findViewById<ImageView>(R.id.filter)
        toolbarImage.setOnClickListener {
            homeViewModel.getGraphFilters()
        }
        currentDateCto = navArg.statsList?.date!!
        checkdate = getCurentDate()
        val formatteddate = formatDate(currentDateCto)
        binding.txtcheckcto.text = formatteddate.toString()

    }

    private fun onClickListeners() {

        binding.presentBtn.setOnClickListener {
            attendanceStatus = "PresentCount"
            currentView = "Present"
            changingPresentLayout()
            changeOtherThenPresent()
            binding.idSV.setText("")
            binding.idSV.hint = "Search"
            StatsEmployeeList?.let { it1 -> newListEmployee(it1) }
        }

        binding.absentBtn.setOnClickListener {
            attendanceStatus = "AbsentCount"
            currentView = "Absent"
            changingAbsentLayout()
            changeOtherThenAbsent()
            binding.idSV.setText("")
            binding.idSV.hint = "Search"
            StatsEmployeeList?.let { it1 -> newListEmployee(it1) }
        }

        /*    binding.lateBtn.setOnClickListener {
                attendanceStatus = "LateCount"
                currentView = "Late"
                changingLateLayout()
                changeOtherThenLate()
                binding.idSV.setText("")
                binding.idSV.hint = "Search"
                StatsEmployeeList?.let { it1 -> newListEmployee(it1) }
            }*/

        binding.leaveBtn.setOnClickListener {
            attendanceStatus = "OnLeaveCount"
            currentView = "Leave"
            changingLeaveLayout()
            changeOtherThenLeave()
            binding.idSV.setText("")
            binding.idSV.hint = "Search"
            StatsEmployeeList?.let { it1 -> newListEmployee(it1) }
        }

        binding.allBtn.setOnClickListener {
            attendanceStatus = "All"
            currentView = "All"
            changingAllLayout()
            changeOtherThenAll()
            binding.idSV.setText("")
            binding.idSV.hint = "Search"
            StatsEmployeeList?.let { it1 -> newListEmployee(it1) }
        }

        binding.download.setSafeOnClickListener {
            checkPermission()
        }

        binding.rightarrowcto.setOnClickListener {
            calldepartmentattendaceListNext()
        }

        binding.leftarrowcto.setOnClickListener {
            calldepartmrntattendancelistPrevious()
        }

    }

    private fun changingAllLayout() {
        binding.allBtn.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.All.setTextColor(resources.getColor(R.color.yellow))
        binding.allBtn.setStrokeColor(resources.getColor(R.color.yellow))
        binding.allBtn.strokeWidth = 1
        changeOtherThenAll()

    }

    private fun changeOtherThenAll() {
        binding.leaveBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Leave.setTextColor(resources.getColor(R.color.white))
        binding.leaveBtn.strokeWidth = 0

        binding.absentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Absent.setTextColor(resources.getColor(R.color.white))
        binding.absentBtn.strokeWidth = 0

        binding.presentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Present.setTextColor(resources.getColor(R.color.white))
        binding.presentBtn.strokeWidth = 0

        binding.lateBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Late.setTextColor(resources.getColor(R.color.white))
        binding.lateBtn.strokeWidth = 0
    }

    private fun changingLeaveLayout() {
        binding.leaveBtn.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.Leave.setTextColor(resources.getColor(R.color.yellow))
        binding.leaveBtn.setStrokeColor(resources.getColor(R.color.yellow))
        binding.leaveBtn.strokeWidth = 1
    }

    private fun changeOtherThenLeave() {
        binding.lateBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Late.setTextColor(resources.getColor(R.color.white))
        binding.lateBtn.strokeWidth = 0

        binding.absentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Absent.setTextColor(resources.getColor(R.color.white))
        binding.absentBtn.strokeWidth = 0

        binding.presentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Present.setTextColor(resources.getColor(R.color.white))
        binding.presentBtn.strokeWidth = 0

        binding.allBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.All.setTextColor(resources.getColor(R.color.white))
        binding.allBtn.strokeWidth = 0

    }

    private fun changingAbsentLayout() {
        binding.absentBtn.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.Absent.setTextColor(resources.getColor(R.color.yellow))
        binding.absentBtn.setStrokeColor(resources.getColor(R.color.yellow))
        binding.absentBtn.strokeWidth = 1
        changeOtherThenAbsent()
    }

    private fun changeOtherThenAbsent() {
        binding.leaveBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Leave.setTextColor(resources.getColor(R.color.white))
        binding.leaveBtn.strokeWidth = 0

        binding.allBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.All.setTextColor(resources.getColor(R.color.white))
        binding.allBtn.strokeWidth = 0

        binding.presentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Present.setTextColor(resources.getColor(R.color.white))
        binding.presentBtn.strokeWidth = 0

        binding.lateBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Late.setTextColor(resources.getColor(R.color.white))
        binding.lateBtn.strokeWidth = 0
    }

    private fun changingPresentLayout() {
        binding.presentBtn.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.Present.setTextColor(resources.getColor(R.color.yellow))
        binding.presentBtn.setStrokeColor(resources.getColor(R.color.yellow))
        binding.presentBtn.strokeWidth = 1
        changeOtherThenPresent()

    }

    private fun changeOtherThenPresent() {
        binding.leaveBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Leave.setTextColor(resources.getColor(R.color.white))
        binding.leaveBtn.strokeWidth = 0

        binding.allBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.All.setTextColor(resources.getColor(R.color.white))
        binding.allBtn.strokeWidth = 0

        binding.absentBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Absent.setTextColor(resources.getColor(R.color.white))
        binding.absentBtn.strokeWidth = 0

        binding.lateBtn.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.Late.setTextColor(resources.getColor(R.color.white))
        binding.lateBtn.strokeWidth = 0

    }

    fun getCurentDate(): org.threeten.bp.LocalDate {
        return LocalDate.now()
    }

    private fun settingCurrentDate() {
        try {
            currentDate = navArg.statsList?.date!!
            val formatteddate = formatDate(currentDate)
            binding.txtcheckcto.text = formatteddate.toString()
            if (currentDate == checkdate) {
                binding.rightarrowcto.isEnabled = false
                binding.rightarrowcto.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_font
                    ), PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.rightarrowcto.isEnabled = true
                binding.rightarrowcto.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        } catch (e: Exception) {
        }
        val formatteddate = formatDate(currentDate)
        binding.txtcheckcto.text = formatteddate.toString()
    }

    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return date.format(formatter)
    }

    private fun calldepartmentattendaceListNext() {

        if (currentDateCto == checkdate) {
            binding.rightarrowcto.isEnabled = false
            binding.rightarrowcto.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_font
                ), PorterDuff.Mode.SRC_IN
            )// = true

        } else {

            val newdate = currentDateCto.plusDays(1)
            currentDateCto = newdate
            if (currentDateCto == checkdate) {
                binding.rightarrowcto.isEnabled = false
                binding.rightarrowcto.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_font
                    ), PorterDuff.Mode.SRC_IN
                )// = true

            }
            val formatteddate = formatDate(currentDateCto)
            binding.txtcheckcto.text = formatteddate.toString()
            var empId = sessionmanagement?.getEmpId()
            var viewid = sessionmanagement?.getViewId()
            callStaticticsGraphApi()
        }
    }

    private fun calldepartmrntattendancelistPrevious() {

        binding.rightarrowcto.isEnabled = true
        binding.rightarrowcto.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            ), PorterDuff.Mode.SRC_IN
        )// = true

        val newdate = currentDateCto.plusDays(-1)
        currentDateCto = newdate
        val formatteddate = formatDate(currentDateCto)

        binding.txtcheckcto.text = formatteddate.toString()


        callStaticticsGraphApi()
    }

    private fun callStaticticsGraphApi() {
        CoroutineScope(Dispatchers.Main).launch {

            homeViewModel.getStatisticsDashboardGraph(
                AttendanceGraphRequest(
                    departmentid = departmentid,
                    designationid = designationid,
                    endDate = currentDateCto.toString(),
                    genderid = genderid,
                    groupid = groupid,
                    officeid = officeid,
                    startDate = currentDateCto.toString()
                )
            )
        }
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
            LoggerGenratter.getInstance()
                .showToast(requireContext(), "No application found to open Excel files.")
        }
    }

    private fun livedataObserver() {

        homeViewModel._downloadGraphAttendence.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        try {
//
                            val sdf = SimpleDateFormat("ddMMyyyyHHHmmss")
                            val filenametitle =
                                "AIS_${sessionmanagement!!.getEmpId()}_${fromDate}_${toDate}"
                            val filename = filenametitle + "_.xlsx"
                            val file = File(requireActivity().getExternalFilesDir(null), filename)
                            val excel = file.writeBytes(response.data!!) //  dont delete or update it , it create file from bytes

                            openfileIntent(file)
                        } catch (e: Exception) {
                            LoggerGenratter.getInstance().printLog("ATTENDANCE FILE", e.message)
                        }

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

                        LoggerGenratter.getInstance()
                            .printLog("ATTENDANCE FILE", "Something went wrong")

                    }

                    is NetworkResult.Loading -> {

                    }
                }
            })
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            showProgressDialog(it)

        })

        homeViewModel._getGraphFilter.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showFilterDialog(response.data)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()

                }

                is NetworkResult.Loading -> {
                }
            }
        }

        homeViewModel._getStatisticsGraph.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {

                    response.data?.body?.ctoAndManagerModelresponceSummery?.let {
                        StatsCountList = it

                    }

                    response.data?.body?.employeedetailModelresponces?.let {
                        StatsEmployeeList = it
                        newListEmployee(it)
                    }
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
                    binding.rvemployeinfo.visibility = View.GONE

                }

                is NetworkResult.Loading -> {

                }
            }
        }

    }

    fun showFilterDialog(response: FilterResponse?) {
        if (filterDialog == null) {
            val binding = ShowfiltersdialogBinding.inflate(layoutInflater)
            filterDialog =
                Dialog(requireActivity(), androidx.transition.R.style.Theme_AppCompat_Dialog)
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
                    filterDialog!!.hide()
                    callStaticticsGraphApi()

                }
                tvcancel.setSafeOnClickListener {
                    clearAllIds()
                    callStaticticsGraphApi()
                    filterDialog!!.dismiss()
                    filterDialog = null
                }
                imgcancel.setSafeOnClickListener {
                    filterDialog!!.hide()

                }
                applyfilterrecyclerview.adapter = adapter
            }

            filterDialog!!.show()
        } else {
            filterDialog!!.show()
        }
    }

    private fun newListEmployee(
        it: List<EmployeedetailModelresponces>,
        isScroll: Boolean? = false
    ) {

        when (currentView) {
            "Present" -> {
                changingPresentLayout()
                changeOtherThenPresent()
                if (isScroll == true)
                    binding.horizontal.post {
                        binding.horizontal.smoothScrollTo(binding.presentBtn.left, 0)
                    }

                if (it != null) {
                    var presentEmployees = it!!.filter {
                        it.remarks?.lowercase()?.contains("present") == true
                    }

                    var lateEmployees = it!!.filter {
                        it.remarks?.lowercase()?.contains("late") == true
                    }
                    var combinedList = presentEmployees + lateEmployees
                    val total = presentEmployees.size + lateEmployees.size

                    if (presentEmployees.isNotEmpty() || lateEmployees.isNotEmpty()) {
                        seekbar(
                            true,
                            1,
                            totalEmployee = total,
                            presentEmployees.size,
                            0,
                            0,
                            lateEmployees.size
                        )
                        binding.seekBar.visibility = View.VISIBLE
                        binding.minProgress.show()
                        binding.maxProgress.show()
                        binding.maxProgress.text = total.toString()
                        initRecyclerView(combinedList)
                    } else {
                        binding.seekBar.progress = 0
                        binding.seekBar.visibility = View.GONE
                        binding.minProgress.hide()
                        binding.maxProgress.hide()
                        binding.rvemployeinfo.hide()
                        binding.txtnodata.show()
                        binding.txtnodata.text = "No record found"
                    }
                }
            }

            "Absent" -> {
                changingAbsentLayout()
                changeOtherThenAbsent()
                if (isScroll == true)
                    binding.horizontal.post {
                        binding.horizontal.smoothScrollTo(binding.absentBtn.left, 0)
                    }

                if (it != null) {
                    var absentEmployees = it!!.filter {
                        it.remarks?.lowercase()?.contains("absent") == true == true
                    }
                    if (absentEmployees.isNotEmpty()) {
                        seekbar(
                            false,
                            1,
                            totalEmployee = absentEmployees.size,
                            0,
                            absentEmployees.size,
                            0,
                            0
                        )
                        binding.seekBar.visibility = View.VISIBLE
                        binding.minProgress.show()
                        binding.maxProgress.show()
                        binding.maxProgress.text = absentEmployees.size.toString()
                        initRecyclerView(absentEmployees)
                    } else {
                        binding.seekBar.progress = 0
                        binding.seekBar.visibility = View.GONE
                        binding.minProgress.hide()
                        binding.maxProgress.hide()
                        binding.rvemployeinfo.hide()
                        binding.txtnodata.show()
                        binding.txtnodata.text = "No record found"
                    }
                }
            }

            "Leave" -> {
                changingLeaveLayout()
                changeOtherThenLeave()
                if (isScroll == true)
                    binding.horizontal.post {
                        binding.horizontal.smoothScrollTo(binding.leaveBtn.left, 0)
                    }

                if (it != null) {
                    var LeaveEmployees = it!!.filter {
                        it.remarks?.lowercase()?.contains("leave") == true == true
                    }
                    if (LeaveEmployees.isNotEmpty()) {
                        seekbar(
                            false,
                            1,
                            totalEmployee = LeaveEmployees.size,
                            0,
                            0,
                            LeaveEmployees.size,
                            0
                        )
                        binding.seekBar.visibility = View.VISIBLE
                        binding.minProgress.show()
                        binding.maxProgress.show()
                        binding.maxProgress.text = LeaveEmployees.size.toString()
                        initRecyclerView(LeaveEmployees)
                    } else {
                        binding.seekBar.progress = 0
                        binding.seekBar.visibility = View.GONE
                        binding.minProgress.hide()
                        binding.maxProgress.hide()
                        binding.rvemployeinfo.hide()
                        binding.txtnodata.show()
                        binding.txtnodata.text = "No record found"
                    }
                }
            }

            else -> {
                changingAllLayout()
                changeOtherThenAll()
                if (it != null) {
                    var presentEmployee = it!!.filter {
                        it.remarks?.lowercase()?.contains("present") == true
                    }
                    var lateEmployee = it!!.filter {
                        it.remarks?.lowercase()?.contains("late") == true
                    }
                    var leaveEmployee = it!!.filter {
                        it.remarks?.lowercase()?.contains("leave") == true
                    }
                    var absentEmployee = it!!.filter {
                        it.remarks?.lowercase()?.contains("absent") == true
                    }
                    if (it.isNotEmpty()) {
                        seekbar(
                            true,
                            20,
                            totalEmployee = it.size,
                            presentEmployee.size,
                            absentEmployee.size,
                            leaveEmployee.size,
                            lateEmployee.size
                        )
                        binding.seekBar.visibility = View.VISIBLE
                        binding.minProgress.show()
                        binding.maxProgress.show()
                        binding.maxProgress.text = it.size.toString()
                        initRecyclerView(it)

                    } else {
                        binding.seekBar.progress = 0
                        binding.seekBar.visibility = View.GONE
                        binding.minProgress.hide()
                        binding.maxProgress.hide()
                        binding.rvemployeinfo.hide()
                        binding.txtnodata.show()
                        binding.txtnodata.text = "No record found"
                    }
                }
            }
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Media And Camera Permission",
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.CAMERA
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
                            makeApiCall(currentDateCto.toString(), currentDateCto.toString())
                        }
                        override fun onPermissionCancel() {

                        }
                    })
            }
        } else {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Storage And Camera Permission",
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
                            makeApiCall(currentDateCto.toString(), currentDateCto.toString())
                        }
                        override fun onPermissionCancel() {

                        }
                    })
            }
        }
    }

//    private fun checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
//                permissionsUtil.requestPermission(
//                    "Please  Camera Permission",
//                    arrayOf(
//                        Manifest.permission.CAMERA
//                    ),
//                    true,
//                    object : PermissionsUtil.PermissionsListenerCallback {
//                        override fun onPermissionGranted() {
//                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
//                            makeApiCall(currentDateCto.toString(), currentDateCto.toString())
//                        }
//
//                        override fun onPermissionCancel() {
//                            LoggerGenratter.getInstance().printLog("Permission request", "Not Granted")
//                        }
//                    })
//            }
//        } else {
//            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
//                permissionsUtil.requestPermission(
//                    "Please Allow Storage Permission",
//                    arrayOf(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ),
//                    true,
//                    object : PermissionsUtil.PermissionsListenerCallback {
//                        override fun onPermissionGranted() {
//                            LoggerGenratter.getInstance().printLog("Permission request", "Granted")
//                            makeApiCall(currentDateCto.toString(), currentDateCto.toString())
//                        }
//
//                        override fun onPermissionCancel() {
//                            LoggerGenratter.getInstance().printLog("Permission request", "Not Granted")
//                        }
//                    })
//            }
//        }
//    }


    private fun makeApiCall(fromdate: String, todate: String) {


        val downloadGraphAttendence = DownloadGraphDataRequest(
            startDate = fromdate,
            endDate = todate,
            employeeid = sessionmanagement?.getEmpId(),
            status = attendanceStatus,
            departmentid = departmentid,
            designationid = designationid,
            officeid = officeid,
            groupid = groupid,
            genderid = genderid
        )

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.downloadGraphData(downloadGraphAttendence)
        }

    }

    private fun searchHandler() {

        binding.idSV.setOnTouchListener { v, event ->
            binding.idSV.hint = ""
            false
        }

        binding.idSV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                if (s.toString().isNullOrEmpty()) {
                    searchviewemployeeenable = false
                    binding.txtnorecordfoundemployee.hide()
                    setAdapter(filteredEmployeeAttendanceList as MutableList<EmployeedetailModelresponces>?)
                } else {
                    searchviewemployeeenable = true
                    searchEmployeeattedancelist?.clear()
                    for (employee in filteredEmployeeAttendanceList.orEmpty()) {
                        if (employee.name?.lowercase()?.contains(
                                s.toString().lowercase()
                            ) == true || employee.remarks?.lowercase()
                                ?.contains(s.toString().lowercase()) == true
                        ) {
                            binding.txtnorecordfoundemployee.hide()
                            searchEmployeeattedancelist?.add(employee)
                        } else {
                            binding.txtnorecordfoundemployee.show()
                        }
                    }
                    setAdapter(searchEmployeeattedancelist)
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Called after the text has been changed
                val newText = s.toString()
                // Perform any desired actions with the new text
            }
        })
    }

    fun setAdapter(list: MutableList<EmployeedetailModelresponces>?) {
        if (list != null) {
            if (list.isNotEmpty())
                if (list!!.isEmpty()) {
                    binding.txtnorecordfoundemployee.show()
                } else {
                    binding.txtnorecordfoundemployee.hide()
                }
        }
        val adapter = list?.let { StatisticsEmployeeAdapter(requireContext(), it, this) }
        binding.rvemployeinfo.adapter = adapter
    }

    private fun initRecyclerView(employeeattedancelist: List<EmployeedetailModelresponces>?) {
        if (employeeattedancelist != null && employeeattedancelist.isNotEmpty()) {
            employeeattedancelist.forEach {
                if (it.firstCheckIn != null) {
                    val firstcheckin = it.firstCheckIn?.split(" ")
                    if (firstcheckin!!.size >= 2) {
                        val finalfirstcheckin = firstcheckin[1]
                        it.firstCheckIn = finalfirstcheckin
                    }
                }
                if (it.lastCheckOut != null) {
                    val lastcheckout = it.lastCheckOut?.split(" ")
                    if (lastcheckout!!.size >= 2) {
                        val finalfirstcheckin = lastcheckout[1]
                        it.lastCheckOut = finalfirstcheckin
                    }
                }
            }
            // Your existing code for formatting time

            var sortedList = employeeattedancelist.sortedWith(
                compareByDescending<EmployeedetailModelresponces> {
                    when {
                        it.designation?.lowercase()?.contains("manager ") == true -> 2
                        it.designation?.lowercase()?.contains("team lead") == true || it.designation?.lowercase()?.contains("tl") == true ||it.designation?.lowercase()?.contains("lead") == true ->1
                        else -> 0
                    }
                }.thenByDescending { it.designation?.lowercase()?.contains("manager ") == true }
                    .thenBy {                         it.designation?.lowercase()?.contains("team lead") == true   || it.designation?.lowercase()?.contains("team lead") == true ||it.designation?.lowercase()?.contains("team lead") == true
                    }
                    .thenBy { it.name }
            )
            filteredEmployeeAttendanceList = sortedList
            binding.txtnodata.hide()
            binding.rvemployeinfo.show()
            val adapter = filteredEmployeeAttendanceList?.let { StatisticsEmployeeAdapter(requireContext(), it, this) }
            binding.rvemployeinfo.adapter = adapter
        } else {
            binding.rvemployeinfo.hide()
            binding.txtnodata.show()
            binding.txtnodata.text = "No record found"
        }
    }

    private fun clearAllIds() {
        departmentid.clear()
        designationid.clear()
        officeid.clear()
        groupid.clear()
        attendanceid = 0
        genderid.clear()
    }

    fun seekbar(
        ismultipleCheck: Boolean,
        segmentcheckValue: Int,
        totalEmployee: Int,
        presentEmployee: Int,
        absentEmployee: Int,
        leaveEmployee: Int,
        lateEmployee: Int
    ) {
        val totalStudents = totalEmployee
        val presentCount = presentEmployee
        val absentCount = absentEmployee
        val leaveCount = leaveEmployee
        val lateArrivalCount = lateEmployee

        val maxProgress = totalStudents

        if (totalStudents != null) {

            binding.seekBar.max = maxProgress

            val presentProgress = presentCount
            val absentProgress = presentProgress + absentCount
            val leaveProgress = absentProgress + leaveCount
            val lateArrivalProgress = leaveProgress + lateArrivalCount

            val progressDrawable = CustomSeekBarProgressDrawable(
                ismultipleCheck,
                segmentcheckValue,
                requireContext(),
                maxProgress,
                presentProgress,
                absentProgress,
                leaveProgress,
                lateArrivalProgress,
            )

            binding.seekBar.progressDrawable = progressDrawable
        }

    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {
    }

    private fun navigateToEmployeeDetails(position: Int) {
        if(searchviewemployeeenable)
        {
            var id = searchEmployeeattedancelist?.get(position)?.employeeID
            var designation = searchEmployeeattedancelist?.get(position)?.designation
            var firstcheckin = searchEmployeeattedancelist?.get(position)?.firstCheckIn
            var remarks = searchEmployeeattedancelist?.get(position)?.remarks
            var lastcheckout = searchEmployeeattedancelist?.get(position)?.lastCheckOut
            var deptid = searchEmployeeattedancelist?.get(position)?.departmentId
            var name = searchEmployeeattedancelist?.get(position)?.name
            val employeeinfo = EmployeeAttendanceInfo(
                name = name,
                employeeID = id,
                designation = designation,
                firstCheckIn = firstcheckin.toString(),
                lastCheckOut = lastcheckout.toString(),
                remarks = remarks,
                depID = deptid,
                status = "n/a",
                from = "EmpAttendance",
                date = currentDate
            )
            try {
                findNavController().navigate(
                    StatisticsFragmentDirections.actionStatisticsFragmentToEmployeeDetail(
                        employeeinfo
                    )
                )
            }
            catch (e:java.lang.Exception)
            {

            }
        }
        else{
            var id = filteredEmployeeAttendanceList ?.get(position)?.employeeID
            var designation = filteredEmployeeAttendanceList?.get(position)?.designation
            var firstcheckin = filteredEmployeeAttendanceList?.get(position)?.firstCheckIn
            var remarks = filteredEmployeeAttendanceList?.get(position)?.remarks
            var lastcheckout = filteredEmployeeAttendanceList?.get(position)?.lastCheckOut
            var deptid = filteredEmployeeAttendanceList?.get(position)?.departmentId
            var name = filteredEmployeeAttendanceList?.get(position)?.name
            val employeeinfo = EmployeeAttendanceInfo(
                name = name,
                employeeID = id,
                designation = designation,
                firstCheckIn = firstcheckin.toString(),
                lastCheckOut = lastcheckout.toString(),
                remarks = remarks,
                depID = deptid,
                status = "n/a",
                from = "EmpAttendance",
                date = currentDate
            )
            try {
                findNavController().navigate(
                    StatisticsFragmentDirections.actionStatisticsFragmentToEmployeeDetail(
                        employeeinfo
                    )
                )
            }
            catch (e:java.lang.Exception)
            {

            }
        }




    }
    override fun onAttendanceStausClick(position: Int) {
        navigateToEmployeeDetails(position)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._downloadGraphAttendence.postValue(null)
    }

}