package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LeavesitemBinding
import com.appinsnap.aishrm.databinding.ManagerdepartmentitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXXX
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import com.bumptech.glide.Glide

class ManagerAttendanceAdapter(val context:Context,val list:List<BodyXX>,val onClick:onAttendanceClick):RecyclerView.Adapter<ManagerAttendanceAdapter.ViewHolder>() {
    class ViewHolder(val binding: ManagerdepartmentitemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ManagerdepartmentitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txttotalabsentcount.text = list.get(position).absentCount.toString()
        holder.binding.txtlatearrivalcount.text = list.get(position).lateCount.toString()
        holder.binding.txttotalpresentcount.text = list.get(position).presentCount.toString()
        holder.binding.txttotalleavescount.text = list.get(position).leaveCount.toString()
        holder.binding.mcvemployeestrength.setSafeOnClickListener {

                onClick.onAttendanceItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface onAttendanceClick{
        fun onAttendanceItemClick(position:Int)
    }


}