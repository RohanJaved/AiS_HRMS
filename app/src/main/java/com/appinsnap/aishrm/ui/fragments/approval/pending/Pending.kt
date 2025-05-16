package com.appinsnap.aishrm.ui.fragments.approval.pending

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
import com.appinsnap.aishrm.databinding.FragmentPendingBinding
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
class Pending(val allnotificationstatuslist: List<Body>) : GeneralDialogFragment(),NotificationStatusAdapter.onRequestClick,GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission  {
    private var _binding: FragmentPendingBinding? = null
    private var selectedposition:Int=-1
    private var notificationId:Int=-1
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private var adapter:NotificationStatusAdapter?=null
    private var sessionmanagement: SessionManagement? = null
    private var pendinglist: ArrayList<Body> = ArrayList()
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

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending, container, false)
        val view = binding.root
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        sessionmanagement = SessionManagement(requireContext())
        initRecyclerView()
        swipeToCancel()
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
                        pendinglist.removeAt(selectedposition)
                        if(pendinglist.isEmpty())
                        {
                            binding.pendingrecyclerview.visibility =View.GONE
                            binding.txtpending.visibility = View.VISIBLE
                        }
                        else{
                            binding.pendingrecyclerview.visibility =View.VISIBLE
                            settingPendingAdapter()
                            binding.txtpending.visibility = View.GONE
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
                        settingPendingAdapter()
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
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

    private fun initRecyclerView() {
        if (allnotificationstatuslist.isNotEmpty()) {
            allnotificationstatuslist.forEach {
                if (it.status.toString().lowercase().equals("pending")||it.status.toString().lowercase().equals("null")) {
                    var empname=""
                    var empcheckIn=""
                    var empcheckOut=""
                    var category=""
                    var leaveType = ""
                    var comments = ""
                    var fromdate=""
                    var todate=""
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
                    if(it.fromDate!=null)
                    {
                        fromdate = it.fromDate
                    }
                    if(it.toDate!=null)
                    {
                        todate = it.toDate
                    }
                    pendinglist.add(Body(it.employeeID,fromdate,it.isEnabled,it.message,it.notificationDateTime,it.notificationID,it.status,it.title, todate,it.type,empname,empcheckIn,empcheckOut,category,leaveType,comments,typeid))

                }
            }

                if (pendinglist.isNotEmpty()) {
                    binding.pendingrecyclerview.show()
                    binding.txtpending.hide()
                     adapter = NotificationStatusAdapter(requireContext(), pendinglist,this)
                    binding.pendingrecyclerview.adapter = adapter
                }
            else{
                binding.pendingrecyclerview.hide()
                    binding.txtpending.show()
                    binding.pendingrecyclerview
                }
            }
        else{
            binding.pendingrecyclerview.hide()

        }

        }

    override fun onRequestItemClick(position: Int, notificationid: String) {
        selectedposition = position
        showRequestConfirmationDialog()
    }
    private fun swipeToCancel() {
        val swipeToDeleteCallbackpending = object : SwipeLeftToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                selectedposition = position
            notificationId = pendinglist.get(selectedposition).notificationID
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        var notificationid = pendinglist.get(position).notificationID
                        showRequestConfirmationDialog()
//                        Toast.makeText(requireContext(), "$approverequest", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbackpending)
        itemTouchHelper.attachToRecyclerView(binding.pendingrecyclerview)
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
        var message: TextView = logoutDialog.findViewById(R.id.txtSuccesss)
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val img: ImageView = logoutDialog.findViewById(R.id.imgPopIcons)
        img.setImageResource(R.drawable.request_popup_icon)

        message.text = "Are you sure you want to cancel this request?"
        yes.setSafeOnClickListener {
            logoutDialog.dismiss()
         notificationId = pendinglist.get(selectedposition).notificationID
            callCancelRequestApi()
        }

        no.setSafeOnClickListener {
          settingPendingAdapter()
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
    override fun onclick(value: Boolean) {
    }
    fun settingPendingAdapter()
    {
        val adapter = NotificationStatusAdapter(requireContext(), pendinglist,this)
        binding.pendingrecyclerview.adapter = adapter
    }

    override fun onLocationPermission(locationvalue: Boolean) {
    }

}