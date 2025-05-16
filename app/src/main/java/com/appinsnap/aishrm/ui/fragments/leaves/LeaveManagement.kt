package com.appinsnap.aishrm.ui.fragments.leaves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.leaves.adapter.LeavesDashboardAdapter
import com.appinsnap.aishrm.ui.fragments.leaves.adapter.RecentLeavesAdapter
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailRequestModel
import com.appinsnap.aishrm.ui.fragments.leaves.models.LeavesDetailResponseModel
import com.appinsnap.aishrm.ui.fragments.leaves.models.RecentLeave
import com.appinsnap.aishrm.ui.fragments.models.leaveinformation
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.Exception

@AndroidEntryPoint
class LeaveManagement :GeneralDialogFragment(){
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private var sessionmanagement: SessionManagement? = null
    //use binding instance to avoid null check in every where in statement
    private val binding get() = _binding!!
    private var _binding:com.appinsnap.aishrm.databinding.FragmentLeaveManagementBinding? = null
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leave_management, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        callleavesdetailapi()
        liveDataObserver()
        // Inflate the layout for this fragment
        return view
    }

    private fun callleavesdetailapi() {
        var empid = sessionmanagement?.getEmpId()
        var leavesrequest = LeavesDetailRequestModel(employeeID = empid!!.toInt())
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getLeavesDetailInformation(leavesrequest)
        }
    }

    private fun liveDataObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(it)
        })

        homeViewModel._leavesdetailinfo.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        LeavesDashboardInformation(response)
                        initRecyclerView(response)
                    }
                    is NetworkResult.Error -> {
                        binding.norec.visibility=View.VISIBLE
                    }
                    is NetworkResult.Loading -> {

                    }
                }

            })
    }

    private fun LeavesDashboardInformation(response: NetworkResult.Success<LeavesDetailResponseModel>) {
        if (response != null) {
            var leavescount = response.data?.body?.getLeaveCount
            if(leavescount!=null)
            {
                val adapter = LeavesDashboardAdapter(requireContext(),leavescount)
                binding.rvleavesdashboard.adapter = adapter
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() {
        binding.applyleave.setSafeOnClickListener {
            try {
                findNavController().navigate(R.id.action_leavemanagement_to_leaveapplicationfragment)

            }
            catch (e:Exception)
            {

            }
        }
    }

    private fun initRecyclerView(response: NetworkResult.Success<LeavesDetailResponseModel>) {
        if (response != null) {
            var leavestatuslist: List<RecentLeave>? = response.data?.body?.recentLeave
            if (leavestatuslist != null) {
                binding.rvrecentleaves.visibility=View.VISIBLE
                binding.norec.visibility=View.GONE
                if (leavestatuslist.isNotEmpty()) {
                    leavestatuslist.forEach {
                        try{
                            if(it.startDate!=null)
                            {
                                var newdate =    it.startDate.split("T")
                                var newstartdate = newdate[0]
                                val startdateconvert = LocalDate.parse(newstartdate, inputFormat)
                                val startformattedDate = outputFormat.format(startdateconvert)
                                it.startDate = startformattedDate
                            }
                        }
                        catch (e:Exception)
                        {

                        }
                        try {

                            if(it.endDate!=null)
                            {
                                var newdate2 =    it.endDate.split("T")
                                var newenddate = it.endDate.split("T")[0]
                                val enddateconvert = LocalDate.parse(newenddate, inputFormat)
                                val endformattedDate = outputFormat.format(enddateconvert)
                                it.endDate = endformattedDate
                            }
                        }
                        catch (e:java.lang.Exception)
                        {

                        }

                      try {
                          if(it.appliedDate!=null)
                          {
                              var applydate = it.appliedDate.split("T")
                              var newappldate = applydate[0]
                              var splitapplydate = newappldate.takeLast(2)
                              var newmonth = applydate.toString().split("-")
                              var newmonthtxte = newmonth[1].toInt()
                              var newmonthcoverttext = DateFormatSymbols().months[newmonthtxte - 1]
                              var trimmonthtext = newmonthcoverttext.substring(0,3)
                              it.appliedDate = splitapplydate+" "+trimmonthtext
                          }
                      }
                      catch (e:java.lang.Exception)
                      {

                      }


                    }
                    val adapter = RecentLeavesAdapter(requireContext(), leavestatuslist)
                    binding.rvrecentleaves.adapter = adapter

                }
                else{
                    binding.norec.visibility=View.VISIBLE
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._leavesdetailinfo.postValue(null)
    }



}