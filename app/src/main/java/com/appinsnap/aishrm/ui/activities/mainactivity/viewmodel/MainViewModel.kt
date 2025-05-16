package com.appinsnap.aishrm.ui.activities.mainactivity.viewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel(), NavController.OnDestinationChangedListener{
    var isConnected=false

    private lateinit var progressBarDialogRegister: ProgressBarDialog
    var mActivity: FragmentActivity?=null
    private var sessionmanagement: SessionManagement? = null

    /*Observable*/
    var toolbarTitle: ObservableField<String> = ObservableField("Home")
    var toolbsrprofileimg: ObservableField<Int> = ObservableField(R.drawable.notification_bell)

    /*Visibility*/
    var imgBurgerMenuVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var toolbsrprofileimgVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var navigationiconVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    var toolbarVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun setup(activity: FragmentActivity) {
        mActivity=activity
        sessionmanagement = SessionManagement(activity)

//        setNavGraph()
    }

    fun createDialogs(mInstance: MainActivity) {
        this.progressBarDialogRegister = ProgressBarDialog(mInstance)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.timelinefragment -> {
                handletoolbarintimelinefragment()
            }
            R.id.checkinoutfragment -> {
                handletoolbarincheckinoutfragment()
            }
            R.id.homefragment -> {
                handletoolbarinhomefragment()
            }
            R.id.profilefragment -> {
                handletoolbarinprofilefragment()
            }
            R.id.updateprofilefragment -> {
                handletoolbarinupdateprofilefragment()
            }
            R.id.attendancehistoryfragment -> {
                handletoolbarinattendancehistoryfragment()
            }
            R.id.notificationfragment -> {
                handletoolbarinnotificatioinfragment()
            }

        }

    }

    private fun handletoolbarinattendancehistoryfragment() {
        toolbarTitle.set("History")
        imgBurgerMenuVisibility.set(View.GONE)
        toolbsrprofileimgVisibility.set(View.INVISIBLE)

    }

    private fun handletoolbarinnotificatioinfragment() {
        toolbarTitle.set("Notification")
        imgBurgerMenuVisibility.set(View.GONE)
        toolbsrprofileimgVisibility.set(View.VISIBLE)
        toolbsrprofileimgVisibility.set(View.GONE)
        navigationiconVisibility.set(View.VISIBLE)

    }

    private fun handletoolbarinupdateprofilefragment() {
        toolbarTitle.set("Update Profile")
        imgBurgerMenuVisibility.set(View.GONE)
        navigationiconVisibility.set(View.VISIBLE)
        toolbarVisibility.set(View.VISIBLE)


    }

    private fun handletoolbarintimelinefragment() {
        toolbarVisibility.set(View.GONE)
    }


    private fun handletoolbarincheckinoutfragment() {
        toolbarTitle.set("History")
        imgBurgerMenuVisibility.set(View.INVISIBLE)
        navigationiconVisibility.set(View.VISIBLE)
        toolbarVisibility.set(View.VISIBLE)
        toolbsrprofileimgVisibility.set(View.INVISIBLE)

    }

    private fun handletoolbarinhomefragment() {

        toolbarTitle.set("Dashboard")
        imgBurgerMenuVisibility.set(View.VISIBLE)
        navigationiconVisibility.set(View.GONE)
        toolbarVisibility.set(View.VISIBLE)
        toolbsrprofileimg.set(R.drawable.notification_bell)
        toolbsrprofileimgVisibility.set(View.VISIBLE)

    }

    private fun handletoolbarinprofilefragment() {
        toolbarTitle.set("Profile")
        imgBurgerMenuVisibility.set(View.GONE)
        navigationiconVisibility.set(View.VISIBLE)
        toolbarVisibility.set(View.VISIBLE)
        toolbsrprofileimgVisibility.set(View.INVISIBLE)
    }

}