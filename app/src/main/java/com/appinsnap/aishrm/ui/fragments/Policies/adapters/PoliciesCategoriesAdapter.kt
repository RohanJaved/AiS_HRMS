package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.HistorydaterecyclerviewitemBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetPoliciesType

class PoliciesCategoriesAdapter(val context: Context, val list:ArrayList<GetPoliciesType>,val onPolicyTypeClick:onCategoryPolicyClick): RecyclerView.Adapter<PoliciesCategoriesAdapter.ViewHolder>(){
    class ViewHolder(val binding: HistorydaterecyclerviewitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HistorydaterecyclerviewitemBinding.inflate(layoutInflater, parent, false)
        return PoliciesCategoriesAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemlayout.setOnClickListener {
            onPolicyTypeClick.onPolicyTypeClick(position)
        }
        holder.binding.txtdate.text = list.get(position).name
    }
    interface onCategoryPolicyClick{
        fun onPolicyTypeClick(position: Int)
    }

}