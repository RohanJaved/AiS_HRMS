package com.appinsnap.aishrm.ui.fragments.approvalnews

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentApprovalAndNewsBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.MonthNewsAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.MonthlyApprovalStatusAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.ThisWeekNewsAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.TodayApprovalStausAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.TodayNewsAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.adapters.WeekApprovalStatusAdapter
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectRequestModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.Body
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.BodyXX
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.DayNotification
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.GetNotificationRequest
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.MonthNotification
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.ThisDay
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.ThisMonth
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.ThisWeek
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.WeekNotification
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.ui.fragments.inappnotification.viewmodel.NotificationViewModel
import com.appinsnap.aishrm.ui.fragments.leavesDetails.models.TodayModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.SwipeToDeleteCallback
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.Exception

@AndroidEntryPoint
class ApprovalAndNews : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission, WeekApprovalStatusAdapter.BtnClickListener,
    MonthlyApprovalStatusAdapter.MonthlyBtnClickListener,
    TodayApprovalStausAdapter.TodayBtnClickListener, TodayApprovalStausAdapter.todayonClick,
    WeekApprovalStatusAdapter.weekonClick, MonthlyApprovalStatusAdapter.monthlyonClick {
    private var _binding: FragmentApprovalAndNewsBinding? = null
    lateinit var currentDateCto: org.threeten.bp.LocalDate
    var myNotificationIdList = ArrayList<Int>()
    private var employeeID: Int = 0
    private var notificationID: Int = 0
    private var itemClickedPosition: Int = 0
    private var nature: String = ""
    private var comment: String = ""
    private var status: String = ""
    private var todayadapter: TodayApprovalStausAdapter? = null
    private var thisweekadapter: WeekApprovalStatusAdapter? = null
    private var showRadioButton: Boolean = false
    private var todaynewslist: List<ThisDay> = listOf()
    private var thisweeknewslist: List<ThisWeek> = listOf()
    private var thiseeekapprovallist: ArrayList<WeekNotification> = ArrayList()
    private var todayapprovallist: ArrayList<DayNotification> = ArrayList()
    private var thismonthapprovallist: ArrayList<MonthNotification> = ArrayList()
    var monthlyAdapter: MonthlyApprovalStatusAdapter? = null
    private var thismonthnewslist: List<ThisMonth> = listOf()
    val inputFormat = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormat =
        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    val f1: SimpleDateFormat =
        SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)
    val f2: SimpleDateFormat = SimpleDateFormat("hh:mm aa")
    var isMChecked = false
    var isWChecked = false
    var isTDChecked = false
    private var sessionmanagement: SessionManagement? = null
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private val notificationViewModel: NotificationViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(NotificationViewModel::class.java)
    }
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_approval_and_news, container, false)
        val view = binding.root
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        currentDateCto = getCurentDate()
        sessionmanagement = SessionManagement(context)
        var viewid = sessionmanagement?.getViewId()
        var isPMO = sessionmanagement?.getPMOStatus()
        if (viewid == 3 && isPMO == true){
            binding.mcvApprovals.show()
            binding.mcvNews.show()
            if (thiseeekapprovallist.isNotEmpty()) {
                thiseeekapprovallist.clear()
            }
            if (todayapprovallist.isNotEmpty()) {
                thiseeekapprovallist.clear()
            }
            if (thismonthapprovallist.isNotEmpty()) {
                thismonthapprovallist.clear()
            }
            callInAppNotificationApi()
            //  attatchingSwipeListenerToAdapter()
            // todayapprovallayoutvisibility()
            changingapprovalnewslayout()
            binding.approvalstatuslayout.show()
            requireActivity().findViewById<ConstraintLayout>(R.id.toolbarlayout)
                .findViewById<TextView>(R.id.tvToolbarTitle).text = "Approvals & News"

        }
        else if (viewid ==4 || viewid ==2 || viewid==1){
            binding.mcvApprovals.show()
            binding.mcvNews.show()
            if (thiseeekapprovallist.isNotEmpty()) {
                thiseeekapprovallist.clear()
            }
            if (todayapprovallist.isNotEmpty()) {
                thiseeekapprovallist.clear()
            }
            if (thismonthapprovallist.isNotEmpty()) {
                thismonthapprovallist.clear()
            }
            callInAppNotificationApi()
            //  attatchingSwipeListenerToAdapter()
            // todayapprovallayoutvisibility()
            changingapprovalnewslayout()
            binding.approvalstatuslayout.show()
            requireActivity().findViewById<ConstraintLayout>(R.id.toolbarlayout)
                .findViewById<TextView>(R.id.tvToolbarTitle).text = "Approvals & News"
        }
        else {
            binding.mcvApprovals.hide()
            binding.mcvNews.hide()
            binding.newslayout.show()
            binding.approvalstatuslayout.hide()
            callNewsApi()
            changingNewsButtonLayout()
            requireActivity().findViewById<ConstraintLayout>(R.id.toolbarlayout).findViewById<TextView>(R.id.tvToolbarTitle).text = "News"
        }

        return view
    }

//    private fun attatchingSwipeListenerToAdapter() {
//        val swipeToDeleteCallbacktoday = object : SwipeToDeleteCallback() {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                nature = "Today"
//                itemClickedPosition = position
//                employeeID = todayapprovallist.get(position).employeeID
//                notificationID = todayapprovallist.get(position).notificationID
//
//                when (direction) {
//                    ItemTouchHelper.LEFT -> {
//                        status = "R"
//                        val approverequest = AcceptRejectRequestModel(
//                            noticationIDs = notificationID.toString(),
//                            comment = comment,
//                            status = status
//                        )
//                        showRequestConfirmationDialog("Reject", approverequest, position, "today")
////                        Toast.makeText(requireContext(), "$approverequest", Toast.LENGTH_SHORT).show()
//                    }
//                    ItemTouchHelper.RIGHT -> {
//                        status = "A"
//                        val approverequest = AcceptRejectRequestModel(
//                            noticationIDs = notificationID.toString(),
//                            comment = comment,
//                            status = status
//                        )
//                        showRequestConfirmationDialog("Accept", approverequest, position, "today")
//                    }
//                }
//            }
//        }
//
//        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbacktoday)
//        itemTouchHelper.attachToRecyclerView(binding.todayrecyclerview)
//    }

    private fun todayapprovallayoutvisibility() {
        if (todayapprovallist.isNotEmpty()) {
            todayadapter = TodayApprovalStausAdapter(
                isTDChecked,
                requireContext(),
                todayapprovallist,
                this,
                this,
                showRadioButton,
                false
            )
            binding.todayrecyclerview.adapter = todayadapter
            binding.todayradiobutton.setOnClickListener {

                isTDChecked = !isTDChecked

                todayadapter = TodayApprovalStausAdapter(
                    isTDChecked,
                    requireContext(),
                    todayapprovallist,
                    this,
                    this,
                    showRadioButton,
                    false
                )
                binding.todayrecyclerview.adapter = todayadapter
                binding.todayradiobutton.isChecked = isTDChecked


            }

            val swipeToDeleteCallbacktoday = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    nature = "Today"
                    itemClickedPosition = position
                    employeeID = todayapprovallist.get(position).employeeID
                    notificationID = todayapprovallist.get(position).notificationID

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            status = "R"
                            val approverequest = AcceptRejectRequestModel(
                                noticationIDs = notificationID.toString(),
                                comment = comment,
                                status = status
                            )
                            showRequestConfirmationDialog(
                                "Reject",
                                approverequest,
                                position,
                                "today"
                            )
                        }
                        ItemTouchHelper.RIGHT -> {
                            status = "A"
                            val approverequest = AcceptRejectRequestModel(
                                noticationIDs = notificationID.toString(),
                                comment = comment,
                                status = status
                            )
                            showRequestConfirmationDialog(
                                "Accept",
                                approverequest,
                                position,
                                "today"
                            )
                        }
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbacktoday)
            itemTouchHelper.attachToRecyclerView(binding.todayrecyclerview)
        }
    }

    private fun changingapprovalnewslayout() {
        binding.mcvApprovals.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.mcvApprovals.setStrokeColor(resources.getColor(R.color.yellow))
        binding.mcvApprovals.strokeWidth = 1
        binding.txtapprovals.setTextColor(resources.getColor(R.color.yellow))
        binding.mcvNews.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.mcvNews.strokeWidth = 0
    }

    private fun callInAppNotificationApi() {
        CoroutineScope(Dispatchers.Main).launch {
            sessionmanagement?.let { GetNotificationRequest(email = it.getEmail()) }
                ?.let { notificationViewModel.getNotificationData(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        livedataObserver()
    }

    private fun livedataObserver() {
        homeViewModel._newsresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {

                        settingupnewsdata(response.data?.body)
                    }
                    is NetworkResult.Error -> {

                    }
                    is NetworkResult.Loading -> {

                    }
                }

            })

        homeViewModel._accceptrejectresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        callInAppNotificationApi()
                        nature = ""
                        itemClickedPosition = 0
                        showGeneralDialogFragment(
                            requireContext(),
                            false,
                            this,
                            true,
                            "",
                            yourMessage = "${response.data?.statusMessage}",
                            yourTitle = "",
                            location = this,
                            icon = R.drawable.tickicon
                        )
                    }

                    is NetworkResult.Error -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            yourMessage = "${response.message}",
                            yourTitle = "",
                            location = this,
                            icon = R.drawable.reject
                        )
                    }
                    is NetworkResult.Loading -> {

                    }
                }
            })
        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(isShow = it)

        })


        notificationViewModel._inappresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        requireActivity().findViewById<TextView>(R.id.notificationcount).hide()
                        hidingRadioButtonsFromList()
                        setNotificationResponse(response.data?.body)
                        myNotificationIdList.clear()
                        thisWeekSelectedItems.clear()
                        thisMonthSelectedItems.clear()
                        todaySelectedItems.clear()
                    }
                    is NetworkResult.Error -> {
                        showGeneralDialogFragment(
                            requireContext(),
                            true,
                            this,
                            true,
                            "yes",
                            yourMessage = "${response.message}",
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

    private fun hidingRadioButtonsFromList() {
        binding.todayradiobutton.visibility = View.GONE
        binding.thisweekradiobutton.visibility = View.GONE
        binding.thismonthradiobutton.visibility = View.GONE
        binding.acceptall.visibility = View.GONE
        if (binding.thisweekradiobutton.isChecked) {
            binding.thisweekradiobutton.isChecked = false
        }
        if (binding.thismonthradiobutton.isChecked) {
            binding.thismonthradiobutton.isChecked = false
        }
        if (binding.todayradiobutton.isChecked) {
            binding.todayradiobutton.isChecked = false
        }
        binding.rejecttall.visibility = View.GONE
        showRadioButton = false
        isWChecked = false
        isMChecked = false
        isTDChecked = false
        thisweekadapter = WeekApprovalStatusAdapter(
            isWChecked,
            requireContext(),
            thiseeekapprovallist,
            this,
            this,
            false,
            showRadioButton
        )
        binding.thisweekrecyclerview.adapter = thisweekadapter
        monthlyAdapter = MonthlyApprovalStatusAdapter(
            isMChecked,
            requireContext(),
            thismonthapprovallist,
            this,
            false,
            showRadioButton,
            this
        )
        binding.thismonthrecyclerview.adapter = monthlyAdapter
        todayadapter = TodayApprovalStausAdapter(
            isTDChecked,
            requireContext(),
            todayapprovallist,
            this,
            this,
            showRadioButton,
            false
        )
        binding.todayrecyclerview.adapter = todayadapter

    }

    private fun setNotificationResponse(body: BodyXX?) {
        if (body != null) {

            body.weekNotification.forEach {
                var dateandtime =
                    it.notificationDateTime.split("T")
                var date = dateandtime[0]
                if(it.checkin!=null)
                {
                    var checkinTime = it.checkin
                    var convertedcheckInTime = f1.parse(checkinTime)
                    var finalconvertedcheckinTime = f2.format(convertedcheckInTime)
                    it.checkin = finalconvertedcheckinTime
                }
                if(it.checkout!=null)
                {
                    var checkoutTime = it.checkout
                    var convertedcheckoutTime = f1.parse(checkoutTime)
                    var finalconvertedcheckoutTime = f2.format(convertedcheckoutTime)
                    it.checkout = finalconvertedcheckoutTime

                }
                if(it.fromDate!=null)
                {
                    var date = java.time.LocalDate.parse(date, inputFormat)
                    var converteddate = outputFormat.format(date)
                    it.fromDate = converteddate
                }
                val dateconvert = java.time.LocalDate.parse(date, inputFormat)
                val formattedDate = outputFormat.format(dateconvert)
                var time = dateandtime[1]
                var parsetime = f1.parse(time)
                var formattime = f2.format(parsetime)
                var filtereddatetime = "${formattedDate}" + "," + " " + "${formattime}"
                it.notificationDateTime = filtereddatetime
            }
            thiseeekapprovallist = body.weekNotification as ArrayList

            body.monthNotification.forEach {
                var dateandtime =
                    it.notificationDateTime.split("T")
                var date = dateandtime[0]
                if(it.checkin!=null)
                {
                    var checkinTime = it.checkin
                    var convertedcheckInTime = f1.parse(checkinTime)
                    var finalconvertedcheckinTime = f2.format(convertedcheckInTime)
                    it.checkin = finalconvertedcheckinTime
                }
                if(it.checkout!=null)
                {
                    var checkoutTime = it.checkout
                    var convertedcheckoutTime = f1.parse(checkoutTime)
                    var finalconvertedcheckoutTime = f2.format(convertedcheckoutTime)
                    it.checkout = finalconvertedcheckoutTime

                }
                if(it.fromDate!=null)
                {
                 var date = java.time.LocalDate.parse(it.fromDate, inputFormat)
                    var formatteddate  = outputFormat.format(date)
                    it.fromDate = formatteddate
                }

                val dateconvert = java.time.LocalDate.parse(date, inputFormat)
                val formattedDate = outputFormat.format(dateconvert)
                var time = dateandtime[1]
                var parsetime = f1.parse(time)
                var formattime = f2.format(parsetime)
                var filtereddatetime = "${formattedDate}" + "," + " " + "${formattime}"
                it.notificationDateTime = filtereddatetime
            }
            thismonthapprovallist = body.monthNotification as ArrayList

            body.dayNotification.forEach {

                var dateandtime =
                    it.notificationDateTime.split("T")
                var date = dateandtime[0]
                if(it.checkin!=null)
                {
                    var checkinTime = it.checkin
                    var convertedcheckInTime = f1.parse(checkinTime)
                    var finalconvertedcheckinTime = f2.format(convertedcheckInTime)
                    it.checkin = finalconvertedcheckinTime
                }
                if(it.checkout!=null)
                {
                    var checkoutTime = it.checkout
                    var convertedcheckoutTime = f1.parse(checkoutTime)
                    var finalconvertedcheckoutTime = f2.format(convertedcheckoutTime)
                    it.checkout = finalconvertedcheckoutTime

                }
                if(it.fromDate!=null)
                {
                    var splitdate = it.fromDate.split("T").get(0)
                    var date = java.time.LocalDate.parse(splitdate, inputFormat)
                    var formatteddate = outputFormat.format(date)
                    it.fromDate = formatteddate
                }

                val dateconvert = java.time.LocalDate.parse(date, inputFormat)
                val formattedDate = outputFormat.format(dateconvert)
                var time = dateandtime[1]
                var parsetime = f1.parse(time)
                var formattime = f2.format(parsetime)
                var filtereddatetime = "${formattedDate}" + "," + " " + "${formattime}"
                it.notificationDateTime = filtereddatetime

            }
            todayapprovallist = body.dayNotification as ArrayList

            todayadapter = TodayApprovalStausAdapter(
                isTDChecked,
                requireContext(),
                todayapprovallist,
                this,
                this,
                showRadioButton,
                false
            )
            binding.todayrecyclerview.adapter = todayadapter
            todayapprovallayoutvisibility()
            thisweekadapter = WeekApprovalStatusAdapter(
                isWChecked,
                requireContext(),
                thiseeekapprovallist,
                this,
                this,
                false,
                showRadioButton
            )

            binding.thisweekrecyclerview.adapter = thisweekadapter
            monthlyAdapter = MonthlyApprovalStatusAdapter(
                isMChecked,
                requireContext(),
                thismonthapprovallist,
                this,
                false,
                showRadioButton,
                this
            )
            binding.thismonthrecyclerview.adapter = monthlyAdapter
        }
    }

    private fun settingupnewsdata(body: Body?) {
        if (body != null) {
            if(body.thisDay!=null)
            {
                todaynewslist = body.thisDay
                settodaynewsadapter()
            }
            if(body.thisMonth!=null)
            {
                thismonthnewslist = body.thisMonth
            }
            if(body.thisWeek!=null)
            {
                thisweeknewslist = body.thisWeek
            }
        }
    }

    private fun settodaynewsadapter() {
        val adapter = TodayNewsAdapter(requireContext(), todaynewslist)
        binding.todaynewsrecyclerview.adapter = adapter
    }

    fun clearNatureAndCount() {
        nature = ""
        itemClickedPosition = 0
    }

    private var todaySelectedItems = ArrayList<Int>()
    private var thisWeekSelectedItems = ArrayList<Int>()
    private var thisMonthSelectedItems = ArrayList<Int>()

    private fun onClickListeners() {
        binding.acceptall.setSafeOnClickListener {
            myNotificationIdList.addAll(thisWeekSelectedItems)
            myNotificationIdList.addAll(thisMonthSelectedItems)
            myNotificationIdList.addAll(todaySelectedItems)
            if (myNotificationIdList.isEmpty()) {
                showGeneralDialogFragment(
                    requireContext(),
                    true,
                    this,
                    true,
                    "yes",
                    this,
                    yourTitle = "",
                    yourMessage = "Select any notification to accept",
                    icon = R.drawable.reject
                )
            } else {
                for (i in myNotificationIdList) {
                    notificationIDs.put(i, i)
                }

                val acceptrejectrequest = AcceptRejectRequestModel(
                    noticationIDs = MyHelperClass.getInstance()
                        .convertHashMapToList(notificationIDs)
                        .joinToString(separator = ","), comment = "", status = "A"
                )


                showRequestConfirmationDialog("Accept", acceptrejectrequest, 0, "")


                LoggerGenratter.getInstance().printLog(
                    "MyFinal List",
                    MyHelperClass.getInstance().convertHashMapToList(notificationIDs)
                        .joinToString(separator = ",")
                )
            }
        }
        binding.rejecttall.setSafeOnClickListener {
            if (myNotificationIdList.isNotEmpty()) {
                myNotificationIdList.clear()
            } else {
                myNotificationIdList.addAll(thisWeekSelectedItems)
                myNotificationIdList.addAll(thisMonthSelectedItems)
                myNotificationIdList.addAll(todaySelectedItems)
            }
            if (myNotificationIdList.isEmpty()) {
                showGeneralDialogFragment(
                    requireContext(),
                    true,
                    this,
                    true,
                    "yes",
                    this,
                    yourTitle = "",
                    yourMessage = "Select any notification to reject",
                    icon = R.drawable.reject
                )
            } else {

                for (i in myNotificationIdList) {
                    notificationIDs.put(i, i)
                }

                val acceptrejectrequest = AcceptRejectRequestModel(
                    noticationIDs = MyHelperClass.getInstance()
                        .convertHashMapToList(notificationIDs)
                        .joinToString(separator = ","), comment = "", status = "R"
                )


                showRequestConfirmationDialog("Reject", acceptrejectrequest, 0, "")
            }
        }


        binding.mcvApprovals.setSafeOnClickListener {
            callInAppNotificationApi()
            changingApprovalButtonLayout()
            binding.newslayout.hide()
            binding.approvalstatuslayout.show()

        }
        binding.mcvNews.setSafeOnClickListener {
            callNewsApi()
            changingNewsButtonLayout()
            binding.newslayout.show()
            binding.approvalstatuslayout.hide()
        }
        binding.todaylayout.setSafeOnClickListener {
            checktodaylayoutvisibility()
            clearNatureAndCount()
            binding.downarrowthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthrecyclerview.hide()
            binding.downarrowthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweekrecyclerview.hide()
        }
        binding.thisweeklayout.setSafeOnClickListener {
            checkthisweeklayoutvisibility()
            clearNatureAndCount()
            binding.downarrowthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthrecyclerview.hide()
            binding.downarrowtoday.setImageResource(R.drawable.downarrow)
            binding.todayrecyclerview.hide()
        }
        binding.thismonthlayout.setSafeOnClickListener {
            checkthismonthlayoutvisibility()
            clearNatureAndCount()
            binding.downarrowtoday.setImageResource(R.drawable.downarrow)
            binding.todayrecyclerview.hide()
            binding.downarrowthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweekrecyclerview.hide()
        }
        binding.todaynewslayout.setOnClickListener {
            checktodaynewslayoutvisibility()
            binding.downarrownewsthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweeknewsrecyclerviewlayout.hide()
            binding.downarrownewsthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthnewsrecyclerviewlayout.hide()
        }
        binding.thisweeknewslayout.setOnClickListener {
            checkthisweeknewslayoutvisibility()
            binding.downarrownewstoday.setImageResource(R.drawable.downarrow)
            binding.todaynewsrecyclerviewlayout.hide()
            binding.downarrownewsthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthnewsrecyclerviewlayout.hide()
        }
        binding.thismonthnewslayout.setOnClickListener {
            checkthismonthnewslayoutvisibility()
            binding.downarrownewsthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweeknewsrecyclerviewlayout.hide()
            binding.downarrownewstoday.setImageResource(R.drawable.downarrow)
            binding.todaynewsrecyclerviewlayout.hide()
        }
    }

    private fun callNewsApi() {
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getNewsData()
        }
    }

    private fun checkthismonthnewslayoutvisibility() {
        if (thismonthnewslist.isNotEmpty()) {
            binding.thismonthnewsrecyclerview.show()
            val adapter = MonthNewsAdapter(requireContext(), thismonthnewslist)
            binding.thismonthnewsrecyclerview.adapter = adapter
        } else {
            binding.thismonthnewsrecyclerview.hide()
        }
        if (binding.thismonthnewsrecyclerviewlayout.visibility == View.VISIBLE) {
            binding.downarrownewsthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthnewsrecyclerviewlayout.hide()
        } else {
            binding.downarrownewsthismonth.setImageResource(R.drawable.uparrow)
            binding.thismonthnewsrecyclerviewlayout.show()
        }
    }

    private fun checkthisweeknewslayoutvisibility() {
        if (thisweeknewslist.isNotEmpty()) {
            binding.thisweeknewsrecyclerview.show()
            val adapter = ThisWeekNewsAdapter(requireContext(), thisweeknewslist)
            binding.thisweeknewsrecyclerview.adapter = adapter
        } else {
            binding.todaynewsrecyclerview.hide()
        }
        if (binding.thisweeknewsrecyclerviewlayout.visibility == View.VISIBLE) {
            binding.downarrownewsthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweeknewsrecyclerviewlayout.hide()
        } else {
            binding.downarrownewsthisweek.setImageResource(R.drawable.uparrow)
            binding.thisweeknewsrecyclerviewlayout.show()
        }

    }

    private fun checktodaynewslayoutvisibility() {
        if (todaynewslist.isNotEmpty()) {
            binding.todaynewsrecyclerview.show()
            val adapter = TodayNewsAdapter(requireContext(), todaynewslist)
            binding.todaynewsrecyclerview.adapter = adapter
        } else {
            binding.todaynewsrecyclerview.hide()
        }

        val adapter = TodayNewsAdapter(requireContext(), todaynewslist)
        binding.todaynewsrecyclerview.adapter = adapter

        if (binding.todaynewsrecyclerviewlayout.visibility == View.VISIBLE) {
            binding.downarrownewstoday.setImageResource(R.drawable.downarrow)
            binding.todaynewsrecyclerviewlayout.hide()
        } else {
            binding.downarrownewstoday.setImageResource(R.drawable.uparrow)
            binding.todaynewsrecyclerviewlayout.show()
        }
    }

    private fun checkthismonthlayoutvisibility() {
        if (binding.thismonthrecyclerview.visibility == View.VISIBLE) {
            binding.downarrowthismonth.setImageResource(R.drawable.downarrow)
            binding.thismonthrecyclerview.hide()
        } else {
            binding.thismonthrecyclerview.show()
            binding.downarrowthismonth.setImageResource(R.drawable.uparrow)

            val adapter =
                MonthlyApprovalStatusAdapter(
                    isMChecked,
                    requireContext(),
                    thismonthapprovallist,
                    this,
                    false,
                    showRadioButton,
                    this
                )
            binding.thismonthrecyclerview.adapter = adapter
            binding.thismonthradiobutton.setOnClickListener {

                isMChecked = !isMChecked

                val adapter = MonthlyApprovalStatusAdapter(
                    isMChecked,
                    requireContext(),
                    thismonthapprovallist,
                    this,
                    true,
                    showRadioButton,
                    this
                )
                binding.thismonthrecyclerview.adapter = adapter

                // Update the radio button state based on the isMChecked value
                binding.thismonthradiobutton.isChecked = isMChecked
            }


        }
        val swipeToDeleteCallbackthismonth = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                nature = "Monthly"
                itemClickedPosition = position
                employeeID = thismonthapprovallist.get(position).employeeID
                notificationID = thismonthapprovallist.get(position).notificationID

                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        status = "A"
                        val approverequest = AcceptRejectRequestModel(
                            noticationIDs = notificationID.toString(),
                            comment = comment,
                            status = status
                        )
                        showRequestConfirmationDialog(
                            "Accept",
                            approverequest,
                            position,
                            "thismonth"
                        )
                    }
                    ItemTouchHelper.LEFT -> {
                        status = "R"
                        val approverequest = AcceptRejectRequestModel(
                            noticationIDs = notificationID.toString(),
                            comment = comment,
                            status = status
                        )
                        showRequestConfirmationDialog(
                            "Reject",
                            approverequest,
                            position,
                            "thismonth"
                        )

                    }
                }


            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbackthismonth)
        itemTouchHelper.attachToRecyclerView(binding.thismonthrecyclerview)
    }

    private fun checkthisweeklayoutvisibility() {

        if (binding.thisweekrecyclerview.visibility == View.VISIBLE) {
            binding.downarrowthisweek.setImageResource(R.drawable.downarrow)
            binding.thisweekrecyclerview.hide()
        } else {
            binding.downarrowthisweek.setImageResource(R.drawable.uparrow)
            binding.thisweekrecyclerview.show()
            /* if (thiseeekapprovallist.isNotEmpty()) {
                 thisweekadapter = WeekApprovalStatusAdapter(isWChecked,
                     requireContext(),
                     thiseeekapprovallist,
                     this,
                     this,
                     false,
                     showRadioButton
                 )
                 binding.thisweekrecyclerview.adapter = thisweekadapter
             }*/


            thisweekadapter = WeekApprovalStatusAdapter(
                isWChecked,
                requireContext(),
                thiseeekapprovallist,
                this,
                this,
                false,
                showRadioButton
            )
            binding.thisweekrecyclerview.adapter = thisweekadapter
            binding.thisweekradiobutton.setOnClickListener {

                isWChecked = !isWChecked

                thisweekadapter = WeekApprovalStatusAdapter(
                    isWChecked,
                    requireContext(),
                    thiseeekapprovallist,
                    this,
                    this,
                    false,
                    showRadioButton
                )
                binding.thisweekrecyclerview.adapter = thisweekadapter

                // Update the radio button state based on the isMChecked value
                binding.thisweekradiobutton.isChecked = isWChecked
            }

        }
        val swipeToDeleteCallbackthisweek = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                nature = "week"
                itemClickedPosition = position
                employeeID = thiseeekapprovallist.get(position).employeeID
                notificationID = thiseeekapprovallist.get(position).notificationID

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        status = "R"
                        val approverequest = AcceptRejectRequestModel(
                            noticationIDs = notificationID.toString(),
                            comment = comment,
                            status = status
                        )
                        showRequestConfirmationDialog(
                            "Reject",
                            approverequest,
                            position,
                            "thisweek"
                        )
                    }
                    ItemTouchHelper.RIGHT -> {
                        status = "A"
                        val approverequest = AcceptRejectRequestModel(
                            noticationIDs = notificationID.toString(),
                            comment = comment,
                            status = status
                        )
                        showRequestConfirmationDialog(
                            "Accept",
                            approverequest,
                            position,
                            "thisweek"
                        )


                    }
                }


            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallbackthisweek)
        itemTouchHelper.attachToRecyclerView(binding.thisweekrecyclerview)


    }

    private fun checktodaylayoutvisibility() {
        if (todayapprovallist.isNotEmpty()) {
            todayadapter = TodayApprovalStausAdapter(
                isTDChecked,
                requireContext(),
                todayapprovallist,
                this,
                this,
                showRadioButton,
                false
            )
            binding.todayrecyclerview.adapter = todayadapter
            binding.todayradiobutton.setOnClickListener {

                isTDChecked = !isTDChecked

                todayadapter = TodayApprovalStausAdapter(
                    isTDChecked,
                    requireContext(),
                    todayapprovallist,
                    this,
                    this,
                    showRadioButton,
                    false
                )
                binding.todayrecyclerview.adapter = todayadapter

                // Update the radio button state based on the isMChecked value
                binding.todayradiobutton.isChecked = isTDChecked
            }
        }
        if (binding.todayrecyclerview.visibility == View.VISIBLE) {
            binding.downarrowtoday.setImageResource(R.drawable.downarrow)
            binding.todayrecyclerview.hide()
        } else {
            binding.downarrowtoday.setImageResource(R.drawable.uparrow)
            binding.todayrecyclerview.show()
        }


    }


    private fun changingNewsButtonLayout() {
        binding.mcvNews.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.mcvNews.setStrokeColor(resources.getColor(R.color.yellow))
        binding.mcvNews.strokeWidth = 1
        binding.txtapprovals.setTextColor(resources.getColor(R.color.white))
        binding.txtNews.setTextColor(resources.getColor(R.color.yellow))
        binding.mcvApprovals.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.mcvApprovals.strokeWidth = 0
    }

    private fun changingApprovalButtonLayout() {
        binding.mcvApprovals.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.mcvApprovals.setStrokeColor(resources.getColor(R.color.yellow))
        binding.txtapprovals.setTextColor(resources.getColor(R.color.yellow))
        binding.txtNews.setTextColor(resources.getColor(R.color.white))
        binding.mcvApprovals.strokeWidth = 1
        binding.mcvNews.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.mcvNews.strokeWidth = 0
    }



    fun getCurentDate(): org.threeten.bp.LocalDate {
        return LocalDate.now()


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
            if (s.lowercase().contains("today")) {
                todayadapter = TodayApprovalStausAdapter(
                    isTDChecked,
                    requireContext(),
                    todayapprovallist,
                    this,
                    this,
                    showRadioButton,
                    false
                )
                binding.todayrecyclerview.adapter = todayadapter
            } else if (s.lowercase().contains("thisweek")) {
                thisweekadapter = WeekApprovalStatusAdapter(
                    isWChecked,
                    requireContext(),
                    thiseeekapprovallist,
                    this,
                    this,
                    false,
                    showRadioButton
                )
                binding.thisweekrecyclerview.adapter = thisweekadapter
            } else if (s.lowercase().contains("thismonth")) {
                monthlyAdapter = MonthlyApprovalStatusAdapter(
                    isMChecked,
                    requireContext(),
                    thismonthapprovallist,
                    this,
                    false,
                    showRadioButton,
                    this
                )
                binding.thismonthrecyclerview.adapter = monthlyAdapter
            }
            nature = ""
            itemClickedPosition = 0
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }


    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }

    override suspend fun monthlyonBtnClick(
        text: String,
        approvalRequest: AcceptRejectRequestModel,
        position: Int,
        b: Boolean
    ) {
        if(b)
        {
            sessionmanagement?.setIsApprovalLayout("approvallayout")
            sessionmanagement?.setLateSittingNotID( approvalRequest.noticationIDs.toInt())
            try {
                findNavController().navigate(R.id.action_approvalAndNews_to_latesittingFragment2)            }
            catch (e:Exception)
            {

            }
        }
        else{
            itemClickedPosition = position
            nature = "Monthly"
            showRequestConfirmationDialog(text, approvalRequest, position, "today")
        }

    }
    override fun todayonBtnClick(
        text: String,
        approvalRequest: AcceptRejectRequestModel,
        position: Int,
        b: Boolean
    ) {
        if(b)
        {
            sessionmanagement?.setLateSittingNotID( approvalRequest.noticationIDs.toInt())
            sessionmanagement?.setIsApprovalLayout("approvallayout")
                findNavController().navigate(R.id.action_approvalAndNews_to_latesittingFragment2)
        }
        else{
            itemClickedPosition = position
            nature = "Today"
            showRequestConfirmationDialog(text, approvalRequest, position, "today")
        }
    }

    override fun todayonLongitemclick(list: ArrayList<Int>?, value: Boolean) {
        if (value) {
            showRadioButton = true
            binding.todayradiobutton.show()
            binding.thisweekradiobutton.show()
            binding.thismonthradiobutton.show()
            binding.acceptall.show()
            binding.rejecttall.show()
        } else {
            showRadioButton = false
            binding.todayradiobutton.hide()
            binding.thisweekradiobutton.hide()
            binding.thismonthradiobutton.hide()
            binding.acceptall.hide()
            binding.rejecttall.hide()
        }
    }

    override fun todayonitemclick(list: ArrayList<Int>?) {
        if (list != null) {
            todaySelectedItems = list

        }

        LoggerGenratter.getInstance().printLog("MY Today lis", todaySelectedItems.toString())

    }

    override fun todayonItemDetailClick(position: Int) {

        val todayModel = TodayModel(
            message = todayapprovallist.get(position).message,
            notificationID = todayapprovallist.get(position).notificationID,
            attachment = todayapprovallist.get(position).attachment
        )
        todayModel?.let {
            try {
                findNavController().navigate(
                    ApprovalAndNewsDirections.actionApprovalAndNewsToLeaveDetails(
                        todayModel
                    )
                )
            }
            catch (e:Exception)
            {

            }

        }
//        findNavController().navigate(ApprovalAndNewsDirections.actionApprovalAndNewsToLeaveDetails())
    }

    var notificationIDs: HashMap<Int, Int> = HashMap()
    override fun weekonitemclick(list: ArrayList<Int>?) {
        if (list != null && list.isNotEmpty()) {
            thisWeekSelectedItems = list

        }

        LoggerGenratter.getInstance().printLog("MY Week lis", thisWeekSelectedItems.toString())
    }

    override fun WeekonItemDetailClick(position: Int) {

        val todayModel = TodayModel(
            message = thiseeekapprovallist.get(position).message,
            notificationID = thiseeekapprovallist.get(position).notificationID,
            attachment = thiseeekapprovallist.get(position).attachment
        )
        todayModel?.let {
            try {
                findNavController().navigate(
                    ApprovalAndNewsDirections.actionApprovalAndNewsToLeaveDetails(
                        todayModel
                    )
                )
            }
            catch (e:Exception)
            {

            }


        }
    }

    override fun weekonLongItemclick(list: ArrayList<Int>?, value: Boolean) {
        if (value) {
            showRadioButton = true
            binding.todayradiobutton.show()
            binding.thisweekradiobutton.show()
            binding.thismonthradiobutton.show()
            binding.acceptall.show()
            binding.rejecttall.show()
        } else {
            showRadioButton = false
            binding.todayradiobutton.hide()
            binding.thisweekradiobutton.hide()
            binding.thismonthradiobutton.hide()
            binding.acceptall.hide()
            binding.rejecttall.hide()
        }
    }


    override fun monthlyonitemclick(list: ArrayList<Int>?) {
        if (list != null) {
            thisMonthSelectedItems = list

        }

        LoggerGenratter.getInstance().printLog("MY Month lis", thisMonthSelectedItems.toString())
    }

    override fun MonthonItemDetailClick(position: Int) {

        val todayModel = TodayModel(
            message = thismonthapprovallist.get(position).message,
            notificationID = thismonthapprovallist.get(position).notificationID,
            attachment = thismonthapprovallist.get(position).attachment
        )
        todayModel?.let {
            try {
                findNavController().navigate(
                    ApprovalAndNewsDirections.actionApprovalAndNewsToLeaveDetails(
                        todayModel
                    )
                )
            }
            catch (e:Exception)
            {

            }
        }
    }

    override fun monthlyonitemclick(list: ArrayList<Int>?, value: Boolean, position: Int) {
        if (value) {
            showRadioButton = true
            binding.todayradiobutton.show()
            binding.thisweekradiobutton.show()
            binding.thismonthradiobutton.show()
            binding.acceptall.show()
            binding.rejecttall.show()
        } else {
            showRadioButton = false
            binding.todayradiobutton.hide()
            binding.thisweekradiobutton.hide()
            binding.thismonthradiobutton.hide()
            binding.acceptall.hide()
            binding.rejecttall.hide()
        }
    }

    override suspend fun weekonBtnClick(
        text: String,
        approvalRequest: AcceptRejectRequestModel,
        position: Int,
        b: Boolean
    ) {
        if(b)
        {
            sessionmanagement?.setLateSittingNotID( approvalRequest.noticationIDs.toInt())
            sessionmanagement?.setIsApprovalLayout("approvallayout")
            try {
                findNavController().navigate(R.id.action_approvalAndNews_to_latesittingFragment2)            }
            catch (e:Exception)
            {

            }
        }
        else{
            itemClickedPosition = position
            nature = "week"
            showRequestConfirmationDialog(text, approvalRequest, position, "today")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._newsresponse.postValue(null)
        homeViewModel._accceptrejectresponse.postValue(null)
        notificationViewModel._inappresponse.postValue(null)
    }
}