package com.appinsnap.aishrm.ui.fragments.leavesDetails.adapter

import android.Manifest
import android.app.ActionBar
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.BuildConfig
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.LeaveDetailsItemBinding
import com.appinsnap.aishrm.util.AppUtils
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.bumptech.glide.Glide
import com.wallet.zindigi.Utils.PermissionsUtil


class LeaveDetailsAdapter(private val context: Context, private val requAc:FragmentActivity , private val urlList: List<String>): RecyclerView.Adapter<LeaveDetailsAdapter.ViewHolder>() {

    class ViewHolder(val binding: LeaveDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val webView: WebView = binding.webView

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaveDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeaveDetailsItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serverUrl = BuildConfig.SERVER_URL
        val finalServerUrl = serverUrl.substring(0,serverUrl.length-4)
        val url = urlList[position]
        val fullUrl = "$finalServerUrl$url"
        Log.d("FullUrl", "" + fullUrl)
        if (fullUrl.contains("jpg") || fullUrl.contains("png") || fullUrl.contains("jpeg")) {
            holder.binding.imageview.show()
            Glide.with(context).load(fullUrl).into(holder.binding.imageview)
        }
        else {
            holder.binding.webView.show()
            holder.webView.settings.javaScriptEnabled = true
            holder.webView.webViewClient = WebViewClient()
//            holder.webView.settings.allowFileAccess = true
//            holder.webView.settings.domStorageEnabled = true
//            holder.webView.settings.allowContentAccess = true
            holder.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$fullUrl")
            holder.webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress < 100) {
                        holder.binding.progressBar.visibility = View.VISIBLE
                    } else {
                        // Hide the ProgressBar when the page finishes loading
                        holder.binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        holder.binding.downLoad.setOnClickListener {
            val serverUrl = BuildConfig.SERVER_URL
            val finalServerUrl = serverUrl.substring(0,serverUrl.length-4)
            val url = urlList[position]
            val fullUrl = "$finalServerUrl$url"
            Log.d("FullUrl", "" + fullUrl)
            val fileUrl = fullUrl
            val fileName = "Ais_Hrms_"+ System.currentTimeMillis()
            imagePickerDialog(context, fileUrl, fileName)
        }
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    fun downloadPdf(context: Context, url: String?, title: String?): Long {
        // Choose a standard download directory or create a custom one
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Ensure the directory exists
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }
        val extension = url?.substring(url.lastIndexOf("."))
        val dm: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)

        // Set the destination directory in the Downloads folder
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            title + extension
        )

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(title)
        showLeaveSuccessMesaage("Your file has been downloaded successfully")
        val downloadReference = dm.enqueue(request)
        return downloadReference
    }

    fun getPermissions(context: Context, url: String?, title : String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppUtils.getMain(requAc).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Camera Permission",
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            downloadPdf(context, url, title)
                        }

                        override fun onPermissionCancel() {
                        }
                    })
            }
        } else {
            AppUtils.getMain(requAc).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Storage Permission",
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            downloadPdf(context, url, title)
                        }

                        override fun onPermissionCancel() {
                        }
                    })
            }
        }
    }

    private fun imagePickerDialog(context: Context, fileUrl: String, fileName: String) {
        val logoutDialog = Dialog(this.context)
        logoutDialog.setCancelable(false)
        logoutDialog.setCanceledOnTouchOutside(false)
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.setContentView(R.layout.layout_download_dialog)
//        logoutDialog.window?.setWindowAnimations(R.style.DialogNoAnimation)
        logoutDialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        val no: ConstraintLayout = logoutDialog.findViewById(R.id.no)
        val yes: ConstraintLayout = logoutDialog.findViewById(R.id.yes)

        no.setSafeOnClickListener {
            logoutDialog.dismiss()
        }

        yes.setSafeOnClickListener {
            getPermissions(context,fileUrl,fileName)
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

    private fun showLeaveSuccessMesaage(statusMessage: String) {
        val permissiondialog = Dialog(context)
        permissiondialog.setCancelable(false)
        permissiondialog.setCanceledOnTouchOutside(true)
        permissiondialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        permissiondialog.setContentView(R.layout.general_dialog_design)
        permissiondialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)

        val okbtn: ConstraintLayout = permissiondialog.findViewById(R.id.ok)
        val popicon: ImageView = permissiondialog.findViewById(R.id.imgPopIcon)
        val textmessage: TextView = permissiondialog.findViewById(R.id.txtsuccess)
        popicon.setImageResource(R.drawable.tickicon)
        popicon.layoutParams.height = 130
        popicon.layoutParams.width = 130

        textmessage.text = "$statusMessage"

        okbtn.setSafeOnClickListener {
            permissiondialog.dismiss()
        }
        if (permissiondialog.isShowing) {
            permissiondialog.dismiss()
        }
        permissiondialog.show()

    }


}