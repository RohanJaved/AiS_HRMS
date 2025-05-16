package com.appinsnap.aishrm.ui.fragments.LeaaveApplication

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.CustomDialoggueBinding
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.adapter.LeavesTypeAdapter
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.adapter.SelectedFilesAdapter
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.models.ImagePickerModel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.leaves.adapter.LeavesDashboardAdapter
import com.appinsnap.aishrm.ui.fragments.leaves.models.GetLeaveCount
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailRequestModel
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailResponseModel
import com.appinsnap.aishrm.util.AppUtils
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.RetrievePDFFromURL
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.wallet.zindigi.Utils.PermissionsUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import worker8.com.github.radiogroupplus.RadioGroupPlus
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random


@AndroidEntryPoint
class LeaveApplication : LeavesTypeAdapter.onitemleaveclick, GeneralDialogFragment(),
    GeneralDialogFragment.onClick, GeneralDialogFragment.locationPermission {
    private val binding get() = _binding!!
    var fromDate: String = ""
    var leaveid: Int = 0
    val random = Random()
    var attatchmentReq: Boolean = false
    var leavename: String = ""
    var attachmentLimit: Int = 0
    var noOfDays: String = "1"
    val calendar = Calendar.getInstance()
    private var characterLimit:Int=0
    var formattedSaturdayDate: String = ""
    var formattedSundayDate: String = ""
    var totalavailableleaves: Int = 0
    var leavestypelist: List<GetLeaveCount>? = null
    var toDate: String = ""
    private val FILE_PICKER_REQUEST = 1
    var cameraImageUri: Uri? = null
    var filterDialog: Dialog? = null
    private lateinit var selectedFilesAdapter: SelectedFilesAdapter
    private val selectedFiles = mutableListOf<ImagePickerModel>()
    private val selectedFilesRequest = mutableListOf<File>()
    private var _binding: com.appinsnap.aishrm.databinding.FragmentLeaveApplicationBinding? = null

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private var sessionmanagement: SessionManagement? = null
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_leave_application, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        settingCharacterLimit()
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        callleavesdashboardApi()
        return view
    }

    private fun settingCharacterLimit() {
        characterLimit =  sessionmanagement!!.getCharacterLimit()
        val maxLength = characterLimit.toInt()
        val inputFilter = InputFilter.LengthFilter(maxLength)
        binding.edtReason.filters = arrayOf(inputFilter)
    }

    private fun callleavesdashboardApi() {
        binding.txtdate.text = LocalDate.now().toString()
        checkWeekendDate()
        val empid = sessionmanagement?.getEmpId()
        val leavesrequest = LeavesDetailRequestModel(employeeID = empid!!.toInt())
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getLeavesDetailInformation(leavesrequest)
        }
    }

    private fun checkWeekendDate() {
        val selectedCalendar = Calendar.getInstance()
        val selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
        val isSaturday = selectedDayOfWeek == Calendar.SATURDAY
        val isSunday = selectedDayOfWeek == Calendar.SUNDAY
        if (isSaturday) {
            formattedSaturdayDate = LocalDate.now().toString()
        }
        if (isSunday) {
            formattedSundayDate = LocalDate.now().toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        livedataObserver()
        onClickListeners()
    }

    fun isFileSizeWithinLimit(uri: Uri, limitInMB: Int): Boolean {
        val contentResolver = requireContext().contentResolver
        val fileSize = contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
            descriptor.statSize
        } ?: 0L
        val fileSizeInMB = fileSize / (1024 * 1024)
        Log.d("Filesize", "File size in bytes: $fileSizeInMB")
        Log.d("Filesize", "Limit in bytes: ${fileSize}")
        return fileSizeInMB < limitInMB
    }


    private fun livedataObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, {
            showProgressDialog(it)
        })

        homeViewModel._leavesdetailinfo.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    LeavesDashboardInformation(response)
                    initLeavesTypeAdapter(response)
                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {

                }
            }

        })
        homeViewModel._applyleaveresponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.e("TAG", "livedataObserver: The response coming is ${response}")
                    if (response != null) {

                        showLeaveSuccessMesaage(response.data!!.statusMessage)
                    }
                }

                is NetworkResult.Error -> {
                    binding.txtleavebalerror.visibility = View.GONE
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

    private fun initLeavesTypeAdapter(response: NetworkResult.Success<LeavesDetailResponseModel>) {
        if (response != null) {
            if (response.data?.body?.getLeaveCount?.isNotEmpty() == true) {
                leavestypelist = response.data?.body?.getLeaveCount
                if (leavestypelist.let { it?.isNotEmpty() == true } || leavestypelist != null) {
                    if (leavestypelist!!.size == 1) {
                        binding.mcvleaves.layoutParams.height = 100
                    } else if (leavestypelist!!.size > 1) {
                        binding.mcvleaves.layoutParams.height = 255
                    } else {

                    }
                    val adapter = LeavesTypeAdapter(requireContext(), leavestypelist, this)
                    binding.rvleavestype.adapter = adapter
                    leavestypelist?.forEach {
                        if (it.name.lowercase().contains("casual")) {
                            binding.leavetype.text = it.name
                            leavename = it.name
                            leaveid = it.id
                            var availableLeaves: Int = it.totalLeave - it.availLeave
                            totalavailableleaves = availableLeaves
                            if (it.hasComments) {
                                binding.cardReason.visibility = View.VISIBLE
                                binding.txtreason.visibility = View.VISIBLE
                            } else {
                                binding.cardReason.hide()
                                binding.txtreason.hide()
                            }
                            if (it.hasMultipleDays) {
                                toDate = ""
                                fromDate = ""
                                binding.radioMDay.visibility = View.VISIBLE
                                binding.txtclientvisit1.visibility = View.VISIBLE
                                binding.layoutDate.visibility = View.VISIBLE
                                binding.layoutdate.visibility = View.INVISIBLE

                            } else {

                                binding.layoutdate.visibility = View.VISIBLE
                                binding.mcvdatelayout.visibility = View.VISIBLE
                                binding.layoutDate.visibility = View.GONE
                                binding.date.visibility = View.VISIBLE
                                binding.txtdate.visibility = View.VISIBLE
                                binding.radioMDay.hide()
                                binding.txtclientvisit1.hide()
                            }
                        }
                    }
                }
            } else {
                binding.downarrow.isEnabled = false
                binding.leavetype.text = "Select leave type"
            }
        }
    }

    private fun LeavesDashboardInformation(response: NetworkResult.Success<LeavesDetailResponseModel>) {
        if (response != null) {
            var leavescount = response.data?.body?.getLeaveCount
            val adapter = LeavesDashboardAdapter(requireContext(), leavescount)
            binding.rvlleavesdashboard.adapter = adapter

        }
    }

    private fun onClickListeners() {
        binding.chooseBtn.setOnClickListener {
            if (selectedFiles.size < attachmentLimit) {
                getPermissions()
            } else {
                showGeneralDialogFragment(
                    requireContext(),
                    false,
                    this,
                    true,
                    "yes",
                    yourTitle = "",
                    yourMessage = "Maximum $attachmentLimit attachments are allowed.",
                    location = this,
                    icon = R.drawable.reject
                )
            }
        }
        binding.radioGroupDays.setOnCheckedChangeListener(RadioGroupPlus.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioOneDay -> {
                    noOfDays = "1"
                    toDate = LocalDate.now().toString()
                    fromDate = LocalDate.now().toString()
                    binding.mcvdatelayout.show()
                    binding.layoutDate.visibility = View.INVISIBLE
                    binding.layoutdate.visibility = View.VISIBLE
                    binding.layoutAttachments.visibility = View.GONE
                    binding.txtAttachments.visibility = View.GONE
                }

                R.id.radioMDay -> {
                    toDate = ""
                    fromDate = ""
                    noOfDays=""
                    binding.tvFromDate.text="From"
                    binding.tvToDate.text="To"
                    binding.mcvdatelayout.hide()
                    binding.layoutDate.show()
                    binding.layoutdate.visibility = View.INVISIBLE
                }
            }
        })

        binding.mcvdatelayout.setOnClickListener {
            showdatedialog()
        }
        binding.mcvleavestypes.setSafeOnClickListener {
            if (leavestypelist?.isNotEmpty() == true) {
                binding.downarrow.isEnabled = true
                checkRecyclerViewVisibility()
            } else {
                binding.downarrow.isEnabled = false
            }
        }
        binding.submit.setOnClickListener {
            callApplyLeaveApi()
        }
        binding.cardViewFrom.setOnClickListener {
            showfromdatedialog()
        }
        binding.cardViewTo.setOnClickListener {
            showtodatedialog()
        }
    }

    private fun showfromdatedialog() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.YEAR, 1)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DAY_OF_MONTH, 31)
        val MaxDate = c.timeInMillis
        val dpd = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, monthOfYear, day)

                val selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                val isSaturday = selectedDayOfWeek == Calendar.SATURDAY
                val isSunday = selectedDayOfWeek == Calendar.SUNDAY
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    fromDate = toDates
                    binding.tvFromDate?.text = fromDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    fromDate = toDates
                    binding.tvFromDate?.text = fromDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    fromDate = toDates
                    binding.tvFromDate?.text = fromDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    fromDate = toDates
                    binding.tvFromDate?.text = fromDate

                }
                calculateDays(toDate, fromDate)

                if (isSaturday) {
                    formattedSaturdayDate = fromDate
                } else if (isSunday) {
                    formattedSundayDate = fromDate
                    // Do something for Sunday
                }

            }, year, month, day
        )
        dpd?.datePicker?.minDate = System.currentTimeMillis() - 1000
        dpd?.datePicker?.maxDate = MaxDate
        dpd.show()
    }

    private fun showtodatedialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.YEAR, 1)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DAY_OF_MONTH, 31)
        val MaxDate = c.timeInMillis
        val dpd = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, monthOfYear, day)

                val selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                val isSaturday = selectedDayOfWeek == Calendar.SATURDAY
                val isSunday = selectedDayOfWeek == Calendar.SUNDAY
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    toDate = toDates
                    binding.tvToDate?.text = toDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    toDate = toDates
                    binding.tvToDate?.text = toDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    toDate = toDates
                    binding.tvToDate?.text = toDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    toDate = toDates
                    binding.tvToDate?.text = toDate

                }

                calculateDays(toDate, fromDate)

                if (isSaturday) {
                    formattedSaturdayDate = toDate
                } else if (isSunday) {
                    formattedSundayDate = toDate
                    // Do something for Sunday
                }

            }, year, month, day
        )
        dpd?.datePicker?.minDate = System.currentTimeMillis() - 1000
        dpd?.datePicker?.maxDate = MaxDate
        dpd.show()
    }

    private fun calculateDays(toDate: String, fromDate: String) {
        var hasAttachment: Boolean = false
        var hasTwoDaysAttachment: Boolean = true
        var hasOneDayAttachment: Boolean = true
        if (toDate.isNotEmpty() && fromDate.isNotEmpty()) {
            var todatenew = LocalDate.parse(toDate)
            var fromdatenew = LocalDate.parse(fromDate)
            var days = 0L
            var currentDate = fromdatenew

            while (currentDate.isBefore(todatenew) || currentDate.isEqual(
                    todatenew
                )
            ) {
                if (currentDate.dayOfWeek != DayOfWeek.SATURDAY && currentDate.dayOfWeek != DayOfWeek.SUNDAY) {
                    days++
                }
                currentDate = currentDate.plusDays(1)
            }
            noOfDays = (days).toString()
        }

        if (leavestypelist.isNullOrEmpty()) {

        } else {
            leavestypelist!!.forEach {
                if (it.name.lowercase().contains("medical")) {
                    attachmentLimit = it.documentcount
                    hasAttachment = it.hasAttachment
                    hasOneDayAttachment = it.hasOneDayAttachment
                    hasTwoDaysAttachment = it.hasTwoDaysAttachment
                }

            }

        }
        if (leavename.lowercase().contains("medical")) {
            if (hasAttachment && hasOneDayAttachment && hasTwoDaysAttachment) {
                attatchmentReq = true
                binding.layoutAttachments.show()
                if (selectedFiles.isNotEmpty()) {
                    selectedFiles.clear()
                    binding.chooseFileTV.show()
                }
                binding.txtAttachments.show()
            } else if (!hasAttachment && hasOneDayAttachment && hasTwoDaysAttachment) {
                if (noOfDays == 2.toString() || noOfDays == 1.toString()) {
                    attatchmentReq = true
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()
                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    setAttachmentAdapter()

                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else if (hasAttachment && !hasOneDayAttachment && hasTwoDaysAttachment) {
                if (noOfDays == 2.toString() || noOfDays > 2.toString()) {
                    attatchmentReq = true
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()

                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    setAttachmentAdapter()
                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else if (hasAttachment && hasOneDayAttachment && !hasTwoDaysAttachment) {
                if (noOfDays == 1.toString() || noOfDays > 2.toString()) {
                    attatchmentReq = true
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()
                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    setAttachmentAdapter()

                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else if (hasAttachment && !hasOneDayAttachment && !hasTwoDaysAttachment) {
                if (noOfDays > 2.toString()) {
                    attatchmentReq = true
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()
                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    setAttachmentAdapter()

                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else if (!hasAttachment && !hasOneDayAttachment && hasTwoDaysAttachment) {
                if (noOfDays == 2.toString()) {
                    attatchmentReq = true
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()
                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    setAttachmentAdapter()

                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else if (!hasAttachment && hasOneDayAttachment && !hasTwoDaysAttachment) {
                if (noOfDays == 1.toString()) {
                    attatchmentReq = true
                    binding.layoutAttachments.show()
                    binding.txtAttachments.show()
                    if (selectedFiles.isNotEmpty()) {
                        selectedFiles.clear()
                        binding.chooseFileTV.show()
                    }
                    if (selectedFilesRequest.isNotEmpty()) {
                        selectedFilesRequest.clear()
                    }
                    setAttachmentAdapter()


                } else {
                    attatchmentReq = false
                    binding.layoutAttachments.hide()
                    binding.txtAttachments.hide()
                }
            } else {

                attatchmentReq = false
                binding.layoutAttachments.hide()
                binding.txtAttachments.hide()
            }


        }
    }

    private fun setAttachmentAdapter() {
        selectedFilesAdapter = SelectedFilesAdapter(object : SelectedFilesAdapter.onClickListener {
            override fun onclick(data: Uri, position: Int, from: Int) {
                when (from) {
                    0 -> {
                        showFilterDialog(data)
                        Log.e("TAG", "onclick: the uri coming in the reponse is ${data}")
                    }

                    1 -> {
                        selectedFiles.removeAt(position)
                        selectedFilesRequest.removeAt(position)
                        if (selectedFiles.size > 0) {
                            binding.chooseFileTV.visibility = View.GONE
                        } else {
                            binding.chooseFileTV.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }, selectedFiles, requireContext())
        binding.selectedFilesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.selectedFilesRecyclerView.adapter = selectedFilesAdapter
    }

    private fun callApplyLeaveApi() {
        if (leavename.isEmpty()) {
            showGeneralDialogFragment(
                requireContext(),
                false,
                this,
                false,
                "yes",
                yourTitle = "",
                yourMessage = "Please select leave type",
                location = this,
                icon = R.drawable.reject
            )
        } else {

            leavestypelist?.forEach {
                if (it.name == leavename) {
                    if (binding.radioGroupDays.checkedRadioButtonId != -1) {
                        if (it.hasMultipleDays) {
                            if (binding.radioGroupDays.checkedRadioButtonId == R.id.radioMDay) {
                                val validationresult = validateDatesInput()
                                if (validationresult.first) {

                                    if ((toDate.equals(formattedSaturdayDate) || toDate.equals(
                                            formattedSundayDate
                                        ) || toDate.equals(formattedSundayDate) || toDate.equals(
                                            formattedSundayDate
                                        )) && (fromDate.equals(formattedSundayDate) || fromDate.equals(
                                            formattedSaturdayDate
                                        ) || fromDate.equals(formattedSaturdayDate) || fromDate.equals(
                                            formattedSaturdayDate
                                        ))
                                    ) {
                                        showGeneralDialogFragment(
                                            requireContext(),
                                            false,
                                            this,
                                            false,
                                            "yes",
                                            yourTitle = "",
                                            yourMessage = "You cannot apply leave on weekend",
                                            location = this,
                                            icon = R.drawable.reject
                                        )
                                    } else {
                                        if ((fromDate.equals(formattedSundayDate) || fromDate.equals(
                                                formattedSaturdayDate
                                            ) || fromDate.equals(formattedSaturdayDate) || fromDate.equals(
                                                formattedSaturdayDate
                                            )) || ((toDate.equals(formattedSundayDate) || toDate.equals(
                                                formattedSaturdayDate
                                            ) || toDate.equals(formattedSaturdayDate) || toDate.equals(
                                                formattedSaturdayDate
                                            )))
                                        ) {
                                            showGeneralDialogFragment(
                                                requireContext(),
                                                false,
                                                this,
                                                false,
                                                "yes",
                                                yourTitle = "",
                                                yourMessage = "You cannot apply leave on weekend",
                                                location = this,
                                                icon = R.drawable.reject
                                            )
                                        } else {
                                            if (noOfDays.toInt() <= totalavailableleaves) {
                                                binding.txtleavebalerror.visibility = View.GONE
                                                if (it.hasComments) {
                                                    var comments = binding.edtReason.text.trim()
                                                    if (comments.isEmpty()) {
                                                        showGeneralDialogFragment(
                                                            requireContext(),
                                                            false,
                                                            this,
                                                            false,
                                                            "yes",
                                                            yourTitle = "",
                                                            yourMessage = "Add reason for Leave",
                                                            location = this,
                                                            icon = R.drawable.reject
                                                        )
                                                    } else {
                                                        if (attatchmentReq) {
                                                            if (selectedFiles.isEmpty()) {
                                                                showGeneralDialogFragment(
                                                                    requireContext(),
                                                                    false,
                                                                    this,
                                                                    false,
                                                                    "yes",
                                                                    yourTitle = "",
                                                                    yourMessage = "No file chosen",
                                                                    location = this,
                                                                    icon = R.drawable.reject
                                                                )
                                                            } else {
                                                                callApiForLeave()
                                                            }
                                                        } else {
                                                            callApiForLeave()
                                                        }

                                                    }
                                                }
                                            } else {
                                                binding.txtleavebalerror.visibility = View.VISIBLE
                                            }

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
                            } else {
                                if ((toDate.equals(formattedSaturdayDate) || toDate.equals(
                                        formattedSundayDate
                                    )) && (fromDate.equals(formattedSundayDate) || fromDate.equals(
                                        formattedSaturdayDate
                                    ))
                                ) {
                                    showGeneralDialogFragment(
                                        requireContext(),
                                        false,
                                        this,
                                        false,
                                        "yes",
                                        yourTitle = "",
                                        yourMessage = "You cannot apply leave on weekend",
                                        location = this,
                                        icon = R.drawable.reject
                                    )
                                } else {

                                    if (noOfDays.toInt() <= totalavailableleaves) {
                                        binding.txtleavebalerror.visibility = View.GONE
                                        if (it.hasComments) {
                                            var comments = binding.edtReason.text.trim()
                                            if (comments.isEmpty()) {
                                                showGeneralDialogFragment(
                                                    requireContext(),
                                                    false,
                                                    this,
                                                    false,
                                                    "yes",
                                                    yourTitle = "",
                                                    yourMessage = "Add reason for Leave",
                                                    location = this,
                                                    icon = R.drawable.reject
                                                )
                                            } else {
                                                callApiForLeave()
                                            }
                                        } else {
                                            callApiForLeave()
                                        }
                                    } else {
                                        binding.txtleavebalerror.visibility = View.VISIBLE
                                    }
                                }
                            }

                        } else {
                            if ((toDate.equals(formattedSaturdayDate) || toDate.equals(
                                    formattedSundayDate
                                ) || toDate.equals(formattedSundayDate) || toDate.equals(
                                    formattedSundayDate
                                )) && (fromDate.equals(formattedSundayDate) || fromDate.equals(
                                    formattedSaturdayDate
                                ) || fromDate.equals(formattedSaturdayDate) || fromDate.equals(
                                    formattedSaturdayDate
                                ))
                            ) {
                                showGeneralDialogFragment(
                                    requireContext(),
                                    false,
                                    this,
                                    false,
                                    "yes",
                                    yourTitle = "",
                                    yourMessage = "You cannot apply leave on weekend",
                                    location = this,
                                    icon = R.drawable.reject
                                )
                            } else {
                                if (noOfDays != null && totalavailableleaves != null) {
                                    if (noOfDays.toInt() <= totalavailableleaves) {
                                        binding.txtleavebalerror.visibility = View.GONE
                                        if (it.hasComments) {
                                            var comments = binding.edtReason.text.trim()
                                            if (comments.isEmpty()) {
                                                showGeneralDialogFragment(
                                                    requireContext(),
                                                    false,
                                                    this,
                                                    false,
                                                    "yes",
                                                    yourTitle = "",
                                                    yourMessage = "Add reason for Leave",
                                                    location = this,
                                                    icon = R.drawable.reject
                                                )
                                            } else {
                                                callApiForLeave()
                                            }
                                        } else {
                                            callApiForLeave()
                                        }
                                    } else {
                                        binding.txtleavebalerror.visibility = View.VISIBLE
                                    }
                                }

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
                            yourMessage = "Please Select Day ",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }
                }

            }

        }
    }

    private fun callApiForLeave() {
        Log.e("TAG", "The data going in the list is  ${selectedFilesRequest}")
        var empid = sessionmanagement?.getEmpId()
        var comments = binding.edtReason.text.toString().trim()
        if (comments.isEmpty()) {
            comments = "--"
        } else {
            comments = binding.edtReason.text.toString()
        }
        var leavetypeid = leaveid

        val body = MultipartBody.Builder()

        body.setType(MultipartBody.FORM)
        body.addFormDataPart("Comment", comments)
        body.addFormDataPart("Employeeid", empid!!.toInt().toString())
        body.addFormDataPart("EndDate", toDate)
        body.addFormDataPart("LeaveTypeID", leavetypeid.toString())
        body.addFormDataPart("NoofDays", noOfDays)
        body.addFormDataPart("StartDate", fromDate)
        if (selectedFilesRequest.isNotEmpty()) {
            val fileProfile = "file_1"
            selectedFilesRequest.forEach { file ->
                val mimeType = URLConnection.guessContentTypeFromName(file.name)
                val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
                body.addFormDataPart("Attachment", file.name, requestFile)
            }
            /* val requestBody = body.build()
             CoroutineScope(Dispatchers.Main).launch {
                 homeViewModel.getApplyLeave(requestBody)
             }*/
        }
        val requestBody = body.build()
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getApplyLeave(requestBody)
        }
    }

    private fun checkRecyclerViewVisibility() {

        if (binding.rvleavestype.visibility == View.GONE) {
            binding.downarrow.setImageResource(R.drawable.uparrow)
            binding.mcvleaves.visibility = View.VISIBLE
            binding.rvleavestype.visibility = View.VISIBLE
        } else {
            binding.downarrow.setImageResource(R.drawable.downarrow)
            binding.mcvleaves.visibility = View.GONE
            binding.rvleavestype.visibility = View.GONE
        }
    }

    private fun showdatedialog() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.YEAR, 1)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DAY_OF_MONTH, 31)
        val MaxDate = c.timeInMillis


        val dpd = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, day ->
                // Display Selected date in TextView
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, monthOfYear, day)

                val selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                val isSaturday = selectedDayOfWeek == Calendar.SATURDAY
                val isSunday = selectedDayOfWeek == Calendar.SUNDAY
                var mmonth = monthOfYear + 1
                if (mmonth < 10 && day < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + 0 + day
                    fromDate = toDates
                    binding.txtdate.text = fromDate

                } else if (mmonth < 10) {
                    val toDates = "" + year + "-" + 0 + mmonth + "-" + day
                    fromDate = toDates
                    binding.txtdate.text = fromDate


                } else if (day < 10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0 + day
                    fromDate = toDates
                    binding.txtdate.text = fromDate

                } else {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    fromDate = toDates
                    binding.txtdate.text = fromDate
                }

                toDate = fromDate
                fromDate = fromDate

                if (isSaturday) {
                    formattedSaturdayDate = toDate
                    formattedSaturdayDate = fromDate
                } else if (isSunday) {
                    formattedSundayDate = toDate
                    formattedSundayDate = fromDate
                    // Do something for Sunday
                }
            }, year, month, day
        )
        dpd.datePicker.maxDate = MaxDate
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    override fun onleaveclick(position: Int) {
        if (leavestypelist?.get(position)?.name != null) {
            leavename = leavestypelist?.get(position)?.name.toString()
        }
        if (leavestypelist?.get(position)?.id != null) {
            leaveid = leavestypelist?.get(position)?.id!!
        }
        if (leavestypelist!!.get(position).availLeave != null && leavestypelist!!.get(position).totalLeave != null) {
            var availleave = leavestypelist!!.get(position).availLeave
            var totalleave = leavestypelist!!.get(position).totalLeave
            totalavailableleaves = totalleave - availleave
        }
        calculateDays(toDate, fromDate)
        binding.txtAttachments.hide()
        binding.layoutAttachments.hide()
        binding.leavetype.text = leavename
        binding.radioGroupDays.clearCheck()
        binding.txtleavebalerror.visibility = View.GONE
        binding.rvleavestype.hide()
        binding.downarrow.setImageResource(R.drawable.downarrow)
        binding.mcvleaves.hide()
        if (leavestypelist != null) {
            leavestypelist!!.forEach {
                if (it.name == leavename) {
                    if (it.hasComments) {
                        binding.cardReason.visibility = View.VISIBLE
                        binding.txtreason.visibility = View.VISIBLE
                    } else {
                        binding.cardReason.hide()
                        binding.txtreason.hide()
                    }
                    if (it.hasMultipleDays) {
                        toDate = ""
                        fromDate = ""
                        noOfDays = ""
                        binding.tvFromDate.text = "From"
                        binding.tvToDate.text = "To"
                        binding.radioMDay.visibility = View.VISIBLE
                        binding.txtclientvisit1.visibility = View.VISIBLE
                        binding.layoutDate.visibility = View.VISIBLE
                        binding.layoutdate.visibility = View.INVISIBLE
                    } else {
                        var currentdate = LocalDate.now().toString()
                        binding.txtdate.text = currentdate
                        toDate = currentdate
                        fromDate = currentdate
                        binding.layoutdate.visibility = View.VISIBLE
                        binding.mcvdatelayout.visibility = View.VISIBLE
                        binding.layoutDate.hide()
                        binding.date.visibility = View.VISIBLE
                        binding.txtdate.visibility = View.VISIBLE
                        binding.radioMDay.hide()
                        binding.txtclientvisit1.hide()
                    }

                }
            }

        }
    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {
    }

    private fun validateDatesInput(): Pair<Boolean, String> {
        var inputtodate = toDate
        var inputfromdate = fromDate

        return homeViewModel.validateDateCredentials(inputtodate, inputfromdate)
    }

    private fun showLeaveSuccessMesaage(statusMessage: String) {
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
                findNavController().navigate(R.id.action_leaveapplicationfragment_to_leavemanagement)
            } catch (e: java.lang.Exception) {

            }

            permissiondialog.dismiss()

        }
        if (permissiondialog.isShowing) {
            permissiondialog.dismiss()
        }

        permissiondialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._leavesdetailinfo.postValue(null)
        homeViewModel._applyleaveresponse.postValue(null)
    }


    private fun imagePickerDialog() {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.camera_dialog)
//        logoutDialog.window?.setWindowAnimations(R.style.DialogNoAnimation)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        val camera: ConstraintLayout = logoutDialog.findViewById(R.id.camera)
        val gallery: ConstraintLayout = logoutDialog.findViewById(R.id.gallery)

        camera.setSafeOnClickListener {
            openCamera()
            logoutDialog.dismiss()
        }

        gallery.setSafeOnClickListener {
            openFilePicker()
            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "NewPic")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text")
        cameraImageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )!!
        val cameraIntenet = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntenet.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        cameraLauncher.launch(cameraIntenet)
    }

    private fun openFilePicker() {
        val mimeTypes = arrayOf(
            "image/*",                  // Images
            "application/pdf",          // PDFs
        )

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes) // Filter by MIME types
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Allow multiple file selection
        startActivityForResult(intent, FILE_PICKER_REQUEST)
    }

    private fun addSelectedFile(imagePickerModel: ImagePickerModel) {
        if (selectedFiles.size < attachmentLimit) {
            imagePickerModel?.uri?.let { uri ->
                if (isFileSizeWithinLimit(uri, 5)) {
                    uriToFile(requireContext(), uri)?.let { selectedFilesRequest.add(it) }
                    selectedFiles.add(ImagePickerModel(name = imagePickerModel.name, uri = uri))
                    selectedFilesAdapter.notifyItemInserted(selectedFiles.size - 1)
                } else {
                    showGeneralDialogFragment(
                        requireContext(),
                        false,
                        this,
                        true,
                        "yes",
                        yourTitle = "",
                        yourMessage = "Attachment exceeds the limit of 5 mb.",
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
                true,
                "yes",
                yourTitle = "",
                yourMessage = "Maximum $attachmentLimit attachments are allowed.",
                location = this,
                icon = R.drawable.reject
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {

                    FILE_PICKER_REQUEST -> {
                        data?.let {
                            val selectedUris = it.clipData
                            if (selectedUris != null) {
                                if (selectedUris.itemCount > attachmentLimit) {
                                    showGeneralDialogFragment(
                                        requireContext(),
                                        false,
                                        this,
                                        true,
                                        "yes",
                                        yourTitle = "",
                                        yourMessage = "Maximum $attachmentLimit attachments are allowed.",
                                        location = this,
                                        icon = R.drawable.reject
                                    )
                                } else {
                                    for (i in 0 until selectedUris.itemCount) {
                                        val uri: Uri? = selectedUris.getItemAt(i).uri
                                        uri?.let {
                                            binding.chooseFileTV.visibility = View.GONE
                                            val fileName = getFileNameFromUri(requireContext(), it)
                                            addSelectedFile(ImagePickerModel(fileName, it))
                                        }
                                    }
                                }
                            } else {
                                val uri: Uri? = it.data
                                uri?.let {
                                    val fileName = getFileNameFromUri(requireContext(), it)
                                    binding.chooseFileTV.visibility = View.GONE
                                    addSelectedFile(ImagePickerModel(fileName, it))
                                }
                            }
                        }
                    }

                }


            }
        } catch (e: java.lang.Exception) {

        }

    }

    fun getPermissions() {
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
                            imagePickerDialog()
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
                            imagePickerDialog()
                        }

                        override fun onPermissionCancel() {

                        }
                    })
            }
        }
    }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cameraImageUri?.let {
                    val randomInt = random.nextInt(15 - 1 + 1) + 1
                    val randomName = "Image ${randomInt.toString()}"
                    addSelectedFile(ImagePickerModel(randomName, it))
                    binding.chooseFileTV.visibility = View.GONE
                }

            }
        }


    fun getFileType(filePath: String): String {
        // Get the file extension from the file path
        val fileExtension = getFileExtension(filePath)

        // Determine the file type based on the file extension
        return when (fileExtension) {
            "pdf" -> "PDF Document"
            "jpg", "jpeg", "png", "gif" -> "Image"
            else -> "Unknown"
        }
    }

    fun getFileExtension(filePath: String): String {
        val lastDotIndex = filePath.lastIndexOf(".")
        return if (lastDotIndex > 0) {
            filePath.substring(lastDotIndex + 1).toLowerCase()
        } else {
            ""
        }
    }

    fun showFilterDialog(uri: Uri) {
        val fileType = getFileType(uri.toString())
        if (true) {
            val binding = CustomDialoggueBinding.inflate(layoutInflater)
            filterDialog =
                Dialog(requireActivity(), androidx.transition.R.style.Theme_AppCompat_Dialog)
            filterDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            filterDialog!!.setContentView(binding.getRoot())
            filterDialog!!.setCancelable(true)
            when (fileType) {
                "PDF Document" -> {
                    with(binding) {
                        try {
                            RetrievePDFFromURL(progressBarPdf,pdfViewLeave).execute(uri.toString())
                        //    pdfViewLeave.fromUri(uri).swipeHorizontal(false).load()
                            imgView.visibility = View.GONE
                            btnClose.setOnClickListener {
                                filterDialog!!.dismiss()
                                filterDialog = null
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "Exception:" + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                "Image" -> {
                    with(binding) {
                        try {
                            imgView.setImageURI(uri)
                            pdfViewLeave.visibility = View.GONE
                            btnClose.setOnClickListener {
                                filterDialog!!.dismiss()
                                filterDialog = null
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "Exception:" + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                "Unknown" -> {
                    with(binding) {
                        try {
                            imgView.setImageURI(uri)
                            pdfViewLeave.visibility = View.GONE
                            btnClose.setOnClickListener {
                                filterDialog!!.dismiss()
                                filterDialog = null
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "Exception:" + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            filterDialog!!.show()
        } else {
            filterDialog!!.show()
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var displayName: String? = null

        if (uri.scheme == "file") {
            // Handle file:// scheme
            displayName = File(uri.path).name
        } else if (uri.scheme == "content") {
            // Handle content:// scheme
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        displayName = it.getString(displayNameIndex)
                    }
                }
            }
        }
        return displayName
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        var file: File? = null

        try {
            inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val fileExtension = getFileExtensionFromUri(contentResolver, uri)
                // Create a temporary file with the original file extension
                file = createTempFile(context, fileExtension)

                outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return null
    }


    private fun getFileExtensionFromUri(contentResolver: ContentResolver, uri: Uri): String {
        val mimeType = contentResolver.getType(uri)
        return when (mimeType) {
//            "image/jpeg" -> "jpg"
            "image/jpeg", "image/jpg" -> "jpg"
            "image/png" -> "png"
            "application/pdf" -> "pdf"
            // Handle more mime types as needed
            else -> "unknown" // Default extension for unknown types
        }
    }

    private fun createTempFile(context: Context, fileExtension: String): File {
        val timeStamp = System.currentTimeMillis()
        val fileName = "temp_${timeStamp}.${fileExtension}"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, "." + fileExtension, storageDir)
    }


}