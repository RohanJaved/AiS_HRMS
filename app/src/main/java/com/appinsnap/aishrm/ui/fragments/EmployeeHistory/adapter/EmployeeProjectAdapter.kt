package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LeavesitemBinding
import com.appinsnap.aishrm.databinding.RvprojectitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.leavesdata
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EmployeeProjectAdapter(val context: Context): RecyclerView.Adapter<EmployeeProjectAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvprojectitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvprojectitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 12
    }
}