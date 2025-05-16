package com.wallet.zindigi.Utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.appinsnap.aishrm.util.TinyDB

/*kami*/
class PermissionsUtil private constructor(private val context: Context, private val registry: ActivityResultRegistry) : DefaultLifecycleObserver {
    private lateinit var callBack:
            PermissionsListenerCallback
    private lateinit var getContent: ActivityResultLauncher<Array<String>>

    var tinyDB: TinyDB?=null

    override fun onCreate(owner: LifecycleOwner) {
        tinyDB= TinyDB(context)
        getContent = registry.register(KEY, owner, ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                this.callBack.onPermissionGranted()
            } else permissions.forEach {
                if (!it.value) {
                    tinyDB?.let { tinyDb->
                        val tinyKey=KEY.replace("android.permission.","")
                        this.callBack.onPermissionCancel()
                        tinyDb.putInt(tinyKey,tinyDb.getInt(tinyKey)+1)
                    }
                    return@register
                }
            }
        }
    }

    fun requestPermission(PermissionMsg:String,@NonNull permission: Array<String>,showDialog:Boolean, callBack: PermissionsListenerCallback) {
        if(::getContent.isInitialized && context!=null){
            this.callBack = callBack
            if (checkForPermissions(permission)) this.callBack.onPermissionGranted()
            else{
                tinyDB?.let { tinyDb->
                    val tinyKey=KEY.replace("android.permission.","")
                    if(showDialog){
                        if(tinyDb.getInt(tinyKey)>1){
                            showAlertDialog(PermissionMsg)
                        }else{
                            getContent.launch(permission)
                        }
                    }else{
                        getContent.launch(permission)
                    }

                }
            }

        }

    }

    fun checkForPermissions(permission: Array<String>): Boolean {
        permission.forEach { if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) return false }
        return true
    }

    fun showAlertDialog(permissionMessage: String) {
        context?.let {
            try {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setMessage(permissionMessage)
                alertDialogBuilder.setPositiveButton("Allow"
                ) { arg0, arg1 ->
                    openSetting()
                }
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                    if(::callBack.isInitialized) callBack.onPermissionCancel()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            } catch (e: Exception) {
            }
        }

    }

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(KEY_PACKAGE, context.packageName, null))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    interface PermissionsListenerCallback {
        fun onPermissionGranted()
        fun onPermissionCancel()
    }

    companion object {
        private const val KEY = "key"
        private const val KEY_PACKAGE = "package"

        @JvmStatic
        fun getInstance(context: Context, registry: ActivityResultRegistry): PermissionsUtil {
            return PermissionsUtil(context, registry)
        }
    }
}