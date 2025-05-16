package com.appinsnap.aishrm.ui.activities.mainactivity

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.appinsnap.aishrm.BuildConfig
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.ActivityMainBinding
import com.appinsnap.aishrm.ui.activities.login.LoginActivity
import com.appinsnap.aishrm.ui.activities.login.viewModel.AuthenticationViewModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.LogoutRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.NotificationCountRequestModel
import com.appinsnap.aishrm.ui.activities.mainactivity.models.NotificationCountResponseModel
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.Constants
import com.appinsnap.aishrm.util.GeneralDialogActivity
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.Utils
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.receivers.NetworkChangeReceiver
import com.appinsnap.aishrm.util.services.LocationService
import com.appinsnap.aishrm.util.showProgressBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wallet.zindigi.Utils.PermissionsUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : GeneralDialogFragment.onClick, NetworkChangeReceiver.NetworkChangeListener,
    GeneralDialogActivity(), GeneralDialogActivity.onClick,
    GeneralDialogActivity.locationPermission, NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    private var sessionmanagement: SessionManagement? = null
    var departmentid: ArrayList<Int> = ArrayList()
    var employeeID: Int = 0
    lateinit var progressBarDialogRegister: ProgressBarDialog
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }


    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    var permissionsUtil: PermissionsUtil? = null
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.FOREGROUND_SERVICE)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
//                }
                else {
                    startService()
                }
            } else {
                startService()
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun notificationPermissionCheckToStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            if (areNotificationsEnabled) {
                startService()
            }
        } else {
            startService()
        }
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    var hasNotificationPermissionGranted = false
    private fun startService() {
        LoggerGenratter.getInstance()
            .printLog("serviceLogs:", "Service called")
        /*val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        LoggerGenratter.getInstance()
            .printLog("serviceLogs:", "Service started")*/
    }

    override fun onResume() {
        super.onResume()
        Constants.IS_MAIN_OPEN = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            MyApplication.get()!!.activity = this@MainActivity
            AndroidThreeTen.init(this)
        }
        catch (e:java.lang.Exception)
        {

        }


        Constants.IS_MAIN_OPEN = true
        sessionmanagement = SessionManagement(this)
        clearCacheOnUninstall()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter)

        permissionsUtil = PermissionsUtil.getInstance(this, activityResultRegistry)
        lifecycle.addObserver(permissionsUtil!!)


        progressBarDialogRegister = ProgressBarDialog(this)

        setSupportActionBar(binding.toolbarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)
        setUpDrawerLayout()
        showAppVersion()
        navigatingFragmentswithAnimation()
        onClickListener()
        checkForPushNotification()
        callNotificationCountApi()
        liveDataObserver()
        notificationPermissionCheckToStartService()
    }

    private fun clearCacheOnUninstall() {
        val packageManager: PackageManager = applicationContext.packageManager
        val packageName: String = this.packageName
        val flags: Int = PackageManager.GET_META_DATA
        val applicationInfo = packageManager.getApplicationInfo(packageName, flags)
        applicationInfo.metaData.putString("android.app.extra.UNINSTALL_ALL_USERS", "true")
    }


    private fun showAppVersion() {
        binding.txtversion.text = "Version" + " " + BuildConfig.VERSION_NAME
    }

    private fun liveDataObserver() {


        authenticationViewModel._notificationcountresponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    changingViewForCountIcon(response)
                   // callApiForEvery20Sec()
                }
                is NetworkResult.Error -> {
                    callApiForEvery20Sec()
                    // view.showSnackbar("${response.message}")
                    //   Toast.makeText(this, "${response.message}", Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {

                }
            }

            authenticationViewModel.progressLoading.observe(this, androidx.lifecycle.Observer {
                showProgressBar(progressBarDialogRegister, it)

            })
            authenticationViewModel._logoutstatusresponse.observe(this) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        sessionmanagement?.clearPreference()
                        //  stopLocationService()
                        val sharedPreference: SharedPreferences =
                            getSharedPreferences("login", Context.MODE_PRIVATE)
                        val edit = sharedPreference.edit()
                        edit.putBoolean("Login", false)
                        edit.putInt("value", 0)
                        edit.commit()
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()

                    }
                    is NetworkResult.Error -> {
                        showGeneralDialogActivity(
                            this,
                            false,
                            "",
                            this,
                            this,
                            false,
                            "",
                            "${response.message}",
                            icon = R.drawable.reject
                        )
                    }
                    is NetworkResult.Loading -> {

                    }
                }

            }

        }
    }

    private fun changingViewForCountIcon(response: NetworkResult.Success<NotificationCountResponseModel>) {


        if (response.data?.body?._NotificationCount == "0" || response.data?.body?._NotificationCount.isNullOrEmpty()) {
            binding.toolbarlayout.notificationcount.hide()

        } else if (response.data?.body?._NotificationCount?.toInt()!! < 100) {
            binding.toolbarlayout.notificationcount.show()

            binding.toolbarlayout.notificationcount.text = response.data?.body?._NotificationCount
        } else {
            binding.toolbarlayout.notificationcount.text = "99" + "+"
            binding.toolbarlayout.notificationcount.show()

        }


    }

    private fun callApiForEvery20Sec() {
        GlobalScope.launch {
            delay(20000)
            callNotificationCountApi()
        }
    }


    fun callNotificationCountApi() {

        CoroutineScope(Dispatchers.Main).launch {
            var empemail = sessionmanagement!!.getEmail()
            val notificationcountrequest = NotificationCountRequestModel(email = empemail)
            authenticationViewModel.getNoficationCount(notificationcountrequest)
            // homeViewModel.getDashboardData(dashboardrequest)
        }
    }

    companion object {
        fun callNotificationCountUpdateApi(
            context: Context,
            authenticationViewModel: AuthenticationViewModel
        ) {

            CoroutineScope(Dispatchers.Main).launch {
                var empemail = SessionManagement(context)!!.getEmail()
                val notificationcountrequest = NotificationCountRequestModel(email = empemail)
                authenticationViewModel.getNoficationCount(notificationcountrequest)
                // homeViewModel.getDashboardData(dashboardrequest)
            }
        }
    }

    fun updatenotification() {
        callNotificationCountApi()
    }

    var flag: Boolean = false
    override fun onBackPressed() {

        when (navController.currentDestination?.id) {

            R.id.homefragment -> {
                exitApplication()
            }

            else -> {
                super.onBackPressed()
            }

        }

    }

    private fun exitApplication() {
        val logoutDialog = Dialog(this)
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.logout_dialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val txt:TextView = logoutDialog.findViewById(R.id.txtSuccesss)
        txt.text = "Are you sure you want to exit the app?"
        //   val img:ImageView=logoutDialog.findViewById()

        yes.setSafeOnClickListener {
            logoutDialog.dismiss()
            lifecycleScope.launch{
                delay(500)
                finish()
            }
        }

        no.setSafeOnClickListener {
            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }


    private fun setUpDrawerLayout() {
        binding.navigationView.setupWithNavController(navController)
        /*Setup Header*/
        val headerView = binding.navigationView.getHeaderView(0)

        val imgMenu = headerView.findViewById<ImageView>(R.id.imgMenu)

        imgMenu.setSafeOnClickListener {
            closeDrawer()
        }
    }

    private fun navigatingFragmentswithAnimation() {
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .build()

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.dashboard -> {
                    navController.navigate(R.id.homefragment, null, null)
                    closeDrawer()
                }


            }
            true
        }

    }

    private fun onClickListener() {
        binding.toolbarlayout.imgBurgerMenu.setSafeOnClickListener {


            openDrawer()
        }
        binding.toolbarlayout.navigationicon.setSafeOnClickListener {

            onBackPressed()
        }


        binding.toolbarlayout.toolbsrprofileimg.setSafeOnClickListener {
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .build()
            navController.navigate(R.id.approvalAndNews, null, options)

        }
    }


    private fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawers()
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.timelinefragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                handletoolbarintimelinefragment()
                binding.toolbarlayout.filter.visibility = View.GONE

            }

            R.id.checkinoutfragment -> {
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.setAttendanceStatus("")
                handletoolbarincheckinoutfragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.homefragment -> {
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.setAttendanceStatus("")
                handletoolbarinhomefragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.profilefragment -> {
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.setAttendanceStatus("")
                handletoolbarinprofilefragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.updateprofilefragment -> {
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.setAttendanceStatus("")
                binding.toolbarlayout.filter.visibility = View.GONE
                handletoolbarinupdateprofilefragment()
            }

            R.id.attendancehistoryfragment -> {
                handletoolbarinattendancehistoryfragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.notificationfragment -> {
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.setAttendanceStatus("")
                binding.toolbarlayout.filter.visibility = View.GONE
                handletoolbarinnotificatioinfragment()
            }

            R.id.employeeDetail -> {
                handletoolbarinemployeedetailfragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.employeeattendance -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                handletoolbarinemployeeattendancefragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.approvalfragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                binding.toolbarlayout.filter.visibility = View.GONE
                handletoolbarinapprovalfragment()
            }

            R.id.leavemanagement -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                handletoolbarinleavemanagementfragment()
                binding.toolbarlayout.filter.visibility = View.GONE
            }

            R.id.approvalAndNews -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")
                binding.toolbarlayout.tvToolbarTitle.text = "Approvals"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.filter.visibility = View.GONE
                binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }

            R.id.leaveapplicationfragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")
                binding.toolbarlayout.tvToolbarTitle.text = "Leave Application"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(16f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.filter.visibility = View.GONE
                binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE


            }
            R.id.latesittingFragment ->{
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")
                binding.toolbarlayout.tvToolbarTitle.text = "Details"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.filter.visibility = View.GONE
                binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }
            R.id.policiesfragment ->{
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                binding.toolbarlayout.tvToolbarTitle.text = "Policies & SOP's"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.filter.visibility = View.GONE
                binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }

            R.id.historyfragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")
                binding.toolbarlayout.tvToolbarTitle.text = "History"
                val typeface = ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
                binding.toolbarlayout.filter.visibility = View.GONE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }

            R.id.statisticsFragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")

                binding.toolbarlayout.tvToolbarTitle.text = "Statistics"
                val typeface = ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.toolbsrprofileimg.show()
                binding.toolbarlayout.filter.visibility = View.VISIBLE
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }
            R.id.leavedetailFragment -> {
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                sessionmanagement?.selectedPDFViewTab("policies")
                handletoolbarLeavesDetailsfragment()
            }

            R.id.requestResubmissionfragment -> {
                sessionmanagement?.selectedPDFViewTab("policies")
                binding.toolbarlayout.tvToolbarTitle.text = "Request Details"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.toolbsrprofileimg.hide()
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }
            R.id.pdfViewFragment ->{
                sessionmanagement?.setAttendanceStatus("")
                sessionmanagement?.setCheckoutTime("")
                sessionmanagement?.setCheckInTime("")
                binding.toolbarlayout.tvToolbarTitle.text = "Policies & SOP's"
                val typeface =
                    ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
                binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
                binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
                binding.toolbarlayout.imgBurgerMenu.hide()
                binding.toolbarlayout.navigationicon.show()
                binding.toolbarlayout.toolbsrprofileimg.hide()
                binding.toolbarlayout.img.visibility = View.GONE
                binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
            }
        }
    }

    private fun handletoolbarinleavemanagementfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")

        binding.toolbarlayout.tvToolbarTitle.text = "Leave Management"
        val typeface =
            ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.img.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.notificationcountlayout.visibility = View.VISIBLE
        binding.toolbarlayout.toolbsrprofileimg.setImageResource(R.drawable.notification_bell)
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.VISIBLE

    }

    private fun handletoolbarinapprovalfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.tvToolbarTitle.text = "Approvals"
        val typeface =
            ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
//        binding.bottombar.visibility = View.GONE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.WHITE)
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
    }

    private fun handletoolbarinemployeeattendancefragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.tvToolbarTitle.text = "Attendance"
        val typeface =
            ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.toolbsrprofileimg.setImageResource(R.drawable.notification_bell)
        binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
    }

    private fun handletoolbarinemployeedetailfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.tvToolbarTitle.text = "Employee Details"
        val typeface =
            ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.notificationcountlayout.visibility = View.GONE
    }

    private fun handletoolbarinattendancehistoryfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.tvToolbarTitle.text = "History"
        binding.toolbarlayout.tvToolbarTitle.typeface = Typeface.DEFAULT_BOLD
//        binding.bottombar.visibility = View.GONE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.INVISIBLE
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.WHITE)
    }

    private fun handletoolbarinnotificatioinfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")

        binding.toolbarlayout.tvToolbarTitle.text = "Notification"
        val typeface = ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.VISIBLE
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.WHITE)
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.notificationcountlayout.visibility = View.GONE

    }

    private fun handletoolbarLeavesDetailsfragment() {
        binding.toolbarlayout.tvToolbarTitle.text = "Leave Details"
        val typeface = ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.imgBurgerMenu.hide()
        binding.toolbarlayout.navigationicon.show()
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.GONE
        binding.toolbarlayout.filter.visibility = View.GONE
        binding.toolbarlayout.img.visibility = View.GONE
        binding.toolbarlayout.notificationcountlayout.visibility = View.GONE

    }


    private fun handletoolbarinupdateprofilefragment() {
        binding.toolbarlayout.toolbar.visibility = View.VISIBLE
        binding.toolbarlayout.tvToolbarTitle.text = "Update Profile"
//        binding.bottombar.visibility = View.GONE
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun handletoolbarintimelinefragment() {
//        binding.bottombar.visibility = View.VISIBLE
        binding.toolbarlayout.toolbar.visibility = View.GONE
    }


    private fun handletoolbarincheckinoutfragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.toolbar.visibility = View.VISIBLE
//        binding.bottombar.visibility = View.VISIBLE
        binding.toolbarlayout.tvToolbarTitle.text = "History"
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.parseColor("#ffffff"))
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.INVISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.INVISIBLE

    }

    private fun handletoolbarinhomefragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.toolbar.visibility = View.VISIBLE
        binding.toolbarlayout.notificationcountlayout.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.toolbarlayout.navigationicon.visibility = View.GONE
        sessionmanagement = SessionManagement(this)
        binding.toolbarlayout.toolbsrprofileimg.setImageResource(R.drawable.notification_bell)
        binding.toolbarlayout.tvToolbarTitle.text = "Dashboard"
        val typeface = ResourcesCompat.getFont(this, com.appinsnap.aishrm.R.font.montserrat_semibold)
        binding.toolbarlayout.tvToolbarTitle.setTypeface(typeface)
        binding.toolbarlayout.tvToolbarTitle.setTextSize(15f)
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.parseColor("#ffffff"))
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.VISIBLE
        binding.toolbarlayout.img.visibility = View.VISIBLE
    }

    private fun handletoolbarinprofilefragment() {
        sessionmanagement?.selectedPDFViewTab("policies")
        binding.toolbarlayout.toolbar.visibility = View.VISIBLE
        binding.toolbarlayout.tvToolbarTitle.text = "Profile"
        binding.toolbarlayout.tvToolbarTitle.setTextColor(Color.parseColor("#ffffff"))
        binding.toolbarlayout.navigationicon.visibility = View.VISIBLE
        binding.toolbarlayout.imgBurgerMenu.visibility = View.GONE
        binding.toolbarlayout.toolbsrprofileimg.visibility = View.INVISIBLE
    }


    fun clickNavigation(view: View) {
        when (view.id) {
            R.id.logout -> {
                logoutApplication()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
//            R.id.attendanceoption -> {
//            if(binding.approval.visibility == View.GONE)
//            {
//                binding.arrowup.setImageResource(R.drawable.arrowdown)
//                binding.approval.visibility = View.VISIBLE
//            }
//            else
//            {
//                binding.arrowup.setImageResource(R.drawable.arrowup)
//                 binding.approval.visibility = View.GONE
//            }
//            }
            R.id.dashboard -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.homefragment, null, null)
            }

            R.id.leaves -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.leavemanagement, null, null)
            }

            R.id.history -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.historyfragment, null, null)
            }
            R.id.policiessops ->{
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.policiesfragment, null, null)
            }
            /*R.id.attendance->{
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.employeeDetail,null,null)
            }*/


//            R.id.laevelayout->{
//                if (binding.requestleavelayout.getVisibility() === View.VISIBLE) {
//                    binding.requestleavelayout.setVisibility(View.GONE)
//                    binding.arrow.setImageResource(R.drawable.downarrow)
//
//                } else {
//                    binding.requestleavelayout.setVisibility(View.VISIBLE)
//                    binding.arrow.setImageResource(R.drawable.arrowup)
//                }
//            }
//            R.id.attendancelayout->{
//                if (binding.consolidatedattendancelayout.getVisibility() === View.VISIBLE) {
//                    binding.consolidatedattendancelayout.setVisibility(View.GONE)
//                    binding.arrowadmin.setImageResource(R.drawable.downarrow)
//
//                } else {
//                    binding.consolidatedattendancelayout.setVisibility(View.VISIBLE)
//                    binding.arrowadmin.setImageResource(R.drawable.arrowup)
//                }
//            }


        }


    }

    private fun logoutApplication() {
        showLogoutDialog()
    }

    private fun checkForPushNotification() {
        try {
            val intent = intent
            if (intent != null && intent.extras != null) {
                val extras = intent.extras
                if (extras!!.getBoolean("isPushNotification", false)) {
                    navController.navigate(R.id.notificationfragment, null, null)
                }

            }
        } catch (exp: Exception) {
            LoggerGenratter.getInstance()
                .printLog("LOGIN", "checkForPushNotification: ${exp.message}")
        }
    }

    override fun onclick(value: Boolean) {

    }

    override fun onLocationPermission(locationvalue: Boolean)
    {
    }

    private fun showLogoutDialog() {
        val logoutDialog = Dialog(this)
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.logout_dialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        //   val img:ImageView=logoutDialog.findViewById()

        yes.setSafeOnClickListener {
            logoutDialog.dismiss()
             callLogoutApi()

        }

        no.setSafeOnClickListener {
            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }

    private fun callLogoutApi() {
        var request = LogoutRequestModel(employeeid = sessionmanagement?.getEmpId()!!.toInt())
        CoroutineScope(Dispatchers.Main).launch {
            authenticationViewModel.logoutUser(request)
        }
    }

    private fun stopLocationService() {
        val serviceIntent = Intent(this@MainActivity, LocationService::class.java)
        this@MainActivity.stopService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
      //  Constants.IS_MAIN_OPEN = false

    }

    override fun onStop() {
        super.onStop()
        Constants.IS_MAIN_OPEN = false

    }

    override fun onStart() {
        super.onStart()
     //   Constants.IS_MAIN_OPEN = true

    }

    override fun onNetworkChanged(isConnected: Boolean) {
        if (!isConnected) {
            InternetCheck(
                setCancelAble = false,
                okBtnText = "Retry",
                yourTitle = "",
                yourMessage = "No Internet Connection !",
                icon = R.drawable.erroricon,
                //   setCancelAble = false,
            )
        }
        else {

            GeneralDialogFragment().showProgressDialog(false)
        }
    }

    fun InternetCheck(
        setCancelAble: Boolean = true,
        yourTitle: String,
        yourMessage: String,
        okBtnText: String = "Ok",
        icon: Int
    ) {
        try {
            val dialogInternet = Dialog(this)
            dialogInternet.window?.requestFeature(Window.FEATURE_NO_TITLE)
            //   dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogInternet.setCancelable(setCancelAble)
            dialogInternet.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogInternet.setContentView(R.layout.general_dialog_design)
            val okBtn = dialogInternet.findViewById(R.id.ok) as? ConstraintLayout
            val alert = dialogInternet.findViewById(R.id.txtalert) as TextView
            val message = dialogInternet.findViewById(R.id.txtsuccess) as TextView
            val popIcon = dialogInternet.findViewById(R.id.imgPopIcon) as ImageView
            val buttonTxt = dialogInternet.findViewById<TextView>(R.id.checkout)
            popIcon.setImageResource(icon)
            buttonTxt.text = okBtnText
            message.text = yourMessage
            okBtn?.setSafeOnClickListener {
                if (Utils.hasInternetConnection()) {
                    dialogInternet.dismiss()
                    Constants.IS_DIALOG_SHOWING = false
                } else {
                    dialogInternet.dismiss()
                    dialogInternet.show()

                }

            }
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialogInternet.getWindow()?.getAttributes())
            lp.width = ActionBar.LayoutParams.MATCH_PARENT

            lp.height = ActionBar.LayoutParams.MATCH_PARENT

            dialogInternet.getWindow()?.setAttributes(lp)
            if (!Constants.IS_DIALOG_SHOWING) {
                dialogInternet.show()
                Constants.IS_DIALOG_SHOWING = true
            }
        } catch (e: Exception) {
            LoggerGenratter.getInstance().printLog("INTERNET  CHECK", e.message)

        }
    }
}