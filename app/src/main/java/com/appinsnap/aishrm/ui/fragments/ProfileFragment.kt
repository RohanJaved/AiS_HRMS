package com.appinsnap.aishrm.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.BaseFragment
import com.appinsnap.aishrm.databinding.FragmentProfile2Binding
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener


class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfile2Binding
    private var sessionmanagement: SessionManagement? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTransitionImage()
    }

    private fun getTransitionImage() {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile2, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())

        onClickListeners()
        providingEmployeeInformatio()
        return view
    }

    private fun providingEmployeeInformatio() {

        var fullname = sessionmanagement?.getName()
        var employeeId = sessionmanagement?.getEmpId()
        var designation = sessionmanagement?.getDesignation()
        var department = sessionmanagement?.getDepartment()
        var reportingto = sessionmanagement?.getReportingTo()
        var cellno = sessionmanagement?.getCellNo()
        binding.employeeId.text=employeeId
        binding.txtname.text = fullname
        binding.profileName.text = fullname
        binding.txtdesignation.text = designation
        binding.txtdepartment.text = department
        binding.txtreportingto.text = reportingto
        binding.txtcellno.text = cellno


    }


    private fun onClickListeners() {
        binding.btnEditProfile.setSafeOnClickListener {
            findNavController().navigate(R.id.action_profilefragment_to_updateProfile)
        }
    }


}