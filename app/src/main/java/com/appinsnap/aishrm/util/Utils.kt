package com.appinsnap.aishrm.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.appinsnap.aishrm.MyApplication
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.base.activities.RickAndMortyActivity
import okhttp3.*
import java.io.IOException
import java.io.InputStream


object Utils {
    fun changeStatusBarColor(color: Int, context: FragmentActivity?, dark: Boolean) {

        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = context.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(context, color)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark)
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else
                        window.decorView.systemUiVisibility =
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                }
            }
        }


        /*val window: Window = context.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(
                context,
                color
            )
        }
        context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/
    }

    fun getHome(activity: FragmentActivity?): RickAndMortyActivity? {
        return activity as RickAndMortyActivity
    }

    fun hideKeyboard(activity: Activity?) {
        try {
            val inputManager = activity
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun statusBarTheme(activity: FragmentActivity?) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                activity?.window?.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                activity?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            else -> {

            }
        }
    }

    /* fun navigateTo(activity: FragmentActivity?, navDirections: NavDirections) {
         hideKeyboard(activity)
         activity?.findNavController(R.id.nav_host)?.navigate(navDirections)
     }

     fun popBackStack(activity: FragmentActivity?) {
         hideKeyboard(activity)
         activity?.findNavController(R.id.nav_host)?.popBackStack()
     }

     fun hideDropDown(powerSpinnerView: PowerSpinnerView?) {
         powerSpinnerView?.dismiss()
     }*/
    fun hasInternetConnection(): Boolean {
        val connectivityManager = MyApplication.get()?.activity!!.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val netInfo: NetworkInfo = connectivityManager.activeNetworkInfo!!
            return netInfo.isConnectedOrConnecting
        }

    }


}
