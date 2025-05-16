package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.HistorydaterecyclerviewitemBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetPoliciesType
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetSOPSType

class SOPCategoriesAdapter(val context: Context, val list:ArrayList<GetSOPSType>, val onPolicyTypeClick:onCategorySOPClick): RecyclerView.Adapter<SOPCategoriesAdapter.ViewHolder>(){
    class ViewHolder(val binding: HistorydaterecyclerviewitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HistorydaterecyclerviewitemBinding.inflate(layoutInflater, parent, false)
        return SOPCategoriesAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemlayout.setOnClickListener {
            onPolicyTypeClick.onSOPTypeClick(position)
        }
        holder.binding.txtdate.text = list.get(position).name
    }
    interface onCategorySOPClick{
        fun onSOPTypeClick(position: Int)
    }

}