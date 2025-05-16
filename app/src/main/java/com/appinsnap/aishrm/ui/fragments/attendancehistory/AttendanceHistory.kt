package com.appinsnap.aishrm.ui.fragments.attendancehistory

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.BaseFragment
import com.appinsnap.aishrm.databinding.FragmentAttendanceHistoryBinding
import com.appinsnap.aishrm.ui.fragments.attendancehistory.adapter.AttendanceHistoryAdapter
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryModel
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryResponseModel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AttendanceHistory : BaseFragment() {
    private lateinit var binding:FragmentAttendanceHistoryBinding
    private var sessionmanagement: SessionManagement?=null
    private var empId:String=""
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private val homeViewModel: HomeViewModel by viewModels()

    private var toDate: String=""
    private var fromDate:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_attendance_history,container,false)
        val view =  binding.root
        binding.txtfromdate.text = "From Date"
        binding.txttodate.text = "To Date"

        progressBarDialogRegister = ProgressBarDialog(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickListener()
        observeData(view)
    }

    private fun onClickListener() {
        binding.mcvfromDate.setSafeOnClickListener{
            displayfromDateDialog()
        }
        binding.mcvtoDate.setSafeOnClickListener{
            displaytoDateDialog()
        }
        binding.attendancehistory.setSafeOnClickListener{
            getUserAttendanceHistory(it)
        }
    }

    private fun getUserAttendanceHistory(view:View) {
        sessionmanagement = SessionManagement(requireContext())

        empId = sessionmanagement!!.getEmpId()
//        val validationresult = validateUserInput()
//        if (validationresult.first) {
//
//            val attendancehistoryrequest =
//                AttendanceHistoryRequestModel(
//                    employeeID = empId,
//                    fromDate = fromDate,
//                    toDate = toDate
//                )
//
//            fromDate=""
//            toDate=""
//            binding.txtfromdate.text="From Date"
//            binding.txttodate.text="To Date"
//
//            CoroutineScope(Dispatchers.Main).launch {
//                homeViewModel.getAttendanceHistory(attendancehistoryrequest)
//
//            }
//
//
//        }
//        else{
//            view.showSnackbar(validationresult.second)
//
//        }
    }

    private fun observeData(view: View) {

        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(it)
        })
        homeViewModel._attendancehistory.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    displayUserAttendanceHistory(response)
                    view.showSnackbar("${response.data?.statusMessage}")

                    //  Snackbar.make(,"Login Successful", Snackbar.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    view.showSnackbar("${response.message}")
                }
                is NetworkResult.Loading -> {

                }
            }

        }
    }

//    private fun validateUserInput(): Pair<Boolean, String> {
//        var inputtodate = toDate
//        var inputfromdate = fromDate
//
//      //  return homeViewModel.validateDateCredentials(inputtodate,inputfromdate)
//    }


    private fun displayUserAttendanceHistory(response: NetworkResult.Success<AttendanceHistoryResponseModel>)
    {
        var attendanceHistoryResponse = response.data?.body
//        binding.mcvAttendanceDetail.visibility = View.VISIBLE
//        binding.txtname.text = response.data?.body?.get(0)?.name
//        binding.txtemail.text = response.data?.body?.get(0)?.email
//        binding.txtdatehistory.text = response.data?.body?.get(0)?.date
//        binding.txtcheckintimehistory.text = response.data?.body?.get(0)?.checkInTime
//        binding.txtcheckouthistory.text = response.data?.body?.get(0)?.checkOutTime
        val attendanceHistoryList = ArrayList<AttendanceHistoryModel>()
        if (attendanceHistoryResponse != null) {
            for(i in attendanceHistoryResponse )
            {
                attendanceHistoryList.add(AttendanceHistoryModel(i.checkInTime,i.checkOutTime,i.date,i.email,i.name))
            }
            binding.rvattendancehistory.visibility = View.VISIBLE
            val adapter = AttendanceHistoryAdapter(requireContext(),attendanceHistoryList)
            binding.rvattendancehistory.adapter=adapter

        }
    }

    private fun displaytoDateDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear+1
                if(mmonth<10&&day<10) {
                    val toDates = "" + year + "-" + 0+mmonth + "-" + 0+day
                    toDate = toDates
                    binding.txttodate.text=toDate

                }
                else if(mmonth<10)
                {
                    val toDates = "" + year + "-" + 0+mmonth + "-" + day
                    toDate = toDates
                    binding.txttodate.text=toDate


                }
                else  if(day<10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0+day
                    toDate = toDates
                    binding.txttodate.text=toDate

                }
                else
                {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    toDate = toDates
                    binding.txttodate.text=toDate


                }

            },
            year,
            month,
            day
        )
        dpd.show()


    }

    private fun displayfromDateDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, day ->
                // Display Selected date in TextView
                var mmonth = monthOfYear+1
                if(mmonth<10&&day<10) {
                    val toDates = "" + year + "-" + 0+mmonth + "-" + 0+day
                    fromDate = toDates
                    binding.txtfromdate.text=fromDate

                }
                else if(mmonth<10)
                {
                    val toDates = "" + year + "-" + 0+mmonth + "-" + day
                    fromDate = toDates
                    binding.txtfromdate.text=fromDate


                }
              else  if(day<10) {
                    val toDates = "" + year + "-" + mmonth + "-" + 0+day
                    fromDate = toDates
                    binding.txtfromdate.text=fromDate

                }
                else
                {
                    val toDates = "" + year + "-" + mmonth + "-" + day
                    fromDate = toDates
                    binding.txtfromdate.text=fromDate


                }

            },
            year,
            month,
            day
        )
        dpd.show()


    }



}