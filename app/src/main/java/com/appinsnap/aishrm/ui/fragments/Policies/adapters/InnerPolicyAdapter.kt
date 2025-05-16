package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.FilesitemlayoutBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.Attachmentlist
import com.appinsnap.aishrm.ui.fragments.Policies.models.PolicyData

class InnerPolicyAdapter(val context:Context,val list:ArrayList<Attachmentlist>,val onPolicyClick:(Url:String,name:String)->Unit):RecyclerView.Adapter<InnerPolicyAdapter.ViewHolder>(){
    class ViewHolder(val binding: FilesitemlayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FilesitemlayoutBinding.inflate(layoutInflater, parent, false)
        return InnerPolicyAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtpolicies.text = list.get(position).title
        holder.itemView.setOnClickListener{

            onPolicyClick(list.get(position).attachments,list.get(position).title)
        }
    }

}