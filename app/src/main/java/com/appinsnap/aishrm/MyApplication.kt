package com.appinsnap.aishrm

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
//import androidx.lifecycle.ViewModelProvider
//import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
//import com.appinsnap.aishrm.util.services.LocationService
//import com.appinsnap.aishrm.util.services.LocationServiceViewModelStoreOwner
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import dagger.hilt.android.HiltAndroidApp
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

@HiltAndroidApp
class MyApplication: Application() {

    var activity: Activity? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        var INSTANCE: MyApplication? = null

        fun get(): MyApplication? {
            return INSTANCE
        }


    }




    override fun onCreate() {
        super.onCreate()
        initializeSSLContext(applicationContext)

        INSTANCE = this
        setupNotificationChannels()
    }
    fun initializeSSLContext(mContext: Context) {
        try {
            SSLContext.getInstance("TLS v1.2")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        try {
            ProviderInstaller.installIfNeeded(mContext.applicationContext)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }
    private fun setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setShowBadge(true)
            notificationChannel.enableVibration(true)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}