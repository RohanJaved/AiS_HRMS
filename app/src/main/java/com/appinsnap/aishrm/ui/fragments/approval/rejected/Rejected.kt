package com.appinsnap.aishrm.ui.fragments.approval.rejected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentRejectedBinding
import com.appinsnap.aishrm.ui.fragments.history.adapters.NotificationStatusAdapter
import com.appinsnap.aishrm.ui.fragments.history.models.Body
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Rejected(val allnotificationstatuslist:List<Body>) : Fragment(),NotificationStatusAdapter.onRequestClick {

    private var _binding: FragmentRejectedBinding? = null
    private var rejectedlist: ArrayList<Body> = ArrayList()
    private var selectedposition:Int=-1
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rejected, container, false)
        val view = binding.root
        initRecyclerView()
        // Inflate the layout for this fragment
        return view
        // Inflate the layout for this fragment

    }

    private fun initRecyclerView() {

        if (allnotificationstatuslist.isNotEmpty()) {
            val size = allnotificationstatuslist.size
            allnotificationstatuslist.forEach {
                if (it.status.toString().lowercase().contains("reject")) {
                    var empname=""
                    var empcheckIn=""
                    var empcheckOut=""
                    var category=""
                    var leaveType = ""
                    var comments = ""
                    var fromdate = ""
                    var todate = ""
                    var typeid=-1
                    if(it.name!=null)
                    {
                        empname = it.name
                    }
                    if(it.checkin!=null)
                    {
                        empcheckIn = it.checkin
                    }
                    if(it.checkout!=null)
                    {
                        empcheckOut = it.checkout
                    }
                    if(it.category!=null)
                    {
                        category = it.category
                    }
                    if(it.leaveType!=null)
                    {
                        leaveType = it.leaveType
                    }
                    if(it.comments!=null)
                    {
                        comments = it.comments
                    }
                    if(it.typeid!=null)
                    {
                        typeid = it.typeid
                    }
                    if(it.fromDate!=null)
                    {
                        fromdate = it.fromDate
                    }
                    if(it.toDate!=null)
                    {
                        todate = it.toDate
                    }
                        rejectedlist.add(Body(it.employeeID,fromdate,it.isEnabled,it.message,it.notificationDateTime,it.notificationID,it.status,it.title,todate,it.type,empname,empcheckIn,empcheckOut,category,leaveType,comments,typeid))

                }
            }

            if(rejectedlist.isNotEmpty()) {
                binding.rejectedrecyclerview.show()
                binding.txtrejected.hide()
                val adapter = NotificationStatusAdapter(requireContext(), rejectedlist,this)
                binding.rejectedrecyclerview.adapter = adapter
            }
            else{
                binding.rejectedrecyclerview.hide()
                binding.txtrejected.show()
            }
        }
        else{
            binding.rejectedrecyclerview.hide()
            binding.txtrejected.show()
        }

    }

    override fun onRequestItemClick(position: Int, notificationid: String) {
    }
}