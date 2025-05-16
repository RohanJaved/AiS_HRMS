package com.appinsnap.aishrm.ui.fragments.approval.approval

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentApproval3Binding
import com.appinsnap.aishrm.ui.fragments.history.adapters.NotificationStatusAdapter
import com.appinsnap.aishrm.ui.fragments.history.models.Body
import com.appinsnap.aishrm.ui.fragments.history.models.CancelRequestModel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.SwipeLeftToDeleteCallback
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Approval(val allnotificationstatuslist: List<Body>) :  GeneralDialogFragment(),NotificationStatusAdapter.onRequestClick, GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission {
    private var _binding: FragmentApproval3Binding? = null
    private var selectedposition:Int=-1
    private var approvelist:ArrayList<Body> = ArrayList()
    private var adapter:NotificationStatusAdapter?=null
    private var notificationId:Int=-1
    private var sessionmanagement: SessionManagement? = null
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_approval3, container, false)
        val view = binding.root
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        sessionmanagement = SessionManagement(requireContext())
        initrecyclerview()
       // swipeToCancel()

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        livedataObserver()
    }

    private fun livedataObserver() {

        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    showProgressDialog(it)
        })
        homeViewModel._getCancelRequest.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        approvelist.removeAt(selectedposition)
                        if(approvelist.isEmpty())
                        {
                            settingApprovalAdapter()
                            binding.txtapproval.visibility = View.VISIBLE
                        }
                        else{
                            settingApprovalAdapter()
                            binding.txtapproval.visibility = View.GONE
                        }
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.data?.body?.statusMessage}",
                            icon = R.drawable.tickicon
                        )

                    }
                    is NetworkResult.Error -> {
                        settingApprovalAdapter()
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            this,
                            yourTitle = "",
                            yourMessage = "${response.message}",
                            icon = R.drawable.reject
                        )

                    }
                    is NetworkResult.Loading -> {

                    }
                }

            })
    }

    private fun swipeToCancel() {
        val swipeToDeleteCallbackapproval = object : SwipeLeftToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                selectedposition = position
               notificationId = approvelist.get(selectedposition).notificationID
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                     var notificationid = approvelist.get(position).notificationID
                        showRequestConfirmationDialog()
                    }
                }


            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbackapproval)
        itemTouchHelper.attachToRecyclerView(binding.approvalrecyclerview)
    }

    private fun initrecyclerview() {
        if(allnotificationstatuslist.isNotEmpty())
        {
            allnotificationstatuslist.forEach {
                if(it.status.toString().lowercase().contains("accept"))
                {
                    var empname=""
                    var empcheckIn=""
                    var empcheckOut=""
                    var category=""
                    var leaveType = ""
                    var comments = ""
                    var todate = ""
                    var fromdate = ""
                    var typeid=-1
                    if(it.name!=null)
                    {
                        empname = it.name
                    }
                    if(it.checkin!=null)
                    {
                        empcheckIn = it.checkin
                    }
                    if(it.checkout!=null)
                    {
                        empcheckOut = it.checkout
                    }
                    if(it.category!=null)
                    {
                        category = it.category
                    }
                    if(it.leaveType!=null)
                    {
                        leaveType = it.leaveType
                    }
                    if(it.comments!=null)
                    {
                        comments = it.comments
                    }
                    if(it.typeid!=null)
                    {
                        typeid = it.typeid
                    }
                    if(it.toDate!=null)
                    {
                        todate = it.toDate
                    }
                    if(it.fromDate!=null)
                    {
                        fromdate = it.fromDate
                    }
                    approvelist.add(Body(it.employeeID,fromdate,it.isEnabled,it.message,it.notificationDateTime,it.notificationID,it.status,it.title,todate,it.type,empname,empcheckIn,empcheckOut,category,leaveType,comments,typeid))
                }
            }
            if(approvelist.isNotEmpty()) {
                binding.approvalrecyclerview.show()
                binding.txtapproval.hide()
                adapter = NotificationStatusAdapter(requireContext(), approvelist,this)
                binding.approvalrecyclerview.adapter = adapter
            }
            else{
                binding.approvalrecyclerview.hide()
                binding.txtapproval.show()
            }
        }
        else{
            binding.approvalrecyclerview.hide()
            binding.txtapproval.show()
        }
    }
    private fun showRequestConfirmationDialog() {
        val logoutDialog = Dialog(requireContext())
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.logout_dialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        var message: TextView = logoutDialog.findViewById(R.id.txtSuccess)
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val img: ImageView = logoutDialog.findViewById(R.id.imgPopIcons)
        img.setImageResource(R.drawable.request_popup_icon)

        message.text = "Are you sure you want to cancel this request?"
        yes.setSafeOnClickListener {
            logoutDialog.dismiss()
            callCancelRequestApi()

        }


        no.setSafeOnClickListener {
          settingApprovalAdapter()
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }
    private fun callCancelRequestApi() {
        var empid = sessionmanagement?.getEmpId()
        var request = CancelRequestModel(employeID = empid!!.toInt(), notificationID = notificationId)
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.cancelRequestResponse(request)
        }

    }
    fun settingApprovalAdapter()
    {
        val adapter = NotificationStatusAdapter(requireContext(), approvelist,this)
        binding.approvalrecyclerview.adapter = adapter
    }



    override fun onRequestItemClick(position: Int,notificationid:String) {
        selectedposition = position
      notificationId = approvelist.get(selectedposition).notificationID
        showRequestConfirmationDialog()
    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }


}