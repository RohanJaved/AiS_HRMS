package com.appinsnap.aishrm.ui.activities.splashscreen

import android.app.ActionBar
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.appinsnap.aishrm.BuildConfig
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ActivitySplashScreenBinding
import com.appinsnap.aishrm.ui.activities.login.LoginActivity
import com.appinsnap.aishrm.util.LoggerGenratter
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var isPushNotification=false;
    private val NOTIFICATION_SETTINGS_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(
            !BuildConfig.DEBUG
        )
        checkForPushNotification()
//        if(isPackageExist("com.JSBL.bank", this))
//        {
//            showDialog()
//        }else
//            notificationCheckForSetting()
//        moveToLoginScreen()
//        notificationPermissionCheck()

//        notificationCheckForSetting()

        moveToLoginScreen()
    }

    private fun notificationCheckForSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            if (areNotificationsEnabled) {
                moveToLoginScreen()
            }
            else
            {
                showNotificationDialog()
            }
        }
        else{
            moveToLoginScreen()
        }
    }

    fun isPackageExist(target: String ,context: Context): Boolean {
        return context.packageManager.getInstalledApplications(0)
            .find { info -> info.packageName == target } != null
    }

    private fun moveToLoginScreen() {
        lifecycleScope.launch {
            delay(3000L)
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java).putExtra("isPushNotification", isPushNotification))
            finish()
        }
    }

    private fun showNotificationDialog() {
        val logoutDialog = Dialog(this)
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(false)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.newdialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val txt: TextView = logoutDialog.findViewById(R.id.txtsuccess)
        val ok: ConstraintLayout = logoutDialog.findViewById(R.id.ok)
        //   val img:ImageView=logoutDialog.findViewById()
        txt.text = "Please allow notification permission from settings"

        ok.setSafeOnClickListener {
            logoutDialog.dismiss()
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)

//            startActivity(intent)
            startActivityForResult(intent, NOTIFICATION_SETTINGS_REQUEST_CODE)
        }
        logoutDialog.show()
    }

    private fun showDialog() {
        val logoutDialog = Dialog(this)
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(false)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.newdialog)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val txt: TextView = logoutDialog.findViewById(R.id.txtsuccess)
        val ok: ConstraintLayout = logoutDialog.findViewById(R.id.ok)
        //   val img:ImageView=logoutDialog.findViewById()

        txt.text = "You have fake gps location apps.You cannot use this application."

        ok.setSafeOnClickListener {
            logoutDialog.dismiss()
            finish()
        }
        logoutDialog.show()
    }

    override fun onStart() {
        super.onStart()
      //  notificationCheckForSetting()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOTIFICATION_SETTINGS_REQUEST_CODE) {
            // User returned from the notification settings activity
            // Check if notifications are enabled again
//            notificationCheckForSetting()
        }
    }

    private fun checkForPushNotification(){
        try {
            val intent = intent
            if (intent != null && intent.extras != null) {
                val extras = intent.extras
                isPushNotification = extras!!.getBoolean("isPushNotification", false)
            }
        } catch (exp: Exception) {
            LoggerGenratter.getInstance().printLog("LOGIN",  "checkForPushNotification: ${exp.message} ")
        }
    }

    private fun notificationPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            if (!areNotificationsEnabled) {
                val sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val manager =
                    applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "fcm_default_channel"
                val channelName = "task_name"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    manager.createNotificationChannel(channel)
                }
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                    applicationContext, channelId
                )
                    .setContentTitle("HRMS App is running")
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText("")
                    .setSmallIcon(R.drawable.logoapp)
                    .setSound(sounduri)
                manager.notify(0, builder.build())
            }
        }
    }
}