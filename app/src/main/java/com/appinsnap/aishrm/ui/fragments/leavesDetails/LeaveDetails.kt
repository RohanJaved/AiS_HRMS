package com.appinsnap.aishrm.ui.fragments.leavesDetails

import android.app.ActionBar
import android.app.Dialog
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.FragmentLeaveDetailsBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectRequestModel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.leavesDetails.adapter.LeaveDetailsAdapter
import com.appinsnap.aishrm.ui.fragments.leavesDetails.models.TodayModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class LeaveDetails : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission{
    private var _binding: FragmentLeaveDetailsBinding?= null
    private val binding get() = _binding!!
    private var sessionmanagement: SessionManagement? = null
    private var message: String = ""
    private var attachment: String = ""
    private var status:String=""
    var employeeID: Int = 0
    var notificationId: Int = 0
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    var isEnabled: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        livedataObserver()
    }

    private fun livedataObserver() {

        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(isShow = it)

        })

        homeViewModel._accceptrejectresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        showLeaveSuccessMessage(response.data?.statusMessage)
                    }
                    is NetworkResult.Error ->{
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            true,
                            "",
                            yourMessage = "${response.message}",
                            yourTitle = "",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }

                    else -> {}
                }
            })
    }
    private fun showLeaveSuccessMessage(statusMessage: Any?) {
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
                findNavController().navigate(R.id.action_leavedetailFragment_to_approvalAndNews)

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
        binding.approve.setOnClickListener {
            callApiForApproveNotification()
        }
        binding.reject.setOnClickListener {
            callApiForRejectNotification()
        }
    }

    private fun callApiForRejectNotification() {
        status="R"
        var comments = "---"
        var approverejectRequest = AcceptRejectRequestModel(
            noticationIDs = notificationId.toString(),
            comment = comments,
            status = status,
        )
        showRequestConfirmationDialog("Reject",approverejectRequest,0,"")
    }

    private fun callApiForApproveNotification() {
        status="A"
        var comments = "---"
        var approverejectRequest = AcceptRejectRequestModel(
            noticationIDs = notificationId.toString(),
            comment = comments,
            status = status,
        )
        showRequestConfirmationDialog("Accept",approverejectRequest,0,"")
    }

    val navArg: LeaveDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leave_details, container, false)
        val view = binding.root

        sessionmanagement = SessionManagement(requireContext())
        val response = navArg.todayModel as TodayModel
        message = response.message.toString()
        employeeID = sessionmanagement!!.getEmpId().toInt()
        notificationId = response.notificationID!!
        attachment = response.attachment.toString()
        val urlList = attachment.split(";")
        val notificationText = message
        val substringToCheck = "Please review"
        if(substringToCheck in notificationText)
        {
            val beforeSubstring = notificationText.substringBefore(substringToCheck)
            val afterSubstring = notificationText.substringAfter(substringToCheck)
            val finaltrimmedafterSubstring = afterSubstring.trimStart()
            val formattedString = "$beforeSubstring\n$finaltrimmedafterSubstring"
            val finalformattedstring = formattedString.replace(": \"", "")
            binding.attendance.text = finalformattedstring
        }
//        else{
//            binding.attendance.text = message
//        }

        val adapter = LeaveDetailsAdapter(requireContext(),requireActivity() ,urlList)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      //  val scrollListener = CustomScrollListener(layoutManager, binding.rvdetails)
      //  binding.rvdetails.addOnScrollListener(scrollListener)

        binding.rvdetails.layoutManager = layoutManager
        binding.rvdetails.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvdetails)
        adapter!!.notifyDataSetChanged()


        // Inflate the layout for this fragment
        return view
    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }

    private fun showRequestConfirmationDialog(
        text: String,
        approveReq: AcceptRejectRequestModel,
        position: Int,
        s: String
    ) {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.logout_dialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        var message: TextView = logoutDialog.findViewById(R.id.txtSuccesss)
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val img: ImageView = logoutDialog.findViewById(R.id.imgPopIcons)
        //   val img:ImageView=logoutDialog.findViewById()
        img.setImageResource(R.drawable.request_popup_icon)

        message.text = "Are you sure you want to $text this request?"
        yes.setSafeOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getAcceptRejectNotification(approveReq)
            }
            logoutDialog.dismiss()
        }

        no.setSafeOnClickListener {
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

}
