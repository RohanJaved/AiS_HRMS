package com.appinsnap.aishrm.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LeavesitemBinding
import com.appinsnap.aishrm.databinding.TimelinesheetitemBinding

class ProjectTimelineAdapter(val context: Context): RecyclerView.Adapter<ProjectTimelineAdapter.ViewHolder>() {
    class ViewHolder(val binding: TimelinesheetitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimelinesheetitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }
}