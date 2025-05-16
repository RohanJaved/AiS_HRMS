package com.appinsnap.aishrm.ui.fragments.attendancehistory.adapter

import android.content.Context
import com.appinsnap.aishrm.databinding.AttendancehistoryitemBinding
import com.appinsnap.aishrm.ui.fragments.attendancehistory.models.AttendanceHistoryModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AttendanceHistoryAdapter(val context: Context,val attendancehistory:ArrayList<AttendanceHistoryModel>): RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding:AttendancehistoryitemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AttendancehistoryitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtname.text = attendancehistory.get(position).nameHistory
        holder.binding.txtemail.text = attendancehistory.get(position).emailHistory
        holder.binding.txtdatehistory.text = attendancehistory.get(position).dateHistory
        holder.binding.txtcheckintimehistory.text=attendancehistory.get(position).checkInTimeHistory
        holder.binding.txtcheckouthistory.text = attendancehistory.get(position).checkOutTimeHistory

    }

    override fun getItemCount(): Int {
        return attendancehistory.size
    }
}