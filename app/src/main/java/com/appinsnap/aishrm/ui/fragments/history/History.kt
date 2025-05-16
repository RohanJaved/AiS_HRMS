package com.appinsnap.aishrm.ui.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.databinding.FragmentHistoryBinding
import com.appinsnap.aishrm.ui.fragments.adapter.FragmentPageAdapter
import com.appinsnap.aishrm.ui.fragments.history.adapters.HistoryDateAdapter
import com.appinsnap.aishrm.ui.fragments.history.models.Body
import com.appinsnap.aishrm.ui.fragments.history.models.StausList
import com.appinsnap.aishrm.ui.fragments.history.models.historyrequestmodel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class History : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission,FragmentPageAdapter.notificationlistdata,HistoryDateAdapter.onDateClick {

    private val binding get() = _binding!!
    var selectedTabPosition:Int=0
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    val f1: SimpleDateFormat =
        SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)
    val f2: SimpleDateFormat = SimpleDateFormat("hh:mm aa")
    private var sessionmanagement: SessionManagement? = null
    private var fromDate: String = ""
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    private var allnotificationstatuslist: ArrayList<Body> = ArrayList()
    private var datelist: ArrayList<String> = arrayListOf("Today", "Last Week", "Last Month")
    private var toDate: String = ""
    private var selectdaytext: TextView? = null
    private var tvFromDate: TextView? = null
    private var requesttoDate: String = ""
    private var requestfromdate: String = ""
    private var tvToDate: TextView? = null
    private var _binding: FragmentHistoryBinding? = null
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        LiveDataObserver()
    }

    private fun LiveDataObserver() {

        homeViewModel.progressLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showProgressDialog(isShow = it)

        })

        homeViewModel._historyresponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        if (response != null) {
                            response.data?.let { settingAdapterToViewPager(it.body) }
                          //  settingUpTabLayout()
                        }
                    }
                    is NetworkResult.Error -> {
                        settingViewPagerAdapter()

                    }
                    is NetworkResult.Loading -> {

                    }
                }

            })
    }

    private fun settingViewPagerAdapter() {
        if(allnotificationstatuslist.isNotEmpty()){
            allnotificationstatuslist.clear()
        }

        val adapter = activity?.supportFragmentManager?.let {
            FragmentPageAdapter(
                it,
                lifecycle,
                allnotificationstatuslist,
                this
            )
        }
        binding.viewpager.adapter = adapter
    }


    private fun settingAdapterToViewPager(historylist: List<Body>) {

        if(historylist.isNotEmpty()) {
            settingUpTab()
            historylist.forEach {
                try {
                    if(it.checkin!=null)
                    {
                       var checkin = f1.parse(it.checkin)
                        var formatcheckintime = f2.format(checkin)
                        it.checkin = formatcheckintime
                    }
                    if(it.checkout!=null)
                    {
                      var checkout = f1.parse(it.checkout)
                        var formatcheckout = f2.format(checkout)
                        it.checkout = formatcheckout
                    }
                    if(it.fromDate!=null)
                    {
                        var splitdate = it.fromDate.split("T").get(0)
                        var date = LocalDate.parse(splitdate, inputFormat)
                        var formattedadate = outputFormat.format(date)
                        it.fromDate = formattedadate
                    }
                    var dateandtime = it.notificationDateTime.split("T")
                    var date = dateandtime[0]
                    val dateconvert = LocalDate.parse(date, inputFormat)
                    val formattedDate = outputFormat.format(dateconvert)
                    var time = dateandtime[1]
                    var parsetime = f1.parse(time)
                    var formattime = f2.format(parsetime)
                    var filtereddatetime = "${formattedDate}" + ","+" " + "${formattime}"
                    it.notificationDateTime = filtereddatetime
                    val string = ""
                } catch (e: Exception) {
                    LoggerGenratter.getInstance().printLog("HISYTORY FRAGMENT",  e.toString())
                }


            }
            allnotificationstatuslist = historylist as ArrayList<Body>
            val adapter = activity?.supportFragmentManager?.let {
                FragmentPageAdapter(
                    it,
                    lifecycle,
                    allnotificationstatuslist,
                    this
                )
            }
            binding.viewpager.adapter = adapter
        }
        else{

        }
    }

    private fun settingUpTab() {
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewpager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                binding.tab.selectTab(binding.tab.getTabAt(position))

            }
        })
    }

    private fun onClickListeners() {
        binding.mcvhistorydate.setOnClickListener {
            showhidedaterecyclerview()
        }
    }

    private fun showhidedaterecyclerview() {
        if (binding.mcvhistorydaterv.visibility == View.GONE) {
            binding.mcvhistorydaterv.visibility = View.VISIBLE
        } else {
            binding.mcvhistorydaterv.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        requesttoDate=MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
        requestfromdate = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
        populatingDatatoList()
        binding.txtdate.text = "Today"
        callHistoryApi()
        settingUpTabLayout()
        return view
    }

    private fun populatingDatatoList() {
        val adapter = HistoryDateAdapter(requireContext(), datelist,this)
        binding.historydaterecyclerview.adapter = adapter
    }

    private fun settingUpTabLayout() {
        val tab1 = binding.tab.newTab().setCustomView(R.layout.customtabitem)
        val tab2 = binding.tab.newTab().setCustomView(R.layout.customtabitem)
        val tab3 = binding.tab.newTab().setCustomView(R.layout.customtabitem)
        tab1.customView?.findViewById<TextView>(R.id.customtabtext)?.setText("Pending")
        tab2.customView?.findViewById<TextView>(R.id.customtabtext)?.setText("Rejected")
        tab3.customView?.findViewById<TextView>(R.id.customtabtext)?.setText("Approved")


        // Set the text color for each tab
        tab1.customView?.findViewById<TextView>(R.id.customtabtext)
            ?.setTextColor(resources.getColor(R.color.leaves_blue))
        tab2.customView?.findViewById<TextView>(R.id.customtabtext)
            ?.setTextColor(resources.getColor(R.color.absent_red))
        tab3.customView?.findViewById<TextView>(R.id.customtabtext)
            ?.setTextColor(resources.getColor(R.color.present_green))

        // Add the tabs to the TabLayout
        binding.tab.addTab(tab1)
        binding.tab.addTab(tab2)
        binding.tab.addTab(tab3)
        val adapter = activity?.supportFragmentManager?.let {
            FragmentPageAdapter(
                it,
                lifecycle,
                allnotificationstatuslist,
                this
            )
        }
        binding.viewpager.adapter = adapter

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewpager.currentItem = tab.position
                    selectedTabPosition = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                binding.tab.selectTab(binding.tab.getTabAt(position))
                selectedTabPosition = position

            }
        })
    }


    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean) {

    }

    override fun notificationstatuslist(allnotificationstatuslist: ArrayList<StausList>) {

    }

    override fun ondateitemclick(position: Int) {
        binding.mcvhistorydaterv.hide()
        if (datelist.get(position).contains("Today")) {
            getCurrentDate()
            callHistoryApi()
            binding.txtdate.text = datelist.get(position)
        } else if (datelist.get(position).contains("Last Week")) {
            binding.txtdate.text = datelist.get(position)
                  getCurrentWeekDate()
            callHistoryApi()
        } else {
            binding.txtdate.text = datelist.get(position)
             getCurrentMonthDate()
            callHistoryApi()
        }
    }

    private fun callHistoryApi() {
        var empid = sessionmanagement?.getEmpId().toString()
        val historyrequest = historyrequestmodel(employeeID = empid, toDate = requesttoDate, fromDate = requestfromdate)
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getHistoryInfo(historyrequest)
        }
    }

    private fun getCurrentMonthDate() {
        val currentDate = LocalDate.now()
        val thirtyDaysAgoDate = LocalDate.now().minusDays(30)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val currentDateString = currentDate.format(formatter)
        requesttoDate = currentDateString
        val thirtyDaysAgoDateString = thirtyDaysAgoDate.format(formatter)
        requestfromdate = thirtyDaysAgoDateString

    }

    private fun getCurrentWeekDate() {
        // Set the calendar to the current day
        calendar.time = Date()
        val currentDate = Calendar.getInstance().time
        // Find the first day of the current week (Monday)
        val currentDateString = dateFormat.format(currentDate)
        requesttoDate = currentDateString

        // Get the date of 5 days ago
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)
        val fifthDayAgoDate = calendar.time
        val fifthDayAgoDateString = dateFormat.format(fifthDayAgoDate)
        requestfromdate = fifthDayAgoDateString
    }

    private fun getCurrentDate() {
        requesttoDate =   MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
        requestfromdate = MyHelperClass.getCurrentDateYYYYMMDD_Dashes()
    }
}