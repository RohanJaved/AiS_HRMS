package com.appinsnap.aishrm.ui.fragments.Policies

import android.Manifest
import android.app.ActionBar
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.FragmentPdfViewBinding
import com.appinsnap.aishrm.util.AppUtils
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.RetrievePDFFromURL
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.wallet.zindigi.Utils.PermissionsUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PdfView : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission{
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    private var _binding: FragmentPdfViewBinding?=null
    private val binding get() = _binding!!
    val navArg: PdfViewArgs by navArgs()
    private var pdfUrl:String = ""
    private var pdfName:String=""
    val MAX_RETRY_ATTEMPTS = 3
    var retryAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pdf_view, container, false)
        val view = binding.root
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        return view
    }

    override fun onclick(value: Boolean) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
        onClickListeners()
    }



    private fun initWebView() {
        if(navArg.pdfData!=null)
        {
            if(navArg.pdfData!!.pdfurl.isNotEmpty())
            {
                requireActivity().findViewById<ConstraintLayout>(R.id.toolbarlayout)
                    .findViewById<TextView>(R.id.tvToolbarTitle).text = navArg.pdfData!!.pdfName
                pdfName = navArg.pdfData!!.pdfName
                pdfUrl = navArg.pdfData!!.pdfurl
                RetrievePDFFromURL(binding.progressBar,binding.idPDFView).execute(pdfUrl)
//                binding.webView.settings.javaScriptEnabled = true
//                binding.webView.webViewClient = WebViewClient()
//                binding.webView.webChromeClient = object : WebChromeClient() {
//                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                        super.onProgressChanged(view, newProgress)
//                        if (newProgress < 100) {
//                            binding.progressBar.visibility = View.VISIBLE
//                        } else {
//                            // Hide the ProgressBar when the page finishes loading
//                            binding.progressBar.visibility = View.GONE
//                        }
//                    }
//                }
//                val googleDocsViewerUrl = "https://docs.google.com/gview?embedded=true&url=$pdfUrl"
//                binding.webView.loadUrl(googleDocsViewerUrl)

            }
        }
    }





    private fun onClickListeners() {
 binding.btndownload.setOnClickListener {
     downloadConfirmationDialog()
 }
    }

    private fun downloadConfirmationDialog() {
        val logoutDialog = Dialog(requireContext())
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
        val txt :TextView = logoutDialog.findViewById(R.id.txtSuccess)
        txt.text = "Are you sure you want to download this file?"
        no.setSafeOnClickListener {
            logoutDialog.dismiss()
        }

        yes.setSafeOnClickListener {
            getPermissions()
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Camera Permission",
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                           downloadPdf()
                        }

                        override fun onPermissionCancel() {
                        }
                    })
            }
        } else {
            AppUtils.getMain(requireActivity()).permissionsUtil?.let { permissionsUtil ->
                permissionsUtil.requestPermission(
                    "Please Allow Storage Permission",
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    true,
                    object : PermissionsUtil.PermissionsListenerCallback {
                        override fun onPermissionGranted() {
                            downloadPdf()
                        }

                        override fun onPermissionCancel() {
                        }
                    })
            }
        }
    }

    private fun downloadPdf():Long {
        val fileName = "$pdfName"+ System.currentTimeMillis()
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Ensure the directory exists
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }
        val extension = pdfUrl?.substring(pdfUrl.lastIndexOf("."))
        val dm: DownloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(pdfUrl)
        val request = DownloadManager.Request(uri)

        // Set the destination directory in the Downloads folder
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            fileName + extension
        )

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(fileName)
        showLeaveSuccessMesaage("Your file has been downloaded successfully")
        val downloadReference = dm.enqueue(request)
        return downloadReference
    }


    override fun onLocationPermission(locationvalue: Boolean) {

    }
    private fun showLeaveSuccessMesaage(statusMessage: String) {
        val permissiondialog = Dialog(requireContext())
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