package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LeavestypeitemBinding
import com.appinsnap.aishrm.ui.fragments.LeaaveApplication.adapter.LeavesTypeAdapter
import com.appinsnap.aishrm.ui.fragments.home.adapter.LeavesAdapter
import com.appinsnap.aishrm.ui.fragments.home.models.GetLeaveCounts
import com.appinsnap.aishrm.ui.fragments.leaves.models.GetLeaveCount

class LeaveAdapter(
    val leavestypelist: List<GetLeaveCounts>?,val onleaceclick:onItemClick
): RecyclerView.Adapter<LeaveAdapter.ViewHolder>() {
    class ViewHolder(val binding: LeavestypeitemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeavestypeitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemposition = leavestypelist?.get(position)
       holder.binding.leavetype.text = itemposition?.name
        holder.binding.categorylayout.setOnClickListener {
            onleaceclick.onLeaveItemClick(position)
        }
    }


    override fun getItemCount(): Int {
        return leavestypelist!!.size
    }
    interface onItemClick{
        fun onLeaveItemClick(position: Int)
        {

        }
    }
}