package com.appinsnap.aishrm.util.services

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.appinsnap.aishrm.BuildConfig
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.network.NetworkApi
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import com.appinsnap.aishrm.ui.fragments.home.models.AddAttendanceRequestModel
import com.appinsnap.aishrm.ui.fragments.home.models.SettingApiResponse
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.SessionManagement
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Adnan Bashir manak on 07,July,2023
 * AIS company,
 * Krachi, Pakistan.
 */


@AndroidEntryPoint
class LocationService : Service(), LocationListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var notification: Notification? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var countryName = ""
    private var userId = ""
    private val jsonObjLocationList = arrayListOf<String>()
    private var isTenSecondIntervalOn = false
    private var mLastClickTime: Long = 0L
    lateinit var provideRetrofitclient: Retrofit
    private val networkApi: NetworkApi by lazy {
        provideRetrofitclient.create(NetworkApi::class.java)
    }
    private var apiIsAlreadyCalling = false

    companion object {

        private const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val TAG: String = "TrackingService"
        private const val twoMinutes: Long = ((1000 * 60) * 2)
        private const val thirtySeconds: Long = ((1000 * 60) / 2)
        private const val tenSeconds: Long = ((1000 * 10))

    }

    private var cdt: CountDownTimer? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        LoggerGenratter.getInstance().printLog(TAG, "Oncreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val userId = intent?.getStringExtra("userId") ?: ""
        this.userId = userId
        LoggerGenratter.getInstance().printLog("user", "onStartCommand 112233")

        createNotificationChannel()
        createLocationRequest()
        getLocationCallBack()
        startLocationUpdates()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, FLAG_IMMUTABLE)

        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Tracking Service")
            .setSmallIcon(R.drawable.logo)
//            .setContentText(data)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .build()

        notification!!.flags = notification!!.flags or Notification.FLAG_FOREGROUND_SERVICE

        startForeground(1, notification)

        return START_NOT_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


    override fun onLocationChanged(p0: Location) {
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateDistanceMeters(100.0F)
//            .setMinUpdateIntervalMillis(1000)
//            .setMaxUpdateDelayMillis(1000)
            .build()


    }

    private fun getLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {

                if (location.locations.isNotEmpty()) {
                    latitude = location.locations[0].latitude
                    longitude = location.locations[0].longitude
                    LoggerGenratter.getInstance()
                        .printLog("onLocationResult1:", " $latitude $longitude")
                }
                for (location1 in location.locations) {
                    latitude = location1.latitude
                    longitude = location1.longitude
//                    Timber.tag(TAG).d("onLocationResult2: $latitude $longitude")
                    LoggerGenratter.getInstance()
                        .printLog("onLocationResult2:", " $latitude $longitude")

//                    addLastActivity(long,lat)
//                    shareLiveLocationToServer(long, lat)
                }
//                Toast.makeText(applicationContext, "location changed", Toast.LENGTH_SHORT).show()
                val json = JSONObject()


                json.put("Longitude", longitude)
                json.put("Latitude", latitude)
                json.put("City", countryName)
                json.put("Country", countryName)
//              json.put("IsLiveTrackLocation", isLiveTrackLocation)
//              json.put("UserID", userId)

                jsonObjLocationList.add("$json")
//                Timber.tag(TAG).d("${jsonObjLocationList.size} : JSONObject: $json")
                LoggerGenratter.getInstance().printLog(
                    "onLocationResult2:",
                    " ${jsonObjLocationList.size} : JSONObject: $json"
                )


                checklocations()

                if (SystemClock.elapsedRealtime() - mLastClickTime < twoMinutes) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime()
//                getCompleteAddressString(latitude, longitude)
            }
        }
    }


    override fun onDestroy() {

        LoggerGenratter.getInstance()
            .printLog("serviceLogs:", "Service Destroyed")

        fusedLocationClient.removeLocationUpdates(locationCallback)
        try {
            cdt?.cancel()
        } catch (exp: Exception) {
            LoggerGenratter.getInstance()
                .printLog("onLocationResult2:", "Service Exp onDestroy: $exp")

        }
        super.onDestroy()
    }


//    private fun makeAttendanceModel(status: String): AddAttendanceRequestModel {
//        val attendancerequest = AddAttendanceRequestModel(
//            employeeID = SessionManagement(this).getEmpId(),
//            latitude = latitude.toDouble(),
//            longitude = longitude.toDouble(),
//            status = status,
//            comment = "",
//            employeeLocation = "office",
//            fromDate = MyHelperClass.getCurrentDateYYYYMMDD_Dashes(),
//            toDate = MyHelperClass.getCurrentDateYYYYMMDD_Dashes(),
//            noofDays = "",
//
//        )
//
//        return attendancerequest
//
//    }

    private fun makeApiCallAddAttendance(data: AddAttendanceRequestModel, message: String) {


        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(NetworkApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(!apiIsAlreadyCalling) {
                    apiIsAlreadyCalling = true
                    val user = apiService.getAttendanceInfo(data)
                    // Process the API response as needed
                    // ...
                    val sessionManagement = SessionManagement(this@LocationService)
                    LoggerGenratter.getInstance().printLog("Response checkin", user.toString())
                    if (user.code() == 200) {
                        if (sessionManagement.getLastLocation().contains("outoffice")) {
                            sessionManagement.setLastLocation("inoffice")
                            val intent: Intent =
                                Intent("com.appinsnap.aishrm") // Customize the action to match your needs
                            intent.putExtra("key", "checkin") // Add any data you want to pass

                            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                        } else if (sessionManagement.getLastLocation().contains("inoffice")) {
                            sessionManagement.setLastLocation("outoffice")
                            val intent: Intent =
                                Intent("com.appinsnap.aishrm") // Customize the action to match your needs
                            intent.putExtra("key", "checkout") // Add any data you want to pass

                            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                        }
                        creatinoutNotification(message);
                    }
                    apiIsAlreadyCalling = false

                }

            } catch (e: Exception) {
                // Handle any errors
                // ...
            }
        }
    }

    private fun checklocations() {
        LoggerGenratter.getInstance().printLog("ADD_ATTENDANCE", "CHECK LOCATIONS LIST")
        var officesLocationResponse: ArrayList<SettingApiResponse.Location?> = arrayListOf()
        officesLocationResponse = SessionManagement(this).getLocations()
        val isLogin = SessionManagement(this).isLogin()
        if (officesLocationResponse.isNotEmpty()) {
            checkLocation(officesLocationResponse, this)
        }
    }

    private fun getOfficeDistance(
        latitudeOffice: String,
        longitudeOffice: String
    ): Double? {
        LoggerGenratter.getInstance().printLog("ADD_ATTENDANCE", "CALCULATE DISTANCE")

        try {
            val latLngA = LatLng(latitude.toDouble(), longitude.toDouble())
            val latLngB =
                LatLng(latitudeOffice.toDouble(), longitudeOffice.toDouble())
            val locationA = Location("point A")
            locationA.latitude = latLngA.latitude
            locationA.longitude = latLngA.longitude
            val locationB = Location("point B")
            locationB.latitude = latLngB.latitude
            locationB.longitude = latLngB.longitude

            val distance = locationA.distanceTo(locationB).toDouble()
            return distance
        } catch (exception: Exception) {
            return null
        }


    }

    private fun checkLocation(
        locationdata: ArrayList<SettingApiResponse.Location?>,
        mcontext: Context
    ) {
        LoggerGenratter.getInstance().printLog("ADD_ATTENDANCE", "CALCULATE DISTANCE")

        val context = mcontext

        GlobalScope.launch(Dispatchers.IO) {
            var isOffice = false
            kotlin.run Out@{
                locationdata.forEach { office ->
                    office?.let {
                        val distance = getOfficeDistance(
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                        distance?.let { dis ->
                            /*val radius = 20
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "total distance $dis",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (dis <= radius) {
                                isOffice = true
                                return@Out
                            }*/


                            office.radius?.let { radius ->
                                if (dis <= radius) {
                                    isOffice = true
                                    return@Out
                                }
                            }
                        }


                        /*distance?.let { dis ->
                            locationdata.forEach { item ->
                                *//* val radius = 200
                                 withContext(Dispatchers.Main) {
                                     Toast.makeText(
                                         context,
                                         "total distance $dis",
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 }
                                 if (dis <= radius) {
                                     isOffice = true
                                     return@Out
                                 }*//*
                                item?.radius?.let { radius ->
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "total distance $dis",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    if (dis <= radius) {
                                        isOffice = true
                                        return@Out
                                    }
                                }
                            }
                        }*/
                    }

                }
            }
            val sessionManagement = SessionManagement(context)
            withContext(Dispatchers.Main) {
                /* Toast.makeText(
                     context,
                     "current ${sessionManagement.getLastLocation()}",
                     Toast.LENGTH_SHORT
                 ).show()*/
                if (isOffice) {
                    if (sessionManagement.getLastLocation().contains("outoffice")) {
//                        sessionManagement.setLastLocation("inoffice")
                     //   makeApiCallAddAttendance(
                           // makeAttendanceModel("CheckIn"),
                          //  "Checked-in at " + MyHelperClass.getCurrentDateHHMMSS_12()
                      //  )
                    }
                } else {
                    if (sessionManagement.getLastLocation().contains("inoffice")) {
//                        sessionManagement.setLastLocation("outoffice")
                       // makeApiCallAddAttendance(
                            //makeAttendanceModel("Checkout"),
                       //     "Checked-out at " + MyHelperClass.getCurrentDateHHMMSS_12()
                       // )
                    }

                }
            }
        }


    }

    fun stopLocationUpdates() {
        TODO("Not yet implemented")
    }


//    class MyViewModelStoreOwner(private val context: Context) : ViewModelStoreOwner,
//        LifecycleOwner {
//
//        private val lifecycleRegistry = LifecycleRegistry(this)
//
//        private val viewModelStore = ViewModelStore()
//
//        init {
//            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
//        }
//
//        override fun getLifecycle(): Lifecycle {
//            return lifecycleRegistry
//        }
//
//        override fun getViewModelStore(): ViewModelStore {
//            return viewModelStore
//        }
//
//        fun getContext(): Context {
//            return context
//        }
//    }


    private fun creatinoutNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // only for gingerbread and newer versions
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                this,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT or FLAG_IMMUTABLE
            )
        }
        val channelId = "fcm_default_channel" //getString(R.string.default_notification_channel_id);
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("AIS HRMS")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /*  */, notificationBuilder.build())
    }
}
