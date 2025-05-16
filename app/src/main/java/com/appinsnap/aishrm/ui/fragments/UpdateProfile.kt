package com.appinsnap.aishrm.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.BaseFragment
import com.appinsnap.aishrm.databinding.FragmentUpdateProfileBinding
import com.appinsnap.aishrm.util.SessionManagement

class UpdateProfile : BaseFragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private var sessionmanagement: SessionManagement? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_profile, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        providimgEmployeeInformation()

        return view
    }

    private fun providimgEmployeeInformation() {
        var fullname = sessionmanagement?.getName()
        binding.profilename.text = fullname

    }
}