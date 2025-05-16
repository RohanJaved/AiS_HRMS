package com.appinsnap.aishrm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.BaseFragment
import com.appinsnap.aishrm.databinding.FragmentTimelineBinding
import com.appinsnap.aishrm.ui.list.adapter.ProjectTimelineAdapter

class TimelineFragment : BaseFragment() {
    private lateinit var binding: FragmentTimelineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false)
        val view = binding.root
        populatetimelinesheetdata()
        return view
    }

    private fun populatetimelinesheetdata() {
        val adapter = ProjectTimelineAdapter(requireContext())
        binding.rvprojecttimeline.adapter = adapter
    }


}