package com.appinsnap.aishrm.ui.fragments.leaves.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LeavesstatusitemBinding
import com.appinsnap.aishrm.ui.fragments.leaves.models.RecentLeave

class RecentLeavesAdapter(val context: Context, val list: List<RecentLeave>?): RecyclerView.Adapter<RecentLeavesAdapter.ViewHolder>() {

    class ViewHolder(val binding:LeavesstatusitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =LeavesstatusitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listposition = list!!.get(position)
        if(listposition.status!=null){
        if(listposition?.status?.lowercase()!!.contains("approved"))
        {
            holder.binding.txtapplieddate.text = listposition.appliedDate
            holder.binding.leavesstatus.text = listposition?.status
            holder.binding.leavesstatus.setTextColor(ContextCompat.getColor(context, R.color.present_green))
            holder.binding.attendancedate.text = listposition.startDate+"  "+"-"+"  "+listposition.endDate
            holder.binding.leavestitle.text = listposition.leaveType
        }
       else if(listposition?.status?.lowercase()!!.contains("accepted"))
        {
            holder.binding.txtapplieddate.text = listposition.appliedDate
            holder.binding.leavesstatus.text = listposition?.status
            holder.binding.leavesstatus.setTextColor(ContextCompat.getColor(context, R.color.present_green))
            holder.binding.attendancedate.text = listposition.startDate+"  "+"-"+"  "+listposition.endDate
            holder.binding.leavestitle.text = listposition.leaveType
        }

       else if(listposition?.status!!.lowercase().contains("rejected"))
        {
            holder.binding.txtapplieddate.text = listposition.appliedDate
            holder.binding.leavesstatus.text = listposition?.status
            holder.binding.leavesstatus.setTextColor(ContextCompat.getColor(context, R.color.absent_red))
            holder.binding.attendancedate.text = listposition.startDate+"  "+"-"+"  "+listposition.endDate
            holder.binding.leavestitle.text = listposition.leaveType
        }
        else
        {
            holder.binding.txtapplieddate.text = listposition.appliedDate
            holder.binding.leavesstatus.text = listposition?.status
            holder.binding.leavesstatus.setTextColor(ContextCompat.getColor(context, R.color.latearrival_yellow))
            holder.binding.attendancedate.text = listposition.startDate+"  "+"-"+"  "+listposition.endDate
            holder.binding.leavestitle.text = listposition.leaveType
        }
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }



}