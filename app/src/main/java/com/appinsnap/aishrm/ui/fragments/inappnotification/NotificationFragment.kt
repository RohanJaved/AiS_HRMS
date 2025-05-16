package com.appinsnap.aishrm.ui.fragments.inappnotification

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.databinding.FragmentNotificationBinding
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationRequest
import com.appinsnap.aishrm.ui.fragments.home.adapter.NotificationAdapter
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.Body
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.NotificationViewRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.viewmodel.NotificationViewModel
import com.appinsnap.aishrm.util.*
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*



//@AndroidEntryPoint
//class NotificationFragment : GeneralDialogFragment(), GeneralDialogFragment.onClick, GeneralDialogFragment.locationPermission,NotificationAdapter.BtnClickListener{
//
//var notificationlist:MutableList<Body> = mutableListOf()
//    var notificationlistposition:Int=0
//    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
//    val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
//    val f1: SimpleDateFormat =
//        SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)
//    val f2: SimpleDateFormat = SimpleDateFormat("hh:mm aa")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//    var adapter: NotificationAdapter? = null
//
//    private var _binding: FragmentNotificationBinding? = null
//    private val binding get() = _binding!!
//    private val notificationViewModel: NotificationViewModel by lazy {
//        ViewModelProvider(
//            this,
//        ).get(NotificationViewModel::class.java)
//    }
//    private val authenticationViewModel: AuthenticationViewModel by viewModels()
//    private var sessionmanagement: SessionManagement? = null
//
//    private lateinit var progressBarDialogRegister: ProgressBarDialog
//  //  private val sessionManager = SessionManager()
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//
//        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
//        val view = binding.root
//        progressBarDialogRegister = ProgressBarDialog(requireContext())
//
//        CoroutineScope(Dispatchers.Main).launch {
//            sessionmanagement?.let { GetNotificationRequest(email = it.getEmail()) }
//                ?.let { notificationViewModel.getNotificationData(it) }
//        }
//       // sessionManager.updateSession()
//
////        if (sessionManager.isSessionExpired()) {
////            // Perform logout or session expiration logic
//////            showSessionExpiredDialog()
////
////            MainActivity.getInstance().showSessionExpireDialog()
////        }
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        oberver(view)
////        initUserStatusBottomSheet()
//    }
//
//    private  fun oberver(view: View) {
//        notificationViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            showProgressDialog(isShow = it)
//
//        })
//
//
//        notificationViewModel._inappresponse.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer { response ->
//                when (response) {
//                    is NetworkResult.Success -> {
//                        requireActivity().findViewById<TextView>(R.id.notificationcount).hide()
//                        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView2)
//                        adapter = response.data?.let {
////                            it.body.forEach { notificationlist ->
////                                try {
////                                    var dateandtime =
////                                        notificationlist.notificationDateTime.split("T")
////                                    var date = dateandtime[0]
////
////                                    val dateconvert = LocalDate.parse(date, inputFormat)
////                                    val formattedDate = outputFormat.format(dateconvert)
////                                    var time = dateandtime[1]
////                                    var parsetime = f1.parse(time)
////                                    var formattime = f2.format(parsetime)
////                                    var filtereddatetime = "${formattedDate}" + ","+" " + "${formattime}"
////                                    notificationlist.notificationDateTime = filtereddatetime
////                                    val string = ""
////                                } catch (e: Exception) {
////                                    LoggerGenratter.getInstance().printLog("NOTIFICATION FRAGMENT",  e.toString())
////                                }
////                            }
////                            notificationlist= it.body as MutableList<Body>
////
////                        val list = it.body
////                            val string = ""
////                            NotificationAdapter(
////                                view.context,
////                                notificationlist,
////                                object: NotificationAdapter.BtnClickListener {
////
////                                    override suspend fun onBtnClick(
////                                        type: Boolean,
////                                        approvalRequest: ApprovalRequest,
////                                        position: Int
////                                    ) {
////                                        showRequestConfirmationDialog(type,approvalRequest,position)
////                                   //     notificationViewModel.approveRequest(approvalRequest)
////                                    }
////                                }
////                            )
//
//                        }
//                        recyclerView.adapter = adapter
//                        callNotificationCountUpdateApi()
//                    }
//                    is NetworkResult.Error -> {
//
//                        showGeneralDialogFragment(
//                            requireContext(),
//                            true,
//                            this,
//                            true,
//                            "yes",
//                            yourMessage = "${response.message}",
//                            yourTitle = "",
//                            location = this,
//                            icon = R.drawable.erroricon
//                        )
//
//                    }
//                    is NetworkResult.Loading -> {
//
//                    }
//                }
//
//            })
//
//        notificationViewModel._approvalResponse.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer { response ->
//                when (response) {
//                    is NetworkResult.Success -> {
//                        removenotificationfromlist()
//                        showGeneralDialogFragment(
//                            requireContext(),
//                            false,
//                            this,
//                            true,
//                            "",
//                            yourMessage = "${response.data?.statusMessage}",
//                            yourTitle = "",
//                            location = this,
//                            icon = R.drawable.tickicon
//                        )
//
//                    }
//                    is NetworkResult.Error -> {
//
//                        showGeneralDialogFragment(
//                            requireContext(),
//                            true,
//                            this,
//                            true,
//                            "yes",
//                            yourMessage = "${response.message}",
//                            yourTitle = "",
//                            location = this,
//                            icon = R.drawable.erroricon
//                        )
//
//                    }
//                    is NetworkResult.Loading -> {
//
//                    }
//                }
//
//            })
//        notificationViewModel._notificationcountupdate.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer { response ->
//                when (response) {
//                    is NetworkResult.Success -> {
//                       MainActivity.callNotificationCountUpdateApi(requireContext(), authenticationViewModel =authenticationViewModel )
//
//                    }
//                    is NetworkResult.Error -> {
//                        LoggerGenratter.getInstance().printLog("notificationcountupdateapi","something went wrong")
//                    }
//                    is NetworkResult.Loading -> {
//
//                    }
//                }
//
//            })
//    }
//
//    private fun removenotificationfromlist() {
//        notificationlist.removeAt(notificationlistposition)
//        adapter?.filterList(notificationlist)
//    }
//
//    private fun callNotificationCountUpdateApi() {
//        var email = sessionmanagement?.getEmail()
//        val notificationupdatecountrequest = NotificationViewRequest(email = email.toString())
//        CoroutineScope(Dispatchers.Main).launch {
//       notificationViewModel.getUpdatedNotificationCount(notificationupdatecountrequest)
//        }
//    }
//
//    private suspend fun showRequestConfirmationDialog(
//        type: Boolean,
//        approveReq: ApprovalRequest,
//        position: Int
//    ) {
//        notificationlistposition = position
//        val logoutDialog = Dialog(requireContext())
//        logoutDialog.setCancelable(true)
//        logoutDialog.setCanceledOnTouchOutside(true)
//        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        logoutDialog.setContentView(R.layout.logout_dialog)
//        logoutDialog.window?.setLayout(
//            ActionBar.LayoutParams.MATCH_PARENT,
//            ActionBar.LayoutParams.MATCH_PARENT
//        )
//
//
//
//        var message: TextView = logoutDialog.findViewById(R.id.txtSuccess)
//        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
//        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
//        //   val img:ImageView=logoutDialog.findViewById()
//        if(type)
//        {
//           message.text="Are you sure you want to accept this request?"
//        }
//       else
//        {
//            message.text="Are you sure you want to reject this request?"
//
//        }
//        yes.setSafeOnClickListener {
//
//
//
//
//            yes.setSafeOnClickListener {
//                CoroutineScope(Dispatchers.Main).launch {
//                    notificationViewModel.approveRequest(approveReq)
//                }
//
//
//
//                logoutDialog.dismiss()
//            }
//
//        }
//
//        no.setSafeOnClickListener {
//
//            logoutDialog.dismiss()
//        }
//
//        logoutDialog.show()
//
//
//    }
//
//    private fun initUserStatusBottomSheet() {
//        val userstatusDialog = Dialog(requireContext(), R.style.DialogStyle)
//        userstatusDialog.setCancelable(true)
//        userstatusDialog.setCanceledOnTouchOutside(true)
//        userstatusDialog.setContentView(R.layout.birthday_card)
//        userstatusDialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
//
//        userstatusDialog.show()
//
//    }
//
//    override fun onclick(value: Boolean) {
//        activity?.onBackPressed()
//    }
//
//    override fun onLocationPermission(locationvalue: Boolean) {
//
//
//    }
//
//    override suspend fun onBtnClick(
//        type: Boolean,
//        approvalRequest: ApprovalRequest,
//        position: Int
//    ) {
//
//    }
//
//
//}