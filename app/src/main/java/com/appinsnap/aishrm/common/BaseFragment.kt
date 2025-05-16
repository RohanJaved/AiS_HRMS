package com.appinsnap.aishrm.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.appinsnap.aishrm.util.popBackStackAllInstances


open class BaseFragment : Fragment() {
    var progressDialog: ProgressBarDialog? = null
    var isNavigated = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }
    fun showProgressDialog(isShow: Boolean) {
        if(isShow){
            if (progressDialog == null) {
                progressDialog = ProgressBarDialog(requireContext())
            }
            progressDialog?.show()
        }else{
            dismissProgressDialog()
        }
    }
    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().popBackStackAllInstances(
                        navController.currentBackStackEntry?.destination?.id!!,
                        true
                    )
                } else
                    navController.popBackStack()
            }
    }


}