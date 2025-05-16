package com.appinsnap.aishrm.ui.fragments.approval

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.FragmentApprovalBinding
import com.appinsnap.aishrm.ui.fragments.adapter.FragmentPageAdapter
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment() {
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private var _binding:FragmentApprovalBinding ? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_approval, container, false)
        val view = binding.root
    //    settingUpTabLayout()
        return view
    }

//    private fun settingUpTabLayout() {
//        val adapter = activity?.supportFragmentManager?.let { FragmentPageAdapter(
//            it,
//            lifecycle,
//            allnotificationstatuslist
//        ) }
//        binding.tab.addTab(binding.tab.newTab().setText("Pending"))
//        binding.tab.addTab(binding.tab.newTab().setText("Rejected"))
//        binding.tab.addTab(binding.tab.newTab().setText("Approved"))
//        binding.viewpager.adapter = adapter
//        binding.tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (tab != null) {
//                    binding.viewpager.currentItem = tab.position
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//        })
//        binding.viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//
//                super.onPageSelected(position)
//                binding.tab.selectTab(binding.tab.getTabAt(position))
//
//            }
//        })
//    }


}