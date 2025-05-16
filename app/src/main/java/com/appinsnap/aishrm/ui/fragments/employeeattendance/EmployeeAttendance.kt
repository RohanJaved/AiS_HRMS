package com.appinsnap.aishrm.ui.fragments.employeeattendance

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
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentEmployeeAttendanceBinding
import com.appinsnap.aishrm.ui.fragments.EmployeeHistory.model.DownloadAttendanceReq
import com.appinsnap.aishrm.ui.fragments.employeeattendance.adapter.DeptTypeAdapter
import com.appinsnap.aishrm.ui.fragments.employeeattendance.adapter.EmployeeAttendanceAdapter
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.*
import com.appinsnap.aishrm.ui.fragments.home.adapter.DepartmentEmployeeAdapter
import com.appinsnap.aishrm.ui.fragments.home.adapter.DepartmentGroupAdapter
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardRequest
import com.appinsnap.aishrm.ui.fragments.home.models.CTOManagerDashboardResponse
import com.appinsnap.aishrm.ui.fragments.home.models.DepartmentGroup
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.*
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.google.android.material.card.MaterialCardView
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
class EmployeeAttendance :GeneralDialogFragment(), GeneralDialogFragment.onClick, DepartmentEmployeeAdapter.onDeptMenuClick,
    GeneralDialogFragment.locationPermission, EmployeeAttendanceAdapter.onItemClick,DepartmentGroupAdapter.onGroupItemCick {
    private var _binding: FragmentEmployeeAttendanceBinding? = null
    private var txttodate: TextView? = null
    private var deptId:Int=0
    private var searchviewdepartmentenable:Boolean=false
    private var searchviewemployeeenable = false
    private var employeeattendanceview: Boolean = false
    private var employeeattedancelist: MutableList<BodyX>? = null
    private var searchEmployeeattedancelist: MutableList<BodyX>? = mutableListOf()
    private var searchDepartmentlist: MutableList<BodyXX>? = mutableListOf()
    private var txtfromdate: TextView? = null
    private var departmentgrouplist: java.util.ArrayList<DepartmentGroup> = java.util.ArrayList()
    private var sessionmanagement: SessionManagement? = null
    lateinit var currentDate: LocalDate
    var addeddataresponselist: java.util.ArrayList<BodyXX>? = java.util.ArrayList()
    var checkdate = LocalDate.now()
    var deptdataresponselist: List<BodyXX>? = null
    private var toDate: String = ""
    var department: String = ""
    private var fromDate: String = ""
    lateinit var currentDateCto: org.threeten.bp.LocalDate
    var departmentId: Int = 0
    private lateinit var spinner : Spinner

    private  var departmentAdapter : DeptTypeAdapter? = null
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
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employee_attendance,
            container,
            false
        )
        val view = binding.root
//        setDeptmentData()
        sessionmanagement = SessionManagement(requireContext())
        currentDate = navArg.empAtDetail!!.date!!

        searchviewemployeeenable = false

        if (checkdate != currentDate) {
            binding.rightarrow.isEnabled = true
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow
                ), PorterDuff.Mode.SRC_IN
            )//

        } else {
            binding.rightarrow.isEnabled = false
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_font
                ), PorterDuff.Mode.SRC_IN
            )//

        }
        settingCurrentDate()
        getDepartmentId()
        getManagerCtoEmployeeList()
        // Inflate the layout for this fragment
        searchHandler()

        return view
    }
    fun setDeptmentData(){
        var selectedDep= navArg.empAtDetail?.depID!!
        if (addeddataresponselist?.isNotEmpty() == true){
            if(addeddataresponselist!!.size==1||addeddataresponselist!!.size==2){
                binding.mcvleaves.layoutParams.height = 120
            }
            else if (addeddataresponselist!!.size==3){
                binding.mcvleaves.layoutParams.height = 250
            }
            else{
                binding.mcvleaves.layoutParams.height = 410
            }

            departmentAdapter = DeptTypeAdapter(requireContext(), addeddataresponselist!!, object : DeptTypeAdapter.OnitemDeptclick{
                override fun onClickDepartment(item: BodyXX, position: Int) {
                    selectedDep=position
                    sessionmanagement?.setSelectedDeptId(selectedDep)
                    sessionmanagement?.setDepartmentId(item.departmentId)

                    if (!binding.depttype.text.toString().equals("")){
                        binding.depttype.text = item.departmentName
                    }
                    if(binding.rvleavestype.isVisible) {
                        binding.downarrow.setImageResource(R.drawable.downarrow)
                        binding.mcvleaves.visibility=View.GONE
                        binding.rvleavestype.visibility=View.GONE
                    }
                    getEmployeeList(selectedDep,addeddataresponselist!!)
                }

            },false)
            binding.rvleavestype.layoutManager = LinearLayoutManager(requireContext())
            binding.rvleavestype.adapter = departmentAdapter


            if (binding.depttype.text.toString().equals("")){
                binding.depttype.text = addeddataresponselist!!.get(0).departmentName
            }

            if (isFirst) {
                binding.depttype.text = navArg.empAtDetail!!.department
                isFirst = false
                temp++
                getEmployeeList(selectedDep, addeddataresponselist!!)
            }
            else{
                if (temp==2 || temp ==0){
                    temp=1
                    selectedDep = sessionmanagement?.getSelectedDeptId()!!
                    if(selectedDep<= addeddataresponselist!!.size-1)
                    {
                        binding.depttype.text = addeddataresponselist!!.get(selectedDep).departmentName
                        getEmployeeList(selectedDep, addeddataresponselist!!)
                    }
                    else{
                        binding.depttype.text = addeddataresponselist!!.get(0).departmentName
                        getEmployeeList(0, addeddataresponselist!!)
                    }
                }
                temp++
            }

        }
    }

    var temp=0

    private fun getEmployeeList(position:Int,item: java.util.ArrayList<BodyXX>) {
        sessionmanagement?.setDepartmentId(item.get(position).departmentId)
        val ctomanageremployeerequest =
            sessionmanagement?.getEmpId()?.let {
                sessionmanagement?.getViewId()?.let { it1 ->
                    ManagerCTOEmployeeRequest(
                        employeeID = it.toInt(),
                        currentDate = currentDate.toString(),
                        dID = item.get(position).departmentId
                    )
                }
            }
        CoroutineScope(Dispatchers.Main).launch {
            if (ctomanageremployeerequest != null) {
                homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
            }
        }
    }

    fun getCurentDate(): org.threeten.bp.LocalDate {
        return LocalDate.now()
    }

    private fun searchHandler() {
        binding.idSV.setOnTouchListener(OnTouchListener { v, event ->
            binding.idSV.setHint("")
            false
        })

        binding.idSV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                if (s.toString() == null || s.toString().isEmpty()) {
                    searchviewemployeeenable = false
                    binding.txtnorecordfoundemployee.hide()
                    setAdapter(employeeattedancelist as MutableList<BodyX>?)

                } else {
                    searchviewemployeeenable = true
//                    searchEmployeeattedancelist= mutableListOf()
                    searchEmployeeattedancelist!!.clear()
                    for (i in employeeattedancelist!!.indices) {


                        if ((employeeattedancelist != null && employeeattedancelist!!.get(i).name.lowercase().contains(s.toString().lowercase()))||(employeeattedancelist != null && employeeattedancelist!!.get(i).remarks.lowercase().contains(s.toString().lowercase()))
                        ) {
                            binding.txtnorecordfoundemployee.hide()
                            searchEmployeeattedancelist?.add(employeeattedancelist!!.get(i))

                        }
                        else{
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

    fun setAdapter(list: MutableList<BodyX>?) {
        if (list != null) {
            if(list.isNotEmpty())
                if(list!!.isEmpty()) {
                    binding.txtnorecordfoundemployee.show()
                } else{
                    binding.txtnorecordfoundemployee.hide()
                }
        }
        val adapter = list?.let { EmployeeAttendanceAdapter(requireContext(), it, this) }
        binding.rvemployeinfo.adapter = adapter
    }

    private fun getDepartmentId() {
        departmentId = sessionmanagement?.getDptidTemp()!!
    }

    private fun settingCurrentDate() {
        try {
            currentDate = navArg.empAtDetail?.date!!
            currentDateCto=navArg.empAtDetail?.date!!
            val formatteddate = formatDate(currentDate)
            binding.txtcheck.text = formatteddate.toString()
            if (currentDate == checkdate) {
                binding.rightarrow.isEnabled = false
                binding.rightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_font
                    ), PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.rightarrow.isEnabled = true
                binding.rightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        } catch (e: Exception) {

        }
        val formatteddate = formatDate(currentDate)
        binding.txtcheck.text = formatteddate.toString()
    }

    val navArg: EmployeeAttendanceArgs by navArgs()

    private fun getManagerCtoEmployeeList() { // Current date
        var empid = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        if (viewid == 2) {
            binding.mcvemployeeattendance.visibility = View.VISIBLE
            binding.dept.hide()
            binding.mcvdatemanager.visibility = View.VISIBLE
            binding.mcvdatecto.hide()
            binding.mcvleavestypes.visibility = View.GONE
            binding.mcvleaves.visibility = View.GONE
            if (navArg.empAtDetail?.depID == sessionmanagement?.getDepartmentId()) {
                departmentId = sessionmanagement?.getDepartmentId()!!.toInt()

            } else {
                departmentId = navArg.empAtDetail?.depID!!

            }

            val ctomanageremployeerequest =
                empid?.let {
                    viewid?.let { it1 ->
                        ManagerCTOEmployeeRequest(
                            employeeID = it.toInt(),
                            currentDate = currentDate.toString(),
                            dID = departmentId
                        )
                    }
                }
            CoroutineScope(Dispatchers.Main).launch {
                if (ctomanageremployeerequest != null) {
                    homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
                }
            }
        }
        if(viewid==4){
            binding.mcvemployeeattendance.visibility = View.VISIBLE
            binding.dept.hide()
            binding.mcvdatemanager.visibility = View.VISIBLE
            binding.mcvdatecto.hide()
            binding.mcvleavestypes.visibility = View.GONE
            binding.mcvleaves.visibility = View.GONE
            if (navArg.empAtDetail?.depID == sessionmanagement?.getDepartmentId()) {
                departmentId = sessionmanagement?.getDepartmentId()!!.toInt()

            } else {
                departmentId = navArg.empAtDetail?.depID!!

            }

            val ctomanageremployeerequest =
                empid?.let {
                    viewid?.let { it1 ->
                        ManagerCTOEmployeeRequest(
                            employeeID = it.toInt(),
                            currentDate = currentDate.toString(),
                            dID = departmentId
                        )
                    }
                }
            CoroutineScope(Dispatchers.Main).launch {
                if (ctomanageremployeerequest != null) {
                    homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
                }
            }
        }

        if (viewid == 1) {
            settingDate()

            binding.mcvemployeeattendance.visibility = View.VISIBLE
            binding.dept.visibility = View.VISIBLE
            val adapter = DepartmentGroupAdapter(
                requireContext(),
                navArg.empAtDetail?.verticalsList as ArrayList<DepartmentGroup>, this
            )
            binding.rvctodept.adapter = adapter
            onGroupClick(Constants.SELECTEDdpt)

        }

    }

    var isFirst:Boolean=true;
    fun getDetailsAgain(){

    }

    private fun settingDate() {
        try {
            currentDate = navArg.empAtDetail?.date!!
            val formatteddate = formatDate(currentDate)
            binding.txtcheck.text = formatteddate.toString()
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
        binding.txtcheckcto.text = formatteddate
    }

    private fun initRecyclerView(response: NetworkResult.Success<ManagerCtoEmployeeResponse>) {
        if (response != null) {
            binding.rvemployeinfo.visibility = View.VISIBLE

            response.data?.body?.forEach {
                if (it.lastCheckOut != null) {
                    var newcheckout = it.lastCheckOut.split(" ")
                    var firstindexcheckout = newcheckout[1]
                    it.lastCheckOut = firstindexcheckout
                }
                if (it.firstCheckIn != null) {
                    var newcheckin = it.firstCheckIn.split(" ")
                    var firstindexcheckin = newcheckin[1]
                    it.firstCheckIn = firstindexcheckin
                }
            }

            // Create a new ArrayList and add elements from the original list
            employeeattedancelist = response.data?.body?.toMutableList()

            // Sorting in ascending order by the name property
            var sortedList = employeeattedancelist?.sortedWith(
                compareByDescending<BodyX> {
                    when {
                        it.designation?.lowercase()?.contains("manager ") == true -> 2
                        it.designation?.lowercase()?.contains("team lead") == true -> 1
                        else -> 0
                    }
                }.thenBy { it.name }
            )
            employeeattedancelist = sortedList?.toMutableList()

            val adapter =
                employeeattedancelist?.let { EmployeeAttendanceAdapter(requireContext(), it, this) }
            binding.rvemployeinfo.adapter = adapter
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        livedataObserver()
        onClickListeners()
        currentDateCto = navArg.empAtDetail?.date!!
        checkdate = getCurentDate()
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
    private fun livedataObserver() {
        homeViewModel._downloadAttendence.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        try {
//                            val fileDir = requireContext().getExternalFilesDir(null)
                            val sdf = SimpleDateFormat("ddMMyyyyHHHmmss")
                            val cd = sdf.format(Date())
                            val filenametitle = "AIS_${sessionmanagement!!.getEmpId()}_${fromDate}_${toDate}"
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

//                        calendarHandling(response.data?.body)
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
        homeViewModel._managerandctoemployeedetail.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        binding.mcvemployeeattendance.visibility = View.VISIBLE
                        binding.mcvdatemanager.visibility=View.VISIBLE
                        binding.mcvdatecto.visibility=View.GONE
                        initRecyclerView(response)


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

            })

        homeViewModel._managerctodashboardresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        displayDepartmentAttendance(response)
                    }

                    is NetworkResult.Error -> {
                        binding.txtnorecordfoundemployee.visibility = View.VISIBLE
                        binding.mcvemployeeattendance.visibility=View.GONE

                    }

                    is NetworkResult.Loading -> {

                    }
                }

            })

    }


    private fun displayDepartmentAttendance(response: NetworkResult.Success<CTOManagerDashboardResponse>) {
        deptdataresponselist = response.data?.body
        if (response != null) {
            var result = response.data?.body?.distinctBy { it.group }
            if (result != null) {
                if (departmentgrouplist.isEmpty()) {
                    for (i in result) {
                        departmentgrouplist?.add(DepartmentGroup(i.group))
                    }
                }
            }
            var groupname = departmentgrouplist
            var string = ""
            val adapter = DepartmentGroupAdapter(requireContext(), groupname, this)
            binding.rvctodept.adapter = adapter
            binding.rvctodept.scrollToPosition(Constants.SELECTEDdpt)
            onGroupClick(Constants.SELECTEDdpt)
        } else {
            binding.txtnorecordfoundemployee.visibility = View.VISIBLE
        }

    }

    private fun onClickListeners() {
        binding.download.setSafeOnClickListener {
            checkPermission()
        }
        binding.rightarrow.setSafeOnClickListener {
            callApiManagerCtoEmployeeListNext()
        }
        binding.leftarrow.setSafeOnClickListener {
            callApiManagerCtoEmployeeListPrevious()
        }
        binding.rightarrowcto.setOnClickListener {
            calldepartmentattendaceListNext()
        }
        binding.leftarrowcto.setOnClickListener {
            calldepartmrntattendancelistPrevious()
        }

        binding.mcvleavestypes.setOnClickListener {
            if(binding.rvleavestype.isVisible) {
                binding.downarrow.setImageResource(R.drawable.downarrow)
                binding.mcvleaves.visibility=View.GONE
                binding.rvleavestype.visibility=View.GONE
            }
            else{
                binding.downarrow.setImageResource(R.drawable.uparrow)
                binding.mcvleaves.visibility=View.VISIBLE
                binding.rvleavestype.visibility=View.VISIBLE
            }


        }
        checkRecyclerViewVisibility()
    }

    private fun checkRecyclerViewVisibility() {
        setDeptmentData()
    }
    private fun calldepartmrntattendancelistPrevious() {
        binding.rightarrowcto.isEnabled = true
        employeeattendanceview=true
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

    private fun calldepartmentattendaceListNext() {
        employeeattendanceview = true
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


            try {
                val managerdashboardrequest = CTOManagerDashboardRequest(
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

    private fun callApiManagerCtoEmployeeListPrevious() {
        var empid = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        deptId = sessionmanagement!!.getDptidTemp()
        binding.rightarrow.isEnabled = true
        binding.rightarrow.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.yellow),
            PorterDuff.Mode.SRC_IN
        )// = true

        val newdate = currentDate.plusDays(-1)
        currentDate = newdate
        val formatteddate = formatDate(currentDate)
        val ctomanageremployeerequest =
            empid?.let {
                viewid?.let { it1 ->
                    ManagerCTOEmployeeRequest(
                        employeeID = it.toInt(),
                        currentDate = currentDate.toString(),
                        dID = deptId
                    )
                }
            }
        CoroutineScope(Dispatchers.Main).launch {
            if (ctomanageremployeerequest != null) {
                homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
            }
        }
        binding.txtcheck.text = formatteddate.toString()

    }

    private fun callApiManagerCtoEmployeeListNext() {
        var empid = sessionmanagement?.getEmpId()
        var viewid = sessionmanagement?.getViewId()
        deptId = sessionmanagement!!.getDptidTemp()
        if (checkdate == currentDate) {
            binding.rightarrow.isEnabled = false
            binding.rightarrow.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray
                ), PorterDuff.Mode.SRC_IN
            )

        } else {
            currentDate = currentDate.plusDays(1)
            if (checkdate == currentDate) {

                binding.rightarrow.isEnabled = false
                binding.rightarrow.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_gray
                    ), PorterDuff.Mode.SRC_IN
                )

            }
            // = true

            val formatteddate = formatDate(currentDate)
            val ctomanageremployeerequest =
                empid?.let {
                    viewid?.let { it1 ->
                        ManagerCTOEmployeeRequest(
                            employeeID = it.toInt(),
                            currentDate = currentDate.toString(),
                            dID = deptId
                        )
                    }
                }
            CoroutineScope(Dispatchers.Main).launch {
                if (ctomanageremployeerequest != null) {
                    homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
                }
            }

            binding.txtcheck.text = formatteddate.toString()

        }
    }


    private fun makeApiCall(fromdate: String, todate: String) {

        val downloadAttendanceReq = DownloadAttendanceReq(
            employeeID = sessionmanagement?.getEmpId(),
            fromDate = fromdate,
            toDate = todate,
            dID = sessionmanagement?.getDptidTemp(),
            IsSelf = false
        )

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.downloadAttendence(downloadAttendanceReq)
        }
    }

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


        fromDate = ""
        toDate = ""
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
                makeApiCall(fromDate, toDate)

                toDate = ""
                fromDate = ""
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

    private fun validateDatesInput(): Pair<Boolean, String> {
        var inputtodate = toDate
        var inputfromdate = fromDate

        return homeViewModel.validateDateCredentials(inputtodate, inputfromdate)
    }

    private fun showToDateCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    toDate = toDates
                    txttodate?.text = toDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    toDate = toDates
                    txttodate?.text = toDate

                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    toDate = toDates
                    txttodate?.text = toDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    toDate = toDates
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

    private fun showFromDateCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    fromDate = toDates
                    txtfromdate?.text = fromDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    fromDate = toDates
                    txtfromdate?.text = fromDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    fromDate = toDates
                    txtfromdate?.text = fromDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    fromDate = toDates
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

    override fun onclick(value: Boolean) {
    }

    override fun onLocationPermission(locationvalue: Boolean) {
    }

    override fun onitemclick(position: Int) {
        itemClickHandler(position)
    }


    private fun itemClickHandler(position: Int) {
        if(!searchviewemployeeenable) {
            var id = employeeattedancelist?.get(position)?.employeeID
            var designation = employeeattedancelist?.get(position)?.designation
            var firstcheckin = employeeattedancelist?.get(position)?.firstCheckIn
            var remarks = employeeattedancelist?.get(position)?.remarks
            var lastcheckout = employeeattedancelist?.get(position)?.lastCheckOut
            var deptid = employeeattedancelist?.get(position)?.departmentId
            var name = employeeattedancelist?.get(position)?.name

            val employeeinfo =
                EmployeeAttendanceInfo(
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
        EmployeeAttendanceDirections.actionEmployeeattendanceToEmployeeDetail(
            employeeinfo
        )
    )
}
catch (e:java.lang.Exception)
{

}

        }
        else{
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
                    EmployeeAttendanceDirections.actionEmployeeattendanceToEmployeeDetail(
                        employeeinfo
                    )
                )
            }
            catch (e:Exception)
            {

            }

        }
    }

    fun formatDate(date: LocalDate): String {

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return date.format(formatter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._managerandctoemployeedetail.postValue(null)
        homeViewModel._downloadAttendence.postValue(null)
    }
    override fun onGroupClick(position: Int) {
        binding.mcvleaves.hide()
        if (employeeattendanceview) {
            addeddataresponselist?.clear()
            binding.mcvemployeeattendance.visibility = View.GONE
            binding.mcvdatecto.visibility=View.VISIBLE
            binding.mcvdatemanager.visibility=View.GONE
            department = departmentgrouplist?.get(position)?.name.toString()
            if (deptdataresponselist != null) {
                for (i in deptdataresponselist!!) {
                    if (i.group.contains(department.toString())) {

                        addeddataresponselist?.add(
                            BodyXX(
                                i.absentCount,
                                i.departmentName,
                                i.group,
                                i.lateCount,
                                i.presentCount,
                                i.leaveCount,
                                i.departmentId
                            )
                        )

                    }

                }

            }
        }

        else {
            addeddataresponselist?.clear()
            binding.mcvemployeeattendance.visibility = View.VISIBLE
            binding.rvemployeinfo.visibility = View.VISIBLE
            binding.mcvdatecto.visibility=View.VISIBLE
            binding.mcvdatemanager.visibility=View.GONE
            var verticallist: ArrayList<DepartmentGroup> = navArg.empAtDetail?.verticalsList as ArrayList<DepartmentGroup>
            if(verticallist.size <=1){
                binding.dept.visibility = View.GONE
            }else{
                binding.dept.visibility = View.VISIBLE
            }
            department = verticallist?.get(position)?.name.toString()
            var alldeptlist: List<BodyXX> = (navArg.empAtDetail!!.alldeptlist as List<BodyXX>?)!!
            if (alldeptlist != null) {
                for (i in alldeptlist) {
                    if (i.group.contains(department)) {
                        addeddataresponselist?.add(
                            BodyXX(
                                i.absentCount,
                                i.departmentName,
                                i.group,
                                i.lateCount,
                                i.presentCount,
                                i.leaveCount,
                                i.departmentId
                            )
                        )
                    }
                }
                setDeptmentData()
            }
        }
    }

    override fun ondeptitemclick(position: Int, from: String) {
        if (from == "empattendance") {
            if (!searchviewdepartmentenable)
            {
                try {
                    var empid = sessionmanagement?.getEmpId()
                    var viewid = sessionmanagement?.getViewId()
                    var deptid = addeddataresponselist?.get(position)?.departmentId
                    if (deptid != null) {
                        sessionmanagement?.setDepartmentId(deptid)
                    }

                    // view 1
                    val ctomanageremployeerequest =
                        empid?.let {
                            viewid?.let { it1 ->
                                ManagerCTOEmployeeRequest(
                                    employeeID = it.toInt(),
                                    currentDate = currentDate.toString(),
                                    dID = deptid!!
                                )
                            }
                        }

                    CoroutineScope(Dispatchers.Main).launch {
                        if (ctomanageremployeerequest != null) {
                            homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
                        }
                    }

                } catch (e: Exception) {

                }
            }
            else{
                try {
                    var empid = sessionmanagement?.getEmpId()
                    var viewid = sessionmanagement?.getViewId()
                    var deptid = searchDepartmentlist?.get(position)?.departmentId
                    if (deptid != null) {
                        deptId =deptid
                    }
                    if (deptid != null) {
                        sessionmanagement?.setDepartmentId(deptid)
                    }

                    val ctomanageremployeerequest =
                        empid?.let {
                            viewid?.let { it1 ->
                                ManagerCTOEmployeeRequest(
                                    employeeID = it.toInt(),
                                    currentDate = currentDate.toString(),
                                    dID = deptid!!
                                )
                            }
                        }
                    CoroutineScope(Dispatchers.Main).launch {
                        if (ctomanageremployeerequest != null) {
                            homeViewModel.getManagerCtoEmployeeList(ctomanageremployeerequest)
                        }
                    }

                } catch (e: Exception) {

                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.idSV.setText("")
        binding.idSV.setHint("Search")
    }
}
