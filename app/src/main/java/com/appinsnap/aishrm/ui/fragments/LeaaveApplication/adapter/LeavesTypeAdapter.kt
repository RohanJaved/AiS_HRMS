package com.appinsnap.aishrm.ui.fragments.LeaaveApplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LeavestypeitemBinding
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXX
import com.appinsnap.aishrm.ui.fragments.leaves.models.GetLeaveCount
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import java.util.ArrayList

class LeavesTypeAdapter(
    val context: Context,
    val leavestypelist: List<GetLeaveCount>?,
    val onItemClick:onitemleaveclick
    ): RecyclerView.Adapter<LeavesTypeAdapter.ViewHolder>() {
    class ViewHolder(val binding: LeavestypeitemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LeavestypeitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemposition = leavestypelist?.get(position)
       holder.binding.leavetype.text = itemposition?.name
        holder.itemView.setOnClickListener {
            onItemClick.onleaveclick(position)
        }
        }


    override fun getItemCount(): Int {
        return leavestypelist!!.size
    }




    interface onitemleaveclick

    {
       fun onleaveclick(position: Int)
        {
        }
    }



}